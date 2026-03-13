#!/usr/bin/env bash
# ==============================================================================
# env-init.sh - One-click environment initialization script
#
# Installs: JDK 8, Podman, kubectl, minikube
# Supports: macOS (Homebrew), Debian/Ubuntu (apt), RHEL/Fedora/CentOS (dnf/yum)
#
# Usage:
#   ./deploy/scripts/env-init.sh
# ==============================================================================
set -euo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

info()  { printf "${GREEN}[INFO]${NC}  %s\n" "$*"; }
warn()  { printf "${YELLOW}[WARN]${NC}  %s\n" "$*"; }
error() { printf "${RED}[ERROR]${NC} %s\n" "$*" >&2; }

# Detect OS
detect_os() {
    OS_TYPE="$(uname -s)"
    case "${OS_TYPE}" in
        Darwin)
            OS="macos"
            ;;
        Linux)
            OS="linux"
            if [ -f /etc/os-release ]; then
                . /etc/os-release
                DISTRO_ID="${ID:-unknown}"
            else
                DISTRO_ID="unknown"
            fi
            ;;
        *)
            error "Unsupported OS: ${OS_TYPE}"
            exit 1
            ;;
    esac
    ARCH="$(uname -m)"
    info "Detected OS: ${OS_TYPE} (${OS}), Arch: ${ARCH}"
    if [ "${OS}" = "linux" ]; then
        info "Linux distribution: ${DISTRO_ID}"
    fi
}

# Detect package manager for Linux
detect_pkg_manager() {
    if [ "${OS}" = "macos" ]; then
        if ! command -v brew >/dev/null 2>&1; then
            error "Homebrew is not installed. Please install it first:"
            error '  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"'
            exit 1
        fi
        PKG_MGR="brew"
        return
    fi

    case "${DISTRO_ID}" in
        ubuntu|debian|linuxmint|pop)
            PKG_MGR="apt"
            ;;
        fedora)
            PKG_MGR="dnf"
            ;;
        centos|rhel|rocky|alma)
            if command -v dnf >/dev/null 2>&1; then
                PKG_MGR="dnf"
            else
                PKG_MGR="yum"
            fi
            ;;
        *)
            if command -v apt-get >/dev/null 2>&1; then
                PKG_MGR="apt"
            elif command -v dnf >/dev/null 2>&1; then
                PKG_MGR="dnf"
            elif command -v yum >/dev/null 2>&1; then
                PKG_MGR="yum"
            else
                error "Could not detect package manager for distro: ${DISTRO_ID}"
                exit 1
            fi
            ;;
    esac
    info "Package manager: ${PKG_MGR}"
}

# ---- Install functions ----

install_java_macos() {
    info "Installing JDK 8 via Homebrew (Eclipse Temurin)..."
    brew install --cask temurin@8
}

install_java_apt() {
    info "Installing JDK 8 via Adoptium APT repository..."
    sudo apt-get update -qq
    sudo apt-get install -y -qq wget apt-transport-https gnupg
    wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | sudo gpg --dearmor -o /usr/share/keyrings/adoptium.gpg 2>/dev/null || true
    echo "deb [signed-by=/usr/share/keyrings/adoptium.gpg] https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print $2}' /etc/os-release) main" | sudo tee /etc/apt/sources.list.d/adoptium.list >/dev/null
    sudo apt-get update -qq
    sudo apt-get install -y -qq temurin-8-jdk
}

install_java_dnf() {
    info "Installing JDK 8 via Adoptium YUM repository..."
    cat <<'REPO' | sudo tee /etc/yum.repos.d/adoptium.repo >/dev/null
[Adoptium]
name=Adoptium
baseurl=https://packages.adoptium.net/artifactory/rpm/centos/$releasever/$basearch
enabled=1
gpgcheck=1
gpgkey=https://packages.adoptium.net/artifactory/api/gpg/key/public
REPO
    sudo ${PKG_MGR} install -y temurin-8-jdk
}

install_java() {
    if command -v java >/dev/null 2>&1; then
        JAVA_VER="$(java -version 2>&1 | head -1)"
        info "Java already installed: ${JAVA_VER}"
        return
    fi
    case "${PKG_MGR}" in
        brew) install_java_macos ;;
        apt)  install_java_apt ;;
        dnf|yum) install_java_dnf ;;
    esac
}

install_podman() {
    if command -v podman >/dev/null 2>&1; then
        info "Podman already installed: $(podman --version)"
        # On macOS, ensure podman machine is initialized and started
        if [ "${OS}" = "macos" ]; then
            if ! podman machine list --format '{{.Name}}' 2>/dev/null | grep -q .; then
                info "Initializing podman machine..."
                podman machine init
            fi
            if ! podman machine list --format '{{.Running}}' 2>/dev/null | grep -qi 'true'; then
                info "Starting podman machine..."
                podman machine start || true
            fi
        fi
        return
    fi

    info "Installing Podman..."
    case "${PKG_MGR}" in
        brew)
            brew install podman
            info "Initializing podman machine..."
            podman machine init
            podman machine start
            ;;
        apt)
            sudo apt-get update -qq
            sudo apt-get install -y -qq podman
            ;;
        dnf|yum)
            sudo ${PKG_MGR} install -y podman
            ;;
    esac
}

install_kubectl() {
    if command -v kubectl >/dev/null 2>&1; then
        info "kubectl already installed: $(kubectl version --client --short 2>/dev/null || kubectl version --client 2>&1 | head -1)"
        return
    fi

    info "Installing kubectl..."
    case "${PKG_MGR}" in
        brew)
            brew install kubectl
            ;;
        apt)
            sudo apt-get update -qq
            sudo apt-get install -y -qq apt-transport-https ca-certificates curl gnupg
            curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.31/deb/Release.key | sudo gpg --dearmor -o /usr/share/keyrings/kubernetes-apt-keyring.gpg 2>/dev/null || true
            echo 'deb [signed-by=/usr/share/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.31/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list >/dev/null
            sudo apt-get update -qq
            sudo apt-get install -y -qq kubectl
            ;;
        dnf|yum)
            cat <<'REPO' | sudo tee /etc/yum.repos.d/kubernetes.repo >/dev/null
[kubernetes]
name=Kubernetes
baseurl=https://pkgs.k8s.io/core:/stable:/v1.31/rpm/
enabled=1
gpgcheck=1
gpgkey=https://pkgs.k8s.io/core:/stable:/v1.31/rpm/repodata/repomd.xml.key
REPO
            sudo ${PKG_MGR} install -y kubectl
            ;;
    esac
}

install_minikube() {
    if command -v minikube >/dev/null 2>&1; then
        info "minikube already installed: $(minikube version --short 2>/dev/null || minikube version | head -1)"
        return
    fi

    info "Installing minikube..."
    case "${PKG_MGR}" in
        brew)
            brew install minikube
            ;;
        apt)
            MINIKUBE_ARCH="${ARCH}"
            if [ "${MINIKUBE_ARCH}" = "x86_64" ]; then
                MINIKUBE_ARCH="amd64"
            elif [ "${MINIKUBE_ARCH}" = "aarch64" ]; then
                MINIKUBE_ARCH="arm64"
            fi
            curl -LO "https://storage.googleapis.com/minikube/releases/latest/minikube_latest_${MINIKUBE_ARCH}.deb"
            sudo dpkg -i "minikube_latest_${MINIKUBE_ARCH}.deb"
            rm -f "minikube_latest_${MINIKUBE_ARCH}.deb"
            ;;
        dnf|yum)
            MINIKUBE_ARCH="${ARCH}"
            if [ "${MINIKUBE_ARCH}" = "x86_64" ]; then
                MINIKUBE_ARCH="amd64"
            elif [ "${MINIKUBE_ARCH}" = "aarch64" ]; then
                MINIKUBE_ARCH="arm64"
            fi
            curl -LO "https://storage.googleapis.com/minikube/releases/latest/minikube-latest.${MINIKUBE_ARCH}.rpm"
            sudo rpm -Uvh "minikube-latest.${MINIKUBE_ARCH}.rpm"
            rm -f "minikube-latest.${MINIKUBE_ARCH}.rpm"
            ;;
    esac
}

# ---- Verify installations ----

verify_installations() {
    info "==============================="
    info "Verifying installations..."
    info "==============================="

    local all_ok=true

    if command -v java >/dev/null 2>&1; then
        info "Java:     $(java -version 2>&1 | head -1)"
    else
        error "Java:     NOT FOUND"
        all_ok=false
    fi

    if command -v podman >/dev/null 2>&1; then
        info "Podman:   $(podman --version)"
    else
        error "Podman:   NOT FOUND"
        all_ok=false
    fi

    if command -v kubectl >/dev/null 2>&1; then
        info "kubectl:  $(kubectl version --client --short 2>/dev/null || kubectl version --client 2>&1 | head -1)"
    else
        error "kubectl:  NOT FOUND"
        all_ok=false
    fi

    if command -v minikube >/dev/null 2>&1; then
        info "minikube: $(minikube version --short 2>/dev/null || minikube version | head -1)"
    else
        error "minikube: NOT FOUND"
        all_ok=false
    fi

    if [ "${all_ok}" = true ]; then
        info "All tools installed successfully!"
    else
        error "Some tools failed to install. Please check the errors above."
        exit 1
    fi
}

# ---- Start minikube ----

start_minikube() {
    if minikube status --format='{{.Host}}' 2>/dev/null | grep -q 'Running'; then
        info "minikube is already running."
        return
    fi

    info "Starting minikube with podman driver..."
    minikube start --driver=podman --memory=4096 --cpus=2
    info "minikube started successfully."
}

# ---- Main ----

main() {
    info "======================================"
    info "  Environment Initialization Script"
    info "======================================"

    detect_os
    detect_pkg_manager

    info ""
    info "Installing tools..."
    install_java
    install_podman
    install_kubectl
    install_minikube

    info ""
    verify_installations

    info ""
    start_minikube

    info ""
    info "======================================"
    info "  Environment initialization complete!"
    info "======================================"
    info ""
    info "Next steps:"
    info "  ./deploy/scripts/env-start.sh dev       # Start dev environment"
    info "  ./deploy/scripts/env-start.sh staging   # Start staging environment"
    info "  ./deploy/scripts/env-start.sh prod      # Start prod environment"
}

main "$@"
