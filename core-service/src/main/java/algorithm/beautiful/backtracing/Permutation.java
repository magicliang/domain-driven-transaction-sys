package algorithm.beautiful.backtracing;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 尝试使用球盒模型来获取全部的排列
 *
 * 盒的视角是更好理解的，球的视角很容易搞出时间复杂度高的解决方案。
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

    public static void backtracing(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        // 在找到特定的组合以后，在此处剪枝
        if (path.size() == nums.length - 1) { // 提前返回是剪枝，“快排好”是一种翻煎饼式的剪枝
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

            // 把本空间的选择从路径中移除
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

        // 尽量基于原地操作的swap排序法，基于子数组区间的解法
        backtracing(nums, result, 0);

        return result;
    }

    /**
     * 通过把每一个当前主数组的每一个元素交换到开头，制造一个子空间，然后仍然让原数组可以原地工作
     *
     * @param nums 原始数组
     * @param result 结果集
     * @param layer 最重要的参数，当前要决策的层数
     */
    public static void backtracing(int[] nums, List<List<Integer>> result, int layer) {
        if (layer == nums.length - 1) {
            result.add(Arrays.stream(nums).boxed().collect(Collectors.toList()));
            return;
        }

        // 比如如果是0，则本层就是想办法把所有这个数组的子区间和index0进行交换
        for (int i = layer; i < nums.length; i++) { // 注意  i = layer 是必须的，如果 i 是 layer + 1，则循环就会少一个乘数，类似阶乘少了最高阶
            swap(nums, layer, i);
            backtracing(nums, result, layer + 1);// i == layer，而不是 layer+1，意味着本layer 作为起点的 tracing 也是需要加入递归的
            swap(nums, i, layer);
        }

    }

    private static void swap(int[] nums, int from, int to) {
        if (from == to) {
            return;
        }
        int temp = nums[from];
        nums[from] = nums[to];
        nums[to] = temp;
    }
}