package algorithm.beautiful.backtracing;


import java.util.ArrayList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 尝试使用球盒模型来获取全部的排列
 *
 * 盒的视角是更好理解的，球的视角很容易搞出时间复杂度高的解决方案。
 *
 * 用「盒」的视角，即让索引取选元素的视角，可以用swap的方法把used数组给优化掉
 *
 * @author magicliang
 *
 *         date: 2025-08-07 11:38
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
     * 回溯算法生成全排列
     *
     * 时间复杂度：O(n! × n)
     * - 生成所有排列的数量为 n!
     * - 每个排列需要 O(n) 时间复制到结果列表中
     * - 总时间复杂度为 O(n! × n)
     *
     * 空间复杂度：O(n)
     * - 递归调用栈的最大深度为 n
     * - used 数组需要 O(n) 空间
     * - path 列表需要 O(n) 空间
     * - 不计算结果存储空间，辅助空间复杂度为 O(n)
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