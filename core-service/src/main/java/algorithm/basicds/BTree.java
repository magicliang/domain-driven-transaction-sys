package algorithm.basicds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        public int val;
        public Node left;
        public Node right;

        public Node() {
        }

        public Node(int val) {
            this.val = val;
        }

        public Node(int val, Node left, Node right) {
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

        private final Node root;

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
                // 这就是一个回溯动作。对中序遍历而言，回溯一次，就要继续探索右子树
                current = stack.pop();
                // 这个方法的神髓：访问当前节点（根节点），只有被 pop 出来的才处理
                result.add(current.val);

                // 阶段3：转向右子树
                // 每一个节点处理完以后都要考虑先处理自己的右节点，然后才到父节点，父节点已经在栈里了，最终能弹出来
                // 让右子树进入下一轮循环的处理流程
                // 注意：这里不是直接处理右子树，而是让右子树在下一轮循环中
                // 按照"左-根-右"的顺序被处理
                // current 被赋值表示继续探索
                current = current.right;
            }
        }
        return result;
    }

    /**
     * 后序遍历（左-右-根）的公共接口
     *
     * 后序遍历顺序：左子树 → 右子树 → 根节点
     * 对于二叉树，访问顺序为：递归遍历左子树 → 递归遍历右子树 → 访问当前节点
     *
     * 应用场景：
     * 1. 删除二叉树（需要先删除子节点再删除父节点）
     * 2. 计算目录大小（需要先计算子目录大小）
     * 3. 后缀表达式计算
     *
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - 递归栈深度，h为树高
     *
     * @param root 二叉树的根节点
     * @return 后序遍历结果列表
     */
    public List<Integer> postOrder(Node root) {
        List<Integer> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        postOrder(root.left, result);
        postOrder(root.right, result);
        result.add(root.val);
        return result;
    }

    /**
     * 后序遍历的递归辅助方法
     *
     * 递归终止条件：节点为null时返回
     * 递归过程：
     * 1. 递归遍历左子树
     * 2. 递归遍历右子树
     * 3. 访问当前节点（添加到结果列表）
     *
     * 关键理解：只有当一个节点的左右子树都处理完后，才处理该节点本身
     * 这就像"先处理完所有后代，再处理祖先"
     *
     * @param root 当前子树的根节点
     * @param result 结果收集器
     */
    private void postOrder(Node root, List<Integer> result) {
        if (root == null) {
            return;
        }
        // 左：递归遍历左子树
        postOrder(root.left, result);

        // 右：递归遍历右子树
        postOrder(root.right, result);

        // 根：访问当前节点（最后处理）
        result.add(root.val);
    }

    /**
     * 非递归后序遍历（左-右-根）实现
     * <p>
     * 算法思路：使用栈模拟递归的后序遍历过程
     * 后序遍历顺序：左子树 → 右子树 → 根节点
     * <p>
     * 核心思想：只有当一个节点的左右子树都访问过后，才能访问该节点
     * 这就像"先处理完所有后代，再处理祖先"
     * <p>
     * 实现要点：
     * 1. 使用LinkedList作为栈结构
     * 2. 需要记录上次访问的节点，用于判断右子树是否已经处理完
     * 3. 分两个阶段：先一直向左深入，然后决定是继续探索右子树还是回溯处理当前节点
     * <p>
     * 工作流程（通俗易懂版）：
     * 1. 从根节点开始，尽可能向左深入，把所有经过的节点压栈
     * 2. 当无法继续向左时，查看栈顶节点的右子树：
     * - 如果右子树存在且没处理过 → 转向右子树继续探索
     * - 如果右子树不存在或已处理过 → 弹出并访问当前节点
     * 3. 重复上述过程直到栈为空
     * <p>
     * 形象比喻：就像探险一样，先沿着左边的小路一直走到底，
     * 然后回头看看右边有没有没走过的路，有就走右边，没有就标记当前位置已探索
     * <p>
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - 栈的最大深度为树高h
     *
     * @param root 二叉树的根节点
     * @return 后序遍历结果列表
     */
    public List<Integer> postOrderNonRecursive(Node root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        // current：当前正在探索的节点
        // 关键状态说明：
        // - 当current != null时：表示还有新的节点需要探索，继续向下深入
        // - 当current == null时：表示已经走到尽头，需要考虑回溯问题
        //   此时需要从栈中弹出节点进行处理
        Node current = root;
        LinkedList<Node> stack = new LinkedList<>();

        // lastVisited：记录上次访问的节点，用于判断右子树是否已经处理
        // 这个变量的作用是：当我们回到一个节点时，能知道它的右子树是否已经处理过
        // 如果lastVisited == 当前节点的右子节点，说明右子树已处理完，可以处理当前节点
        // 这个指针的存在提示我们，默认我们要先处理自己的孩子再处理自己，但是等到我们二次访问自己的时候，可以通过这类设计表达孩子是不是被访问过了。
        Node lastVisited = null;

        // 探索和回溯如果没有做完，不退出循环
        // 循环条件：只要还有节点需要处理（current不为空）或者还有节点在栈中等待处理
        while (current != null || !stack.isEmpty()) {
            // 阶段1：尽可能向左深入，把所有经过的节点压栈
            // 这一步就像沿着左边的小路一直往前走
            if (current != null) {
                stack.push(current);
                current = current.left;
            } else {
                // 如果本轮探索完了，要考虑是继续探索右子树，还是直接处理当前根节点

                // peek 的用处这就体现出来了-当然，也可以不嫌麻烦先 push 再 pop
                Node top = stack.peek(); // 和其他栈一样，弹出来的都当作某个根节点
                final Node right = top.right;
                // 如果没有探索过右子树
                if (right != null && lastVisited != right) {
                    // 右子树存在且还没处理过 → 转向右子树继续探索
                    current = right;
                } else {
                    // 如果探索过右子树，则可以处理当前根了：没有右子树要处理了，可能右子树为空，也可能本身是右子树，也可能右子树被处理了
                    // 注意处理是一种消费，此时要弹出
                    top = stack.pop();
                    result.add(top.val);
                    lastVisited = top; // 这时候让这个根成为 lastVisited，如果这个根是某个右子树的根，可以帮助下一轮回溯继续向上，这个根可能是一颗左子树的根，也可能是
                    // 顶部的 root，也可能是右子树的根

                    // 和中序遍历不一样，中序遍历在这一步会继续给 current 赋 right，意味着要对右子树开始探索
                    // 但是这里处理完了以后就是要回溯的，也就意味着不再向下，要向上，所以这里不给 current 赋值，下一轮循环甚至以后的循环都是在这个大 else 里执行了
                }
            }
        }

        return result;
    }

    /**
     * 根据前序遍历和中序遍历构建二叉树
     *
     * 算法原理：
     * 前序遍历的第一个元素是根节点，中序遍历中根节点左侧是左子树，右侧是右子树
     * 通过递归构建左右子树
     *
     * @param preorder 前序遍历数组
     * @param inorder 中序遍历数组
     * @return 构建好的二叉树根节点
     *
     *         时间复杂度：O(n) - 每个节点只处理一次
     *         空间复杂度：O(n) - 哈希表存储和递归栈空间
     * @throws IllegalArgumentException 如果输入数组无效或包含重复值
     */
    public Node buildTree(int[] preorder, int[] inorder) {
        // 参数校验
        if (preorder == null || inorder == null || preorder.length != inorder.length) {
            return null;
        }

        if (preorder.length == 0) {
            return null;
        }

        // 检查是否有重复值
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (int val : inorder) {
            valueCount.put(val, valueCount.getOrDefault(val, 0) + 1);
            if (valueCount.get(val) > 1) {
                throw new IllegalArgumentException(
                        "buildTree方法不支持重复值，请使用buildTreeWithDuplicates方法处理包含重复值的情况");
            }
        }

        Map<Integer, Integer> inorderMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            // 倒序 v k 到 k v
            inorderMap.put(inorder[i], i);
        }

        // 从根节点开始处理，i为0意味着当前子树的根节点是preorder[0]，l和r的当前值意味着当前涉及的子树范围在全部 inorderMap 范围内
        Node root = dfsConstructTree(preorder, inorderMap, 0, 0, inorder.length - 1);
        return root;
    }

    /**
     * 深度优先搜索构建二叉树的递归辅助方法
     *
     * 算法原理：
     * 1. 前序遍历的第一个元素是当前子树的根节点
     * 2. 在中序遍历中找到该根节点的位置，可将中序遍历分为左子树和右子树
     * 3. 根据左子树的节点数，确定前序遍历中左右子树的起始位置
     *
     * 参数说明：
     *
     * @param preorder 前序遍历数组
     * @param inorderMap 中序遍历值到索引的映射，用于快速查找根节点位置
     * @param i 当前子树根节点在前序遍历中的索引
     * @param l 当前子树在中序遍历中的左边界（包含）
     * @param r 当前子树在中序遍历中的右边界（包含）
     * @return 构建好的子树根节点
     *
     *         时间复杂度：O(n) - 每个节点只处理一次
     *         空间复杂度：O(h) - 递归栈深度，h为树高
     *
     *         递归过程详解：
     *         1. 终止条件：当右边界小于左边界时，表示子树为空
     *         2. 创建根节点：使用前序遍历中索引i处的值
     *         3. 找到根节点在中序遍历中的位置m
     *         4. 计算左子树的大小：m - l
     *         5. 递归构建左子树：前序遍历从i+1开始，中序遍历区间[l, m-1]
     *         6. 递归构建右子树：前序遍历从i+1+(m-l)开始，中序遍历区间[m+1, r]
     */
    private Node dfsConstructTree(int[] preorder, Map<Integer, Integer> inorderMap, int i, int l, int r) {
        // 可以认为 inorderMap、l、r 都是为了在 inorder 的数据里移动而存在的，i是 preorder 的索引。
        // 所以前序遍历提供根节点，后序遍历提供子树

        // 子树区间为空时终止
        if (r - l < 0) {
            return null;
        }

        // 检查索引边界
        if (i < 0 || i >= preorder.length) {
            return null;
        }

        Node root = new Node(preorder[i]);
        Integer m = inorderMap.get(preorder[i]);

        // 如果找不到对应的索引，返回null
        if (m == null) {
            return null;
        }

        // 检查中序索引是否在有效范围内
        if (m < l || m > r) {
            return null;
        }

        // 计算左子树的大小
        int leftSize = m - l;

        // 检查右子树的起始索引是否越界
        int rightStartIndex = i + 1 + leftSize;
        if (rightStartIndex >= preorder.length && m + 1 <= r) {
            // 如果右子树应该存在但索引越界，返回null
            return null;
        }

        root.left = dfsConstructTree(preorder, inorderMap, i + 1, l, m - 1);
        root.right = dfsConstructTree(preorder, inorderMap, rightStartIndex, m + 1, r);

        return root;
    }

    /**
     * 根据前序遍历和中序遍历构建二叉树（支持重复值版本）
     *
     * 算法原理：
     * 前序遍历的第一个元素是根节点，中序遍历中根节点左侧是左子树，右侧是右子树
     * 由于允许重复值，不能使用HashMap进行快速查找，需要使用线性搜索
     *
     * 为什么需要这个方法：
     * 1. 原有的buildTree方法使用HashMap存储值到索引的映射，当存在重复值时，后面的值会覆盖前面的值
     * 2. 这会导致无法正确识别根节点在中序遍历中的位置，从而构建错误的树结构
     * 3. 线性搜索虽然时间复杂度较高(O(n²))，但能正确处理重复值的情况
     *
     * @param preorder 前序遍历数组
     * @param inorder 中序遍历数组
     * @return 构建好的二叉树根节点
     *
     *         时间复杂度：O(n²) - 每个节点需要线性搜索根节点位置
     *         空间复杂度：O(h) - 递归栈深度，h为树高
     */
    public Node buildTreeWithDuplicates(int[] preorder, int[] inorder) {
        // 参数校验
        if (preorder == null || inorder == null || preorder.length != inorder.length) {
            return null;
        }

        if (preorder.length == 0) {
            return null;
        }

        // 从根节点开始处理，i为0意味着当前子树的根节点是preorder[0]
        // l和r的当前值意味着当前涉及的子树范围在全部inorder数组范围内
        Node root = dfsConstructTreeWithDuplicates(preorder, inorder, 0, 0, inorder.length - 1);
        return root;
    }

    /**
     * 深度优先搜索构建二叉树的递归辅助方法（支持重复值版本）
     *
     * 算法原理：
     * 1. 前序遍历的第一个元素是当前子树的根节点
     * 2. 在中序遍历中找到该根节点的位置，将数组分为左子树和右子树
     * 3. 由于允许重复值，使用线性搜索找到根节点位置
     * 4. 根据左子树的节点数，确定前序遍历中左右子树的起始位置
     *
     * 关键理解：
     * - 对于普通二叉树（非二叉搜索树），重复值是允许的
     * - 前序遍历的第一个元素总是当前子树的根节点
     * - 在中序遍历中，根节点左侧是左子树，右侧是右子树
     * - 即使有重复值，这个分割逻辑仍然成立
     *
     * 参数说明：
     *
     * @param preorder 前序遍历数组
     * @param inorder 中序遍历数组
     * @param preIndex 当前子树根节点在前序遍历中的索引
     * @param inStart 当前子树在中序遍历中的左边界（包含）
     * @param inEnd 当前子树在中序遍历中的右边界（包含）
     * @return 构建好的子树根节点
     *
     *         时间复杂度：O(n²) - 每个节点需要线性搜索根节点位置
     *         空间复杂度：O(h) - 递归栈深度，h为树高
     */
    private Node dfsConstructTreeWithDuplicates(int[] preorder, int[] inorder, int preIndex, int inStart, int inEnd) {
        // 子树区间为空时终止
        if (inStart > inEnd) {
            return null;
        }

        // 当前子树的根节点值
        int rootValue = preorder[preIndex];
        Node root = new Node(rootValue);

        // 在中序遍历的当前区间内找到根节点的位置
        // 对于重复值，我们使用第一个匹配的位置作为根节点
        // 这是因为在构建树时，前序遍历的顺序决定了根节点的位置
        int rootIndex = -1;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == rootValue) {
                rootIndex = i;
                break;
            }
        }

        // 如果找不到根节点（理论上不应该发生，除非输入无效）
        if (rootIndex == -1) {
            throw new IllegalArgumentException(
                    "Invalid input: root value " + rootValue + " not found in inorder array");
        }

        // 计算左子树的大小
        int leftSize = rootIndex - inStart;

        // 递归构建左子树
        // 左子树的前序遍历起始位置：preIndex + 1
        // 左子树的中序遍历区间：[inStart, rootIndex - 1]
        root.left = dfsConstructTreeWithDuplicates(preorder, inorder, preIndex + 1, inStart, rootIndex - 1);

        // 递归构建右子树
        // 右子树的前序遍历起始位置：preIndex + 1 + leftSize
        // 右子树的中序遍历区间：[rootIndex + 1, inEnd]
        root.right = dfsConstructTreeWithDuplicates(preorder, inorder, preIndex + 1 + leftSize, rootIndex + 1, inEnd);

        return root;
    }
}