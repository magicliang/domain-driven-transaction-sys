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

        // 测试用例5：包含重复元素
        int[] nums5 = {1, 1, 2};
        System.out.println("测试用例5 - 数组 [1,1,2] 的全排列: " + arrange(nums5));

        // 测试用例6：空数组
        int[] nums6 = {};
        System.out.println("测试用例6 - 空数组的全排列: " + arrange(nums6));
    }

    public static List<List<Integer>> arrange(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        if (nums.length == 1) {
            List<Integer> list = new ArrayList<>();
            list.add(nums[0]);
            result.add(list);
            return result;
        }

        Set<Integer> used = new HashSet<>();

        // brutal force 暴力穷举
        for (int i = 0; i < nums.length; i++) {
            // 遍历数组，产生当前的决策，然后把子问题丢到递归函数里面去，用他们的返回值来更新结果
            used.add(i);// 记录当前决策，如果有必要，把这个值传给下方，这里不保存实际值，保存索引页可以

            List<List<Integer>> subResult = arrange(nums, used);
            for (List<Integer> list : subResult) {
                // 把当前值作为结尾值放进来
                list.add(nums[i]);
            }

            // 在本层级更新结果，回溯法对每轮迭代都需要清理上一轮迭代的决策的，used 只在单一递归深度里增加
            used.remove(i);
            result.addAll(subResult);
        }
        return result;
    }

    private static List<List<Integer>> arrange(int[] nums, Set<Integer> used) {
        List<List<Integer>> result = new ArrayList<>();

        if (nums.length == 1) {
            List<Integer> list = new ArrayList<>();
            list.add(nums[0]);
            result.add(list);
            return result;
        }
        List<List<Integer>> finalResult = null;
        if (used.size() == nums.length - 1) {
            finalResult = new ArrayList<>();
            finalResult.add(new ArrayList<>());
        }

        // brutal force 暴力穷举
        for (int i = 0; i < nums.length; i++) {

            if (used.contains(i)) {
                continue;
            }

            if (finalResult != null) {
                finalResult.get(0).add(nums[i]);
                return finalResult;
            }

            // 遍历数组，产生当前的决策，然后把子问题丢到递归函数里面去，用他们的返回值来更新结果
            used.add(i);// 记录当前决策，如果有必要，把这个值传给下方

            List<List<Integer>> subResult = arrange(nums, used);
            for (List<Integer> list : subResult) {
                // 把当前值作为结尾值放进来
                list.add(nums[i]);
            }

            // 在本层级更新结果
            used.remove(i);
            result.addAll(subResult);
        }
        return result;
    }

}
