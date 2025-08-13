package algorithm.basicds;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基础二叉树
 *
 * @author magicliang
 *
 *         date: 2025-08-12 21:21
 */
public class BTree {

    public static class Node {

        int val;
        Node left;
        Node right;

        Node() {
        }

        Node(int val) {
            this.val = val;
        }

        Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        /**
         * 链式设置左子节点
         */
        public Node left(int val) {
            this.left = new Node(val);
            return this.left;
        }

        /**
         * 链式设置右子节点
         */
        public Node right(int val) {
            this.right = new Node(val);
            return this.right;
        }

        /**
         * 链式设置左子节点为已有节点
         */
        public Node left(Node node) {
            this.left = node;
            return this;
        }

        /**
         * 链式设置右子节点为已有节点
         */
        public Node right(Node node) {
            this.right = node;
            return this;
        }

        /**
         * 返回父节点，用于链式调用的回溯
         */
        public Node up() {
            // 注意：这里需要外部维护父节点引用，简化实现中返回this
            return this;
        }
    }

    /**
     * 二叉树构建器 - 提供可读性强的构造方式
     */
    public static class TreeBuilder {

        private Node root;

        public TreeBuilder(int rootVal) {
            this.root = new Node(rootVal);
        }

        public static TreeBuilder create(int rootVal) {
            return new TreeBuilder(rootVal);
        }

        /**
         * 设置左子树
         * 示例：left(2).left(4).right(5)
         */
        public TreeBuilder left(int val) {
            root.left = new Node(val);
            return this;
        }

        /**
         * 设置右子树
         * 示例：right(3).left(6).right(7)
         */
        public TreeBuilder right(int val) {
            root.right = new Node(val);
            return this;
        }

        /**
         * 设置左子树为已有节点
         */
        public TreeBuilder left(Node node) {
            root.left = node;
            return this;
        }

        /**
         * 设置右子树为已有节点
         */
        public TreeBuilder right(Node node) {
            root.right = node;
            return this;
        }

        /**
         * 设置左子树为子构建器
         */
        public TreeBuilder left(TreeBuilder leftBuilder) {
            root.left = leftBuilder.build();
            return this;
        }

        /**
         * 设置右子树为子构建器
         */
        public TreeBuilder right(TreeBuilder rightBuilder) {
            root.right = rightBuilder.build();
            return this;
        }

        /**
         * 获取根节点
         */
        public Node build() {
            return root;
        }

        /**
         * 链式设置左子节点并返回子构建器
         */
        public TreeBuilder leftChild(int val) {
            root.left = new Node(val);
            return new TreeBuilder(val);
        }

        /**
         * 链式设置右子节点并返回子构建器
         */
        public TreeBuilder rightChild(int val) {
            root.right = new Node(val);
            return new TreeBuilder(val);
        }
    }

    /**
     * 快速创建树的工具方法
     */
    public static TreeBuilder tree(int rootVal) {
        return TreeBuilder.create(rootVal);
    }

    public List<Integer> levelOrder(Node root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        LinkedList<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            final Node current = queue.poll();
            result.add(current.val);
            if (current.left != null) {
                queue.offer(current.left);
            }

            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }

    /**
     * 纯递归实现的层序遍历（不使用队列）
     *
     * 算法核心：分层递归下探模式
     * 1. 分层：将树按层分解（1,2,3...层）
     * 2. 递归下探：通过level参数控制下探深度
     * 3. 条件触发：level=1时收集节点值
     *
     * 工作流程：
     * - 先计算树的总层数（最长路径节点数）
     * - 从第1层到第totalLevel层，逐层处理
     * - 每层都从根节点开始递归，level参数控制下探深度
     *
     * 时间复杂度：O(n²) - 每层都从根开始遍历，存在重复计算
     * 空间复杂度：O(h) - 递归栈深度，h为树高
     */
    public List<Integer> levelOrderRecursive(Node root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        // 计算树的总层数（最长路径上的节点数量，1-based）
        // 例如：单节点树返回1，两层树返回2，以此类推
        int totalLevel = getTotalLevel(root);

        // 逐层处理：从第1层到第totalLevel层
        // 每层都从根节点开始递归，level参数控制下探深度
        for (int level = 1; level <= totalLevel; level++) {
            levelOrderLevel(result, root, level);
        }

        return result;
    }

    /**
     * 计算树的总层数（最长路径上的节点数量）
     *
     * 注意：这里计算的是节点高度（节点数），不是边高度（边数）
     * 定义：从当前节点到最远叶子节点的节点数量
     *
     * @param root 当前子树的根节点
     * @return 以root为根的子树的总层数
     */
    private int getTotalLevel(Node root) {
        if (root == null) { // 易错的点：忘记处理根/子节点为空的情况
            return 0;
        }

        // 叶子节点（无左右子节点）返回1，表示当前层
        if (root.left == null && root.right == null) {
            return 1;
        }

        // 非叶子节点：当前层(1) + 左右子树的最大层数
        return 1 + Math.max(getTotalLevel(root.left), getTotalLevel(root.right));
    }

    /**
     * 递归访问指定层的所有节点
     *
     * 分层递归下探模式的核心实现：
     * - level参数就像"下探指令"，告诉函数还需要向下深入多少层
     * - level=1：到达目标层，当前节点就是要收集的节点
     * - level>1：继续向下传递，level-1传给子节点
     *
     * 形象理解：就像逐层剥洋葱，level就是"剥几层才能看到核心"
     * 每次递归调用level减1，模拟逐层深入的过程
     *
     * @param result 结果收集器
     * @param root 当前子树的根节点
     * @param level 还需要下探的层数（1表示当前层就是要找的层）
     */
    private void levelOrderLevel(List<Integer> result, Node root, int level) {
        if (root == null) {
            return;
        }

        // 分层递归下探的核心逻辑：
        // 当level=1时，表示已经"下探"到了目标层，当前节点就是要收集的节点
        // 当level>1时，需要继续向下传递，让子节点在level-1时处理

        if (level == 1) {
            // 到达目标层，收集当前节点的值
            result.add(root.val);
            return;
        }

        // 继续向下展开：将level-1传给左右子树
        // 就像把"下探指令"传递给下一层工人
        levelOrderLevel(result, root.left, level - 1);
        levelOrderLevel(result, root.right, level - 1);
    }


    /**
     * 前序遍历（根左右）的公共接口
     *
     * 前序遍历顺序：根节点 → 左子树 → 右子树
     * 对于二叉树，访问顺序为：当前节点 → 递归遍历左子树 → 递归遍历右子树
     *
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - 递归栈深度，h为树高
     *
     * @param root 二叉树的根节点
     * @return 前序遍历结果列表
     */
    public List<Integer> preOrder(Node root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        // 使用辅助方法进行递归遍历
        preOrder(result, root);
        return result;
    }

    /**
     * 前序遍历的递归辅助方法
     *
     * 递归终止条件：节点为null时返回
     * 递归过程：
     * 1. 访问当前节点（添加到结果列表）
     * 2. 递归遍历左子树
     * 3. 递归遍历右子树
     *
     * @param result 结果收集器
     * @param root 当前子树的根节点
     */
    private void preOrder(List<Integer> result, Node root) {
        if (null == root) {
            return;
        }

        // 根：先访问当前节点
        result.add(root.val);

        // 左：递归遍历左子树
        preOrder(result, root.left);

        // 右：递归遍历右子树
        preOrder(result, root.right);
    }


    /**
     * 非递归前序遍历（根-左-右）实现
     * <p>
     * 算法思路：使用栈模拟递归调用栈，实现深度优先遍历
     * 前序遍历顺序：根节点 → 左子树 → 右子树
     * <p>
     * 实现要点：
     * 1. 使用LinkedList作为栈结构（后进先出）
     * 2. 关键点：由于栈是后进先出，需要先压右子节点，再压左子节点
     * 这样才能保证左子节点先被处理，符合根-左-右的顺序
     * 3. 从根节点开始，每次处理栈顶节点，然后将其右、左子节点压栈
     * <p>
     * 工作流程：
     * 1. 初始化栈，压入根节点
     * 2. 循环直到栈为空：
     * - 弹出栈顶节点并访问
     * - 先压右子节点（后处理）
     * - 再压左子节点（先处理）
     * <p>
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - 栈的最大深度为树高h
     *
     * @param root 二叉树的根节点
     * @return 前序遍历结果列表
     */
    public List<Integer> preOrderNonRecursive(Node root) {

        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        // 使用栈实现前序遍历（根-左-右）
        // 关键点：由于栈是后进先出，需要先压右子节点，再压左子节点
        // 这样才能保证左子节点先被处理
        LinkedList<Node> stack = new LinkedList<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            final Node current = stack.pop();
            // 其实这步不一定需要存在，因为不会push null进栈里面
            if (current == null) {
                break;
            }
            result.add(current.val);
            // 注意顺序：先右后左，这样左子节点会先被处理
            if (current.right != null) {
                stack.push(current.right);
            }
            if (current.left != null) {
                stack.push(current.left);
            }
        }

        return result;
    }

    /**
     * 中序遍历（左根右）的公共接口
     *
     * 中序遍历顺序：左子树 → 根节点 → 右子树
     * 对于二叉搜索树，中序遍历的结果是有序的（升序排列）
     *
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - 递归栈深度，h为树高
     *
     * @param root 二叉树的根节点
     * @return 中序遍历结果列表
     */
    public List<Integer> midOrder(Node root) {
        List<Integer> result = new ArrayList<>();
        if (null == root) {
            return result;
        }
        midOrder(root.left, result);
        result.add(root.val);
        midOrder(root.right, result);

        return result;
    }

    /**
     * 中序遍历的递归辅助方法
     *
     * 递归终止条件：节点为null时返回
     * 递归过程：
     * 1. 递归遍历左子树
     * 2. 访问当前节点（添加到结果列表）
     * 3. 递归遍历右子树
     *
     * 对于二叉搜索树，这个过程会按照从小到大的顺序访问所有节点
     *
     * @param root 当前子树的根节点
     * @param result 结果收集器
     */
    private void midOrder(Node root, List<Integer> result) {
        if (null == root) {
            return;
        }
        // 左：递归遍历左子树
        midOrder(root.left, result);

        // 根：访问当前节点
        result.add(root.val);

        // 右：递归遍历右子树
        midOrder(root.right, result);
    }

    /**
     * 非递归中序遍历（左-根-右）实现
     * <p>
     * 算法思路：使用栈模拟递归调用栈，实现深度优先遍历
     * 中序遍历顺序：左子树 → 根节点 → 右子树
     * <p>
     * 实现要点：
     * 1. 使用LinkedList作为栈结构（后进先出）
     * 2. 关键点：需要先将所有左子节点压栈，直到最左叶子节点
     * 3. 处理顺序：弹出栈顶节点（此时该节点的左子树已处理完）→ 访问该节点 → 处理右子树
     * 4. 使用current指针跟踪当前处理的节点，避免重复压栈
     * <p>
     * 工作流程：
     * 1. 初始化栈和current指针（指向根节点）
     * 2. 循环直到栈为空且current为null：
     * - 如果current不为null：压栈并继续向左深入（current = current.left）
     * - 如果current为null：弹出栈顶节点并访问，然后转向右子树（current = current.right）
     * <p>
     * 形象理解：就像沿着左边界一直往下走，走到尽头后回溯，每回溯一步就处理一个节点，然后转向右子树
     * <p>
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - 栈的最大深度为树高h，最坏情况下为O(n)（树退化为链表）
     *
     * @param root 二叉树的根节点
     * @return 中序遍历结果列表
     */
    public List<Integer> midOrderNonRecursive(Node root) {
        List<Integer> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        LinkedList<Node> stack = new LinkedList<>();
        Node current = root;

        // 核心循环：模拟递归的中序遍历过程
        // 每个节点都是某棵子树的根，处理完左子树后处理根节点，再处理右子树
        while (current != null || !stack.isEmpty()) {
            // 阶段1：沿着左子树一直向下，将所有左子节点压栈
            // 这一步模拟了递归中的"一直向左深入"过程
            if (current != null) {
                stack.push(current);
                current = current.left;
            } else {
                // 阶段2：左子树已处理完，开始回溯
                // 弹出栈顶节点（此时该节点的左子树已全部处理完）
                current = stack.pop();
                // 这个方法的神髓：访问当前节点（根节点），只有被 pop 出来的才处理
                result.add(current.val);

                // 阶段3：转向右子树
                // 让右子树进入下一轮循环的处理流程
                // 注意：这里不是直接处理右子树，而是让右子树在下一轮循环中
                // 按照"左-根-右"的顺序被处理
                current = current.right;
            }
        }
        return result;
    }

}
