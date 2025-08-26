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
     * 回溯算法核心实现 - 允许重复选择元素的子集和求解
     *
     * @param nums 输入整数数组，允许包含重复元素
     * @param target 剩余需要达到的目标和值
     * @param states 当前已选择的元素列表，表示当前路径状态
     * @param total 当前已选择元素的总和
     * @param result 存储所有满足条件的子集结果列表
     * @implNote 实现细节：
     *         - 使用深度优先搜索(DFS)遍历所有可能的组合
     *         - 允许重复使用数组中的元素（通过不更新start索引实现）
     *         - 在进入递归前进行剪枝，避免无效搜索
     *         - 使用回溯模板：选择→递归→撤销选择
     * @algorithm 回溯算法步骤：
     *         1. 终止条件：当total等于target时，找到有效解
     *         2. 剪枝条件：当total超过target时，停止当前路径搜索
     *         3. 选择阶段：遍历所有元素，允许重复选择
     *         4. 递归探索：对每个选择进行深度优先搜索
     *         5. 撤销选择：回溯到上一步状态，继续探索其他可能性
     * @timeComplexity O(n ^ m) 其中n是数组长度，m是target/min(nums)的近似值
     * @spaceComplexity O(m) 递归栈的最大深度
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
     * 不重复使用元素的子集和回溯算法实现
     *
     * @param nums 已排序的输入整数数组，必须预先排序以支持重复元素去重
     * @param target 剩余需要达到的目标和值
     * @param states 当前已选择的元素列表，表示当前路径状态
     * @param start 起始搜索位置索引，确保不重复使用前面的元素
     * @param result 存储所有满足条件的子集结果列表
     * @implNote 实现细节：
     *         - 使用排序后的数组来支持重复元素的去重处理
     *         - 通过start参数控制元素使用范围，避免重复使用
     *         - 在同一层级跳过重复值，避免产生重复解
     *         - 利用排序特性进行负值剪枝，提前终止无效搜索
     * @algorithm 回溯算法步骤：
     *         1. 终止条件：当target减至0时，找到有效解
     *         2. 选择范围：从start位置开始遍历，避免重复使用元素
     *         3. 去重处理：跳过同一层级的重复元素
     *         4. 剪枝优化：利用排序特性，提前终止不可能的路径
     *         5. 递归探索：对每个有效选择进行深度优先搜索
     *         6. 回溯撤销：移除最后选择的元素，继续探索其他可能性
     * @timeComplexity O(2 ^ n) 其中n是数组长度，每个元素都有选或不选两种选择
     * @spaceComplexity O(n) 递归栈的最大深度，最坏情况下为数组长度
     * @see #subsetSumNoDuplicateCombination(int[], int) 公有方法调用此私有方法
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
            // 关键：i > start 确保只在同一层级跳过重复，不影响不同层级的相同值
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }

            // 作负值剪枝
            // 由于数组已排序，后续元素更大，可以提前终止
            if (target - nums[i] < 0) {
                continue;
            }

            states.add(nums[i]);

            // 传入i+1确保不重复使用当前元素
            // 关键：i+1 确保每个元素只使用一次
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
        // 排序是后续跳过重复元素的前提条件
        Arrays.sort(nums);
        backtrackNoDuplicateElements(nums, target, new ArrayList<>(), 0, result);

        return result;
    }

    /**
     * 不重复使用元素的子集和回溯算法实现（与subsetSumNoDuplicateCombination功能相同）
     *
     * @param nums 已排序的输入整数数组，必须预先排序以支持重复元素去重
     * @param target 剩余需要达到的目标和值
     * @param states 当前已选择的元素列表，表示当前路径状态
     * @param start 起始搜索位置索引，确保不重复使用前面的元素
     * @param result 存储所有满足条件的子集结果列表
     * @implNote 实现细节：
     *         - 此实现与{@link #backtrackNoDuplicateCombination}方法功能完全相同
     *         - 使用排序后的数组来支持重复元素的去重处理
     *         - 通过start参数控制元素使用范围，避免重复使用
     *         - 在同一层级跳过重复值，避免产生重复解
     *         - 利用排序特性进行负值剪枝，提前终止无效搜索
     * @algorithm 回溯算法步骤：
     *         1. 终止条件：当target减至0时，找到有效解
     *         2. 选择范围：从start位置开始遍历，避免重复使用元素
     *         3. 去重处理：跳过同一层级的重复元素（i > start && nums[i] == nums[i-1]）
     *         4. 剪枝优化：利用排序特性，当target - nums[i] < 0时提前终止
     *         5. 递归探索：对每个有效选择进行深度优先搜索
     *         6. 回溯撤销：移除最后选择的元素，继续探索其他可能性
     * @timeComplexity O(2 ^ n) 其中n是数组长度，每个元素都有选或不选两种选择
     * @spaceComplexity O(n) 递归栈的最大深度，最坏情况下为数组长度
     * @see #subsetSumNoDuplicateElements(int[], int) 公有方法调用此私有方法
     * @see #backtrackNoDuplicateCombination(int[], int, List, int, List) 功能相同的方法
     */
    private void backtrackNoDuplicateElements(int[] nums, int target, List<Integer> states, int start,
            List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(states));
            return;
        }

        final int length = nums.length;
        for (int i = start; i < length; i++) {
            // 跳过重复元素，避免重复解
            // 关键：只在同一层级跳过重复，不影响不同层级的相同值
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }

            // 作负值剪枝
            // 由于数组已排序，后续元素更大，可以提前终止循环
            if (target - nums[i] < 0) {
                continue;
            }

            states.add(nums[i]);
            // 此处是 i + 1，不是 i 了
            // 修改递归值是类似 twosum 的改法
            // 关键：i+1 确保每个元素只使用一次，避免重复使用
            backtrackNoDuplicateElements(nums, target - nums[i], states, i + 1, result);
            states.remove(states.size() - 1);
        }
    }
}