package algorithm.search;

/**
 * @author liangchuan
 */
public class BinarySearch {

    public int searchInsert1(int nums[], int target) {

        // 这道题易错的点在于插入位置可能是在现行数组边界之外的
        int l = 0;
        int r = nums.length - 1; // 易错的点：闭区间问题需要

        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                // 区间右移
                l = mid + 1;
            } else if (nums[mid] > target) {
                // 区间左移
                r = mid - 1;
            }
        }

        // 终结以后，r + 1 = l
        // nums[r] <= target <= nums[l]
        return l;
    }

    public int searchInsert2(int[] nums, int target) {
        // 这道题易错的点在于插入位置可能是在现行数组边界之外的
        int l = 0;
        int r = nums.length; // 易错的点：这里 r 初始化为 nums.length，表示右边界是开区间，搜索区间是 [l, r)

        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                // 区间右移：mid 位置的值小于 target，所以插入位置在 mid+1 及其右边
                l = mid + 1;
            } else if (nums[mid] > target) {
                // 区间左移：mid 位置的值大于 target，mid 本身可能就是插入点，但不能再往右了
                r = mid; // 所以把 r 设为 mid，保持左闭右开的语义
            }
        }

        // 终止时 l == r，循环结束
        // 此时，l 是第一个满足 ">= target" 的位置：
        //   - 所有 < target 的位置都已经被 l 抛在左边（因为 l = mid + 1 只在 nums[mid] < target 时发生）
        //   - 所有 >= target 的候选都被 r 不断收缩保留下来
        // 因此 l（也就是 r）就是 target 应该插入的位置
        // 特别地，这个位置可以是 nums.length（插在末尾），也可以是 0（插在开头），完全合法
        // 最终 l 和 r 撞在一起了，l 左边全是比 target 小的，r 右边全是比 target 大的
        // 那么 l 这个位置，就是 target 正好可以插进去的地方
        return l;
    }
}
