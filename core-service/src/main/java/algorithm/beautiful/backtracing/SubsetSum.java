package algorithm.beautiful.backtracing;


import java.util.ArrayList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 子集和问题 - 寻找数组中所有和为指定目标值的子集
 * 允许重复使用数组中的元素
 *
 * @author magicliang
 *
 *         date: 2025-08-26 10:41
 */
public class SubsetSum {

    /**
     * 寻找数组中所有和为指定目标值的子集（允许重复选择元素）
     *
     * @param nums 输入整数数组，允许包含重复元素
     * @param target 目标和值
     * @return 所有满足条件的子集列表，每个子集是一个整数列表
     * @example 输入: nums = [2,3,6,7], target = 7
     *         输出: [[7], [2,2,3]]
     * @note 该方法允许重复使用数组中的元素
     * @note 结果中可能包含重复的子集（当输入数组有重复元素时）
     */
    public List<List<Integer>> subsetSumINaive(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (null == nums || nums.length == 0) {
            return result;
        }

        // 在回溯框架里，有时候 states 和 states 是可以合并的
        List<Integer> states = new ArrayList<>();
        backtrack(nums, target, states, result);
        return result;
    }

    /**
     * 回溯算法核心实现
     *
     * @param nums 输入整数数组
     * @param target 剩余需要达到的目标和
     * @param states 当前已选择的元素列表
     * @param result 存储所有满足条件的子集结果
     * @algorithm 回溯算法：
     *         1. 如果当前和等于目标值，记录结果
     *         2. 如果当前和超过目标值，剪枝返回
     *         3. 遍历所有元素，允许重复选择
     *         4. 递归探索所有可能组合
     */
    private void backtrack(int[] nums, int target, List<Integer> states, List<List<Integer>> result) {
        if (sum(states) == target) {
            result.add(new ArrayList<>(states));
            return;
        } else if (sum(states) > target) {
            // 找不到也退出，停止回溯剪枝
            return;
        }

        // 因为可以重复取值，所以每一轮循环都不校验 used 的纵向使用，而允许全嵌套
        for (int num : nums) {
            // 选择即记录
            states.add(num);
            backtrack(nums, target, states, result);
            // 撤销选择即记录
            states.remove(states.size() - 1);
        }

    }

    /**
     * 计算整数列表的和
     *
     * @param choices 整数列表
     * @return 列表中所有整数的和，空列表返回0
     */
    private int sum(List<Integer> choices) {
        int sum = 0;
        if (null == choices) {
            return sum;
        }
        for (Integer choice : choices) {
            sum += choice;
        }
        return sum;
    }
}