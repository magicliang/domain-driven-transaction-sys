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
        backtrack(nums, target, states, 0, result);
        return result;
    }

    /**
     * 回溯算法核心实现
     *
     * @param nums 输入整数数组
     * @param target 剩余需要达到的目标和
     * @param states 当前已选择的元素列表
     * @param total 当前已选择元素的总和
     * @param result 存储所有满足条件的子集结果
     * @algorithm 回溯算法：
     *         1. 如果当前和等于目标值，记录结果
     *         2. 如果当前和超过目标值，剪枝返回
     *         3. 遍历所有元素，允许重复选择
     *         4. 递归探索所有可能组合
     */
    void backtrack(int[] nums, int target, List<Integer> states, int total, List<List<Integer>> result) {

        // 只处理等于的情况
        if (total == target) {
            result.add(new ArrayList<>(states));
            return;
        }

        // 添加深度限制防止无限递归
//        if (states.size() > 100) {  // 或其他合理限制
//            return;
//        }

        // 因为可以重复取值，所以每一轮循环都不校验 used 的纵向使用，而允许全嵌套
        for (int num : nums) {
            // 在进入选择前剪枝，和在下一个递归里剪枝的结果是一样的
            if (total + num > target) {
                continue;
            }
            // 选择即记录
            total += num;
            states.add(num);
            backtrack(nums, target, states, total, result);
            // 撤销选择即记录
            states.remove(states.size() - 1);
            total -= num;
        }

    }

}