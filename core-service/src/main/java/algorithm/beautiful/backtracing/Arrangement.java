package algorithm.beautiful.backtracing;

import java.util.ArrayList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 排列是第一个回溯法可以拿来解决的第一个经典问题
 *
 * @author magicliang
 *
 *         date: 2025-08-06 21:00
 */
public class Arrangement {

    public static void main(String[] args) {
        // 测试用例1：基本测试
        int[] nums1 = {1, 2, 3};
        System.out.println("测试用例1 - 数组 [1,2,3] 的全排列: " + arrange(nums1));

        // 测试用例2：单个元素
        int[] nums2 = {5};
        System.out.println("测试用例2 - 数组 [5] 的全排列: " + arrange(nums2));

        // 测试用例3：两个元素
        int[] nums3 = {1, 2};
        System.out.println("测试用例3 - 数组 [1,2] 的全排列: " + arrange(nums3));

        // 测试用例4：四个元素
        int[] nums4 = {1, 2, 3, 4};
        List<List<Integer>> result4 = arrange(nums4);
        System.out.println("测试用例4 - 数组 [1,2,3,4] 的全排列数量: " + result4.size());
        System.out.println("测试用例4 - 前5个排列: " + result4.subList(0, Math.min(5, result4.size())));

        // 测试用例5：包含重复元素
        int[] nums5 = {1, 1, 2};
        System.out.println("测试用例5 - 数组 [1,1,2] 的全排列: " + arrange(nums5));

        // 测试用例6：空数组
        int[] nums6 = {};
        System.out.println("测试用例6 - 空数组的全排列: " + arrange(nums6));
    }

    public static List<List<Integer>> arrange(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }

        boolean[] used = new boolean[nums.length];
        List<Integer> current = new ArrayList<>();

        // 好的递归 backtrack 不依赖于返回值，而依赖于全局变量或者传递变量
        // 竞赛的方式是在第一层初始化全部数据结构，但真正使用数据结构在第二层方法
        backtrack(nums, used, current, result);
        return result;
    }

    private static void backtrack(int[] nums, boolean[] used, List<Integer> current, List<List<Integer>> result) {
        // 终止条件：当前排列长度等于数组长度
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }

        // 选择列表：遍历所有未被使用的元素
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }

            // 做出选择
            used[i] = true;
            current.add(nums[i]);

            // 递归进入下一层
            backtrack(nums, used, current, result);

            // 撤销选择（回溯），易错的点：used 和 current 两个要一起撤销
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }
}
