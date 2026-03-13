#!/usr/bin/env bash
# ==============================================================================
# env-start.sh - Start or destroy a K8s environment
#
# Usage:
#   ./deploy/scripts/env-start.sh <env>              # Start environment
#   ./deploy/scripts/env-start.sh <env> --destroy     # Destroy environment
#   ./deploy/scripts/env-start.sh <env> --status      # Check environment status
#
# Where <env> is one of: dev, staging, prod
# ==============================================================================
set -euo pipefail

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

info()  { printf "${GREEN}[INFO]${NC}  %s\n" "$*"; }
warn()  { printf "${YELLOW}[WARN]${NC}  %s\n" "$*"; }
error() { printf "${RED}[ERROR]${NC} %s\n" "$*" >&2; }
header(){ printf "${CYAN}%s${NC}\n" "$*"; }

# Resolve project root directory
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"

IMAGE_NAME="domain-driven-transaction-sys"
IMAGE_TAG="latest"

# ---- Argument parsing ----

usage() {
    echo "Usage: $0 <env> [--destroy|--status]"
    echo ""
    echo "  env:        dev | staging | prod"
    echo "  --destroy:  Destroy the specified environment"
    echo "  --status:   Show status of the specified environment"
    echo ""
    echo "Examples:"
    echo "  $0 dev                # Start dev environment"
    echo "  $0 staging --destroy  # Destroy staging environment"
    echo "  $0 prod --status      # Check prod environment status"
    exit 1
}

if [ $# -lt 1 ]; then
    usage
fi

ENV="$1"
ACTION="${2:-start}"

case "${ENV}" in
    dev|staging|prod) ;;
    *) error "Invalid environment: ${ENV}. Must be dev, staging, or prod."; usage ;;
esac

case "${ACTION}" in
    start|--start) ACTION="start" ;;
    --destroy)     ACTION="destroy" ;;
    --status)      ACTION="status" ;;
    *) error "Invalid action: ${ACTION}"; usage ;;
esac

NAMESPACE="ddts-${ENV}"
K8S_DIR="${PROJECT_ROOT}/deploy/k8s/${ENV}"

# ---- Pre-flight checks ----

check_prerequisites() {
    local missing=false
    for cmd in podman minikube kubectl; do
        if ! command -v ${cmd} >/dev/null 2>&1; then
            error "${cmd} is not installed. Run ./deploy/scripts/env-init.sh first."
            missing=true
        fi
    done
    if [ "${missing}" = true ]; then
        exit 1
    fi

    if [ ! -d "${K8S_DIR}" ]; then
        error "K8s manifests directory not found: ${K8S_DIR}"
        exit 1
    fi
}

ensure_minikube_running() {
    if minikube status --format='{{.Host}}' 2>/dev/null | grep -q 'Running'; then
        info "minikube is running."
        return
    fi
    info "minikube is not running. Starting..."
    minikube start --driver=podman --memory=4096 --cpus=2
    info "minikube started."
}

# ---- Build ----

build_image() {
    header "==============================="
    header "  Building Docker image"
    header "==============================="
    info "Building ${IMAGE_NAME}:${IMAGE_TAG} ..."

    # Use podman to build
    podman build \
        -t "${IMAGE_NAME}:${IMAGE_TAG}" \
        -f "${PROJECT_ROOT}/deploy/docker/Dockerfile" \
        "${PROJECT_ROOT}"

    info "Image built successfully: ${IMAGE_NAME}:${IMAGE_TAG}"
}

load_image() {
    header "==============================="
    header "  Loading image into minikube"
    header "==============================="

    # Save image from podman and load into minikube
    info "Loading ${IMAGE_NAME}:${IMAGE_TAG} into minikube..."
    podman save "${IMAGE_NAME}:${IMAGE_TAG}" | minikube image load --daemon=false -
    info "Image loaded into minikube."
}

# ---- Deploy ----

deploy_environment() {
    header "==============================="
    header "  Deploying ${ENV} environment"
    header "==============================="

    info "Applying K8s manifests from ${K8S_DIR}/ ..."
    kubectl apply -f "${K8S_DIR}/"

    info ""
    info "Waiting for MariaDB to be ready (timeout: 120s)..."
    kubectl wait pod \
        -l app=mariadb \
        --for=condition=ready \
        -n "${NAMESPACE}" \
        --timeout=120s || {
            warn "MariaDB pod not ready within timeout. Check: kubectl get pods -n ${NAMESPACE}"
        }

    info ""
    info "Waiting for application to be ready (timeout: 300s)..."
    kubectl wait pod \
        -l app=ddts-app \
        --for=condition=ready \
        -n "${NAMESPACE}" \
        --timeout=300s || {
            warn "App pod not ready within timeout. Check: kubectl logs -n ${NAMESPACE} -l app=ddts-app"
        }
}

# ---- Tunnel ----

start_tunnel() {
    header "==============================="
    header "  Setting up LoadBalancer access"
    header "==============================="

    # Check if tunnel is already running
    if pgrep -f "minikube tunnel" >/dev/null 2>&1; then
        info "minikube tunnel is already running."
    else
        info "Starting minikube tunnel in background..."
        info "(You may be prompted for your password for binding network routes)"
        nohup minikube tunnel >/dev/null 2>&1 &
        sleep 3
    fi

    # Wait for external IP
    info "Waiting for LoadBalancer external IP..."
    local retries=0
    local max_retries=15
    local external_ip=""

    while [ ${retries} -lt ${max_retries} ]; do
        external_ip="$(kubectl get svc ddts-app-svc -n "${NAMESPACE}" -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null || true)"
        if [ -n "${external_ip}" ] && [ "${external_ip}" != "<pending>" ]; then
            break
        fi
        retries=$((retries + 1))
        sleep 2
    done

    info ""
    header "========================================"
    header "  Environment ${ENV} is ready!"
    header "========================================"
    if [ -n "${external_ip}" ]; then
        info "Access URL:  http://${external_ip}:8502"
        info "Health:      http://${external_ip}:8502/actuator/health"
    else
        warn "External IP not yet assigned. Check manually:"
        warn "  kubectl get svc ddts-app-svc -n ${NAMESPACE}"
        info ""
        info "You can also use minikube service:"
        info "  minikube service ddts-app-svc -n ${NAMESPACE} --url"
    fi
    info ""
    info "Useful commands:"
    info "  kubectl get pods -n ${NAMESPACE}"
    info "  kubectl logs -n ${NAMESPACE} -l app=ddts-app -f"
    info "  kubectl get svc -n ${NAMESPACE}"
}

# ---- Status ----

show_status() {
    header "==============================="
    header "  Status: ${ENV} environment"
    header "==============================="

    if ! kubectl get namespace "${NAMESPACE}" >/dev/null 2>&1; then
        warn "Namespace ${NAMESPACE} does not exist. Environment is not deployed."
        return
    fi

    info "Namespace: ${NAMESPACE}"
    echo ""
    info "Pods:"
    kubectl get pods -n "${NAMESPACE}" -o wide
    echo ""
    info "Services:"
    kubectl get svc -n "${NAMESPACE}"
    echo ""
    info "PVCs:"
    kubectl get pvc -n "${NAMESPACE}"
}

# ---- Destroy ----

destroy_environment() {
    header "==============================="
    header "  Destroying ${ENV} environment"
    header "==============================="

    if ! kubectl get namespace "${NAMESPACE}" >/dev/null 2>&1; then
        warn "Namespace ${NAMESPACE} does not exist. Nothing to destroy."
        return
    fi

    warn "This will delete namespace ${NAMESPACE} and ALL its resources (including persistent data)."
    printf "Are you sure? [y/N] "
    read -r confirm
    if [ "${confirm}" != "y" ] && [ "${confirm}" != "Y" ]; then
        info "Cancelled."
        return
    fi

    kubectl delete namespace "${NAMESPACE}"
    info "Environment ${ENV} destroyed."
}

# ---- Main ----

main() {
    check_prerequisites

    case "${ACTION}" in
        start)
            ensure_minikube_running
            build_image
            load_image
            deploy_environment
            start_tunnel
            ;;
        destroy)
            destroy_environment
            ;;
        status)
            show_status
            ;;
    esac
}

main
