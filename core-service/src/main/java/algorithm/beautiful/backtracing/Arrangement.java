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
        int[] nums1 = {1, 2, 3};
        System.out.println("全排列: " + arrange(nums1));
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
                list.add(i);
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
                list.add(i);
            }

            // 在本层级更新结果
            used.remove(i);
            result.addAll(subResult);
        }
        return result;
    }

}
