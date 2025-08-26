package algorithm.beautiful.backtracing;


import java.util.ArrayList;
import java.util.Arrays;
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

        // 验证输入，防止无限递归
        for (int num : nums) {
            if (num <= 0) {
                throw new IllegalArgumentException("输入数组必须只包含正整数，避免无限递归");
            }
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

    /**
     * 寻找数组中所有和为指定目标值的子集（不重复使用元素，避免重复组合）
     *
     * @param nums 输入整数数组，可能包含重复元素
     * @param target 目标和值
     * @return 所有满足条件的子集列表，每个子集是一个整数列表
     * @example 输入: nums = [2,3,6,7], target = 7
     *         输出: [[2,5], [7]]  // 假设数组包含5
     * @note 该方法不重复使用数组中的元素（每个位置的元素最多使用一次）
     * @note 通过排序和跳过重复元素来避免产生重复的组合
     * @note 时间复杂度: O(2^n)，其中n是数组长度
     * @note 空间复杂度: O(n)用于递归栈空间
     */
    public List<List<Integer>> subsetSumNoDuplicateCombination(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (null == nums || nums.length == 0) {
            return result;
        }

        // 直接在开头避免负数的无限递归
        for (int num : nums) {
            if (num <= 0) {
                throw new IllegalArgumentException("输入数组必须只包含正整数，避免无限递归");
            }
        }

        // 要去重要保持单调有序
        Arrays.sort(nums);
        backtrackNoDuplicateCombination(nums, target, new ArrayList<>(), 0, result);

        return result;
    }

    /**
     * 不重复使用元素的子集和回溯算法
     *
     * @param nums 已排序的输入数组
     * @param target 剩余目标和
     * @param states 当前已选择的元素
     * @param start 起始搜索位置，避免重复使用前面的元素
     * @param result 存储结果的列表
     * @algorithm 回溯算法：
     *         1. 当target为0时，找到有效解
     *         2. 从start位置开始遍历，避免重复使用元素
     *         3. 跳过重复元素以避免重复解
     *         4. 递归搜索后续元素
     */
    private void backtrackNoDuplicateCombination(int[] nums, int target, List<Integer> states, int start,
            List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(states));
            // 结尾剪枝
            return;
        }

        final int length = nums.length;
        // 从start开始，确保不重复使用前面的元素
        for (int i = start; i < length; i++) {
            // 跳过重复元素，避免重复解
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }

            // 作负值剪枝
            if (target - nums[i] < 0) {
                continue;
            }

            states.add(nums[i]);

            // 传入i+1确保不重复使用当前元素
            backtrackNoDuplicateCombination(nums, target - nums[i], states, i + 1, result);

            states.remove(states.size() - 1);
        }
    }

    public List<List<Integer>> subsetSumNoDuplicateElements(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (null == nums || nums.length == 0) {
            return result;
        }

        // 直接在开头避免负数的无限递归
        for (int num : nums) {
            if (num <= 0) {
                throw new IllegalArgumentException("输入数组必须只包含正整数，避免无限递归");
            }
        }

        // 易错的点：要去重要保持单调有序
        Arrays.sort(nums);
        backtrackNoDuplicateElements(nums, target, new ArrayList<>(), 0, result);

        return result;
    }

    private void backtrackNoDuplicateElements(int[] nums, int target, List<Integer> states, int start,
            List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(states));
            return;
        }

        final int length = nums.length;
        for (int i = start; i < length; i++) {
            // 跳过重复元素，避免重复解
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }

            // 作负值剪枝
            if (target - nums[i] < 0) {
                continue;
            }

            states.add(nums[i]);
            // 此处是 i + 1，不是 i 了
            // 修改递归值是类似 twosum 的改法
            backtrackNoDuplicateElements(nums, target - nums[i], states, i + 1, result);
            states.remove(states.size() - 1);
        }
    }
}