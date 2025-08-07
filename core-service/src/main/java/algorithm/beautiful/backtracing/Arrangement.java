package algorithm.beautiful.backtracing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // 测试用例5：包含重复元素，如果要去除重复元素，要有一个横跨所有函数的
        int[] nums5 = {1, 1, 2};
        System.out.println("测试用例5 - 数组 [1,1,2] 的全排列: " + arrange(nums5));

        System.out.println("测试用例5 - 数组 [1,1,2] 的 permutationsII 全排列: " + permutationsII(nums5));

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
        if (current.size() == nums.length) {  // 这是一个剪枝
            result.add(new ArrayList<>(current));
            return;
        }

        // 选择列表：遍历所有未被使用的元素

        // 这个列表可以过滤掉本轮用过的元素，如果全局有n个元素（比如2个1），那么剪枝过后，结果数量会/n。比如 [1,1,2] 原本输出6个排列，现在只剩下3个
        Set<Integer> duplicated = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            // 这是一个全路径剪枝和本轮剪枝结合的做法
            if (used[i] // 重复元素剪枝
                    || duplicated.contains(nums[i]) // 相等元素剪枝
            ) {
                continue;
            }

            // 做出选择
            used[i] = true;
            current.add(nums[i]);

            // 在同层里用duplicated去重，这样这一层会少一个循环-这样剪枝意味着嵌套的层数变少

            duplicated.add(nums[i]);
            // 递归进入下一层
            backtrack(nums, used, current, result);

            // 撤销选择（回溯），易错的点：used 和 current 两个要一起撤销。current意味着当前的数据要穿梭到下游的数据，当本轮结束后，current的路径点上是不应该包含本 nums[i] 了
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }

    /* 回溯算法：全排列 II */
    static void backtrack(List<Integer> state, int[] choices, boolean[] selected, List<List<Integer>> res) {
        // 当状态长度等于元素数量时，记录解
        if (state.size() == choices.length) {
            res.add(new ArrayList<Integer>(state));
            return;
        }
        // 遍历所有选择
        Set<Integer> duplicated = new HashSet<Integer>();
        for (int i = 0; i < choices.length; i++) {
            int choice = choices[i];
            // 剪枝：不允许重复选择元素 且 不允许重复选择相等元素
            if (!selected[i] && !duplicated.contains(choice)) {
                // 尝试：做出选择，更新状态
                duplicated.add(choice); // 记录选择过的元素值
                selected[i] = true;
                state.add(choice);
                // 进行下一轮选择
                backtrack(state, choices, selected, res);
                // 回退：撤销选择，恢复到之前的状态
                selected[i] = false;
                state.remove(state.size() - 1);
            }
        }
    }

    /* 全排列 II */
    static List<List<Integer>> permutationsII(int[] nums) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        backtrack(new ArrayList<Integer>(), nums, new boolean[nums.length], res);
        return res;
    }
}
