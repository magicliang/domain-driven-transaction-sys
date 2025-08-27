package algorithm.beautiful.backtracing;


import java.util.ArrayList;
import java.util.List;

/**
 * 全排列问题的多种回溯算法实现
 * <p>
 * 这是一个经典的回溯算法问题，展示了如何使用不同的策略生成数组的所有排列。
 * 问题描述：给定一个不含重复数字的数组 nums，返回其所有可能的全排列。
 * </p>
 *
 * <p>
 * <strong>球盒模型理论基础：</strong><br>
 * 全排列问题可以用球盒模型来理解：
 * <ul>
 *   <li><strong>球的视角：</strong>每个数字是一个球，考虑每个球放在哪个位置</li>
 *   <li><strong>盒的视角：</strong>每个位置是一个盒子，考虑每个盒子放哪个球</li>
 * </ul>
 * 盒的视角通常更直观，也更容易实现高效的算法。
 * </p>
 *
 * <p>
 * <strong>算法策略对比：</strong>
 * <table border="1">
 *   <tr>
 *     <th>实现方式</th>
 *     <th>核心思想</th>
 *     <th>空间复杂度</th>
 *     <th>优缺点</th>
 *   </tr>
 *   <tr>
 *     <td>Permutation1</td>
 *     <td>使用used数组标记已选元素</td>
 *     <td>O(n)</td>
 *     <td>逻辑清晰，易于理解，但需要额外空间</td>
 *   </tr>
 *   <tr>
 *     <td>Permutation2</td>
 *     <td>使用交换优化，无需used数组</td>
 *     <td>O(1)</td>
 *     <td>空间最优，但逻辑稍复杂</td>
 *   </tr>
 * </table>
 * </p>
 *
 * <p>
 * <strong>回溯算法核心要素：</strong>
 * <ol>
 *   <li><strong>选择列表：</strong>当前可以做的选择</li>
 *   <li><strong>路径：</strong>已经做出的选择</li>
 *   <li><strong>结束条件：</strong>到达决策树底层，无法再做选择</li>
 *   <li><strong>做选择：</strong>将选择加入路径</li>
 *   <li><strong>撤销选择：</strong>从路径中移除选择</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>时间复杂度分析：</strong><br>
 * 对于长度为n的数组：
 * <ul>
 *   <li>总排列数：n!</li>
 *   <li>每个排列的生成时间：O(n)</li>
 *   <li>总时间复杂度：O(n! × n)</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>数学背景：</strong><br>
 * 全排列是组合数学中的基本概念：
 * <ul>
 *   <li>n个不同元素的全排列数为 n! = n × (n-1) × ... × 2 × 1</li>
 *   <li>这是因为第一个位置有n种选择，第二个位置有(n-1)种选择，以此类推</li>
 *   <li>全排列是一种特殊的组合，考虑元素的顺序</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>实际应用场景：</strong>
 * <ul>
 *   <li>密码破解：尝试所有可能的字符组合</li>
 *   <li>任务调度：安排任务的执行顺序</li>
 *   <li>路径规划：遍历所有可能的访问顺序</li>
 *   <li>游戏开发：生成随机的关卡布局</li>
 *   <li>数据分析：排列组合分析</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>扩展问题：</strong>
 * <ul>
 *   <li>带重复元素的全排列：需要去重处理</li>
 *   <li>下一个排列：字典序的下一个排列</li>
 *   <li>第k个排列：直接计算第k个排列而不生成所有排列</li>
 *   <li>部分排列：从n个元素中选择m个进行排列</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>性能优化建议：</strong>
 * <ul>
 *   <li>对于小规模数据（n≤8），两种方法性能相近</li>
 *   <li>对于中等规模数据（8<n≤10），推荐使用Permutation2（交换法）</li>
 *   <li>对于大规模数据（n>10），考虑使用迭代方法或剪枝优化</li>
 * </ul>
 * </p>
 *
 * @author magicliang
 * @version 1.0
 * @since 2025-08-07
 * @see <a href="https://leetcode-cn.com/problems/permutations/">LeetCode 46. 全排列</a>
 * @see <a href="https://en.wikipedia.org/wiki/Permutation">Permutation - Wikipedia</a>
 * @see <a href="https://en.wikipedia.org/wiki/Backtracking">Backtracking - Wikipedia</a>
 */
public class Permutation {

    public static void main(String[] args) {
//        testCase1();
        testCase2();
    }

    private static void testCase1() {
        // 测试用例1：基本测试
        int[] nums1 = {1, 2, 3};
        System.out.println("测试用例1 - 数组 [1,2,3] 的全排列: " + Permutation1.permutate1(nums1));

        // 测试用例2：单个元素
        int[] nums2 = {5};
        System.out.println("测试用例2 - 数组 [5] 的全排列: " + Permutation1.permutate1((nums2)));

        // 测试用例3：两个元素
        int[] nums3 = {1, 2};
        System.out.println("测试用例3 - 数组 [1,2] 的全排列: " + Permutation1.permutate1((nums3)));

        // 测试用例4：四个元素
        int[] nums4 = {1, 2, 3, 4};
        List<List<Integer>> result4 = Permutation1.permutate1((nums4));
        System.out.println("测试用例4 - 数组 [1,2,3,4] 的全排列数量: " + result4.size());
        System.out.println("测试用例4 - 前5个排列: " + result4.subList(0, Math.min(5, result4.size())));

        // 测试用例5：包含重复元素
        int[] nums5 = {1, 1, 2};
        System.out.println("测试用例5 - 数组 [1,1,2] 的全排列: " + Permutation1.permutate1((nums5)));

        // 测试用例6：空数组
        int[] nums6 = {};
        System.out.println("测试用例6 - 空数组的全排列: " + Permutation1.permutate1((nums6)));
    }


    private static void testCase2() {
        // 测试用例1：基本测试
        int[] nums1 = {1, 2, 3};
        System.out.println("测试用例1 - 数组 [1,2,3] 的全排列: " + Permutation2.permutate2(nums1));

        // 测试用例2：单个元素
        int[] nums2 = {5};
        System.out.println("测试用例2 - 数组 [5] 的全排列: " + Permutation2.permutate2((nums2)));

        // 测试用例3：两个元素
        int[] nums3 = {1, 2};
        System.out.println("测试用例3 - 数组 [1,2] 的全排列: " + Permutation2.permutate2((nums3)));

        // 测试用例4：四个元素
        int[] nums4 = {1, 2, 3, 4};
        List<List<Integer>> result4 = Permutation2.permutate2((nums4));
        System.out.println("测试用例4 - 数组 [1,2,3,4] 的全排列数量: " + result4.size());
        System.out.println("测试用例4 - 前5个排列: " + result4.subList(0, Math.min(5, result4.size())));

        // 测试用例5：包含重复元素
        int[] nums5 = {1, 1, 2};
        System.out.println("测试用例5 - 数组 [1,1,2] 的全排列: " + Permutation2.permutate2((nums5)));

        // 测试用例6：空数组
        int[] nums6 = {};
        System.out.println("测试用例6 - 空数组的全排列: " + Permutation2.permutate2((nums6)));
    }
}

class Permutation1 {

    public static List<List<Integer>> permutate1(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        if (null == nums || nums.length == 0) {
            return result;
        }

        // 要有一个全局变量记录已经做的决策-对于不可重复选的选择。这个 used 和搜索类算法的层数的接近的
        boolean[] used = new boolean[nums.length];

        // 要有一个全局变量，延伸已经做出的选择
        List<Integer> path = new ArrayList<>();

        // 如果学翻饼问题，这里可以把 used, path, result 放到一个 init 方法里初始化为本实例的成员，现在为了和lc一致，继续使用方法参数传递的方法来解决这个问题
        backtracing(nums, used, path, result);

        return result;
    }

    /**
     * 使用标记数组的回溯算法生成全排列（核心递归方法）
     * <p>
     * 这是经典的回溯算法实现，使用boolean数组标记已选择的元素。
     * 该方法体现了回溯算法的标准模板：选择→递归→撤销选择。
     * </p>
     *
     * <p>
     * <strong>算法流程：</strong>
     * <ol>
     *   <li><strong>终止条件：</strong>当路径长度等于数组长度时，找到一个完整排列</li>
     *   <li><strong>选择遍历：</strong>遍历所有可能的选择（未使用的元素）</li>
     *   <li><strong>做选择：</strong>将元素加入路径，标记为已使用</li>
     *   <li><strong>递归调用：</strong>继续构建下一个位置的排列</li>
     *   <li><strong>撤销选择：</strong>回溯时移除元素，取消标记</li>
     * </ol>
     * </p>
     *
     * <p>
     * <strong>决策树分析：</strong><br>
     * 以数组[1,2,3]为例，决策树结构如下：
     * <pre>
     *                    []
     *           /        |        \
     *         [1]       [2]       [3]
     *        /  \      /   \     /   \
     *    [1,2] [1,3] [2,1] [2,3] [3,1] [3,2]
     *      |     |     |     |     |     |
     *  [1,2,3][1,3,2][2,1,3][2,3,1][3,1,2][3,2,1]
     * </pre>
     * 每个节点代表当前的路径状态，叶子节点是完整的排列。
     * </p>
     *
     * <p>
     * <strong>复杂度详细分析：</strong>
     * <ul>
     *   <li><strong>时间复杂度：O(n! × n)</strong>
     *     <ul>
     *       <li>决策树的叶子节点数量：n!（所有排列的数量）</li>
     *       <li>每个叶子节点需要O(n)时间复制路径到结果中</li>
     *       <li>内部节点的处理时间相对较少，主要开销在叶子节点</li>
     *       <li>总时间复杂度：O(n!) × O(n) = O(n! × n)</li>
     *     </ul>
     *   </li>
     *   <li><strong>空间复杂度：O(n)</strong>
     *     <ul>
     *       <li>递归调用栈深度：O(n)（最多递归n层）</li>
     *       <li>used数组空间：O(n)</li>
     *       <li>path列表空间：O(n)（最多存储n个元素）</li>
     *       <li>不计算输出结果的存储空间</li>
     *     </ul>
     *   </li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>算法正确性证明：</strong><br>
     * <strong>归纳法证明：</strong>
     * <ul>
     *   <li><strong>基础情况：</strong>当n=1时，只有一个元素，算法正确返回[nums[0]]</li>
     *   <li><strong>归纳假设：</strong>假设对于长度为k的数组，算法能正确生成所有k!个排列</li>
     *   <li><strong>归纳步骤：</strong>对于长度为k+1的数组：
     *     <ul>
     *       <li>算法会依次选择每个元素作为第一个位置</li>
     *       <li>对于每个选择，剩余k个元素的排列由归纳假设保证正确</li>
     *       <li>因此总共生成(k+1)×k! = (k+1)!个排列，且都是正确的</li>
     *     </ul>
     *   </li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>关键实现细节：</strong>
     * <ul>
     *   <li><strong>深拷贝必要性：</strong>必须创建path的副本加入结果，因为path会被后续递归修改</li>
     *   <li><strong>状态恢复：</strong>每次递归返回后必须恢复used数组和path的状态</li>
     *   <li><strong>剪枝优化：</strong>通过used数组避免选择已使用的元素，减少无效递归</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>调试技巧：</strong>
     * <ul>
     *   <li>可以在递归入口和出口添加日志，观察决策树的遍历过程</li>
     *   <li>检查used数组的状态变化，确保正确的标记和恢复</li>
     *   <li>验证path的长度变化，确保正确的添加和移除</li>
     * </ul>
     * </p>
     *
     * @param nums 输入数组，包含待排列的元素
     * @param used 标记数组，used[i]为true表示nums[i]已被使用
     * @param path 当前构建的排列路径
     * @param result 存储所有排列结果的列表
     *
     * @implNote 该方法修改传入的used数组和path列表，但在递归返回前会恢复原状态
     * @implSpec 使用深度优先搜索遍历所有可能的排列组合
     * @see <a href="https://en.wikipedia.org/wiki/Depth-first_search">Depth-first search</a>
     * @since 1.0
     */
    public static void backtracing(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        // 在找到特定的组合以后，在此处剪枝
        if (path.size() == nums.length) { // 提前返回是剪枝，"快排好"是一种翻煎饼式的剪枝
            // 这个复制深拷贝是为了上方的撤销决策的时候能够不影响输出数据，是必要的空间复杂度
            List<Integer> temp = new ArrayList<>(path);
            result.add(temp);
            return;
        }

        // 如果不剪枝，则把当前的探索空间取出来遍历，current意味着当前探索空间是底基层

        // 从球盒模型出发，假设 current 是当前盒子
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                // 本盒子被用过了
                continue;
            }
            // 把本空间的选择加入路径
            path.add(nums[i]);
            used[i] = true;
            backtracing(nums, used, path, result);
            // 撤销决策
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
}

class Permutation2 {

    public static List<List<Integer>> permutate2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        if (null == nums || nums.length == 0) {
            return result;
        }

        // 要有一个全局变量，延伸已经做出的选择
        List<Integer> path = new ArrayList<>();

        // 如果学翻饼问题，这里可以把 used, path, result 放到一个 init 方法里初始化为本实例的成员，现在为了和lc一致，继续使用方法参数传递的方法来解决这个问题
        backtracing(nums, 0, path, result);

        return result;
    }

    public static void backtracing(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        // 在找到特定的组合以后，在此处剪枝
        if (path.size() == nums.length) { // 提前返回是剪枝，"快排好"是一种翻煎饼式的剪枝
            // 这个复制深拷贝是为了上方的撤销决策的时候能够不影响输出数据，是必要的空间复杂度
            List<Integer> temp = new ArrayList<>(path);
            result.add(temp);
            return;
        }

        // 如果不剪枝，则把当前的探索空间取出来遍历，current意味着当前探索空间是底基层

        // 从球盒模型出发，假设 current 是当前盒子
        for (int i = start; i < nums.length; i++) {
            // 把本空间的选择加入路径
            path.add(nums[i]);
            // 交换元素，将当前元素移到已处理部分的末尾
            swap(nums, start, i);
            backtracing(nums, start + 1, path, result);
            // 撤销决策
            path.remove(path.size() - 1);
            // 恢复原始顺序
            swap(nums, start, i);
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}