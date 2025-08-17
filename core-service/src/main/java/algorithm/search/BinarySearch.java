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

    public int leftBound1(int nums[], int target) {
        int l = 0;
        int r = nums.length;

        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                // 找到等价的值的时候先别处理，想一想要左搜索还是右搜索，因为 right 本身并不是一个确切的坐标，所以收窄左可能找不到，但是收窄右，我们还可以用现在 l 返回
                r = mid;
            } else if (nums[mid] < target) {
                l = mid + 1;
            } else if (nums[mid] > target) {
                r = mid;
            }
        }

        // 易错的的点：l==r也可能是非法的
        if (l == nums.length) {
            return -1;
        }

        // 当这个算法退出的时候，l==r。找得到还是找不到呢？找不到返回-1
        // 这种搜索的最后一步通常没有校验nums[l]和target的关系，此时我们要教研一下是不是真的找到了target，并且是左边界；否则返回-1
        return nums[l] == target ? l : -1;
    }

    /**
     * 在一个升序数组中，找到小于 target 的最大元素的索引（即 floor 值）。
     * <p>
     * 如果不存在这样的元素（即所有元素都 >= target），则返回 -1。
     * <p>
     * 易错的点：
     * floor 需要用到 searchInsert，而不能依赖 leftBound。
     * 因为 leftBound 只有在 target 存在时才能返回有效索引；如果 target 不存在，它会返回 -1。
     * <p>
     * 举例：
     * nums = [1, 3, 5, 7], target = 4
     * 我们希望 floor 返回 1（因为 nums[1] = 3 是小于 4 的最大元素）
     * 但 leftBound(4) 会返回 -1（因为 4 不存在）
     * 如果基于 leftBound - 1 计算 floor，就会得到 -2，完全错误。
     * <p>
     * 关键区别：
     * searchInsert 找的是“插入位置”——第一个 >= target 的位置，这是一种“理论边界”，即使 target 不存在也有意义。
     * leftBound   找的是“第一个等于 target 的位置”——是一种“实际边界”，只有 target 存在时才有意义。
     * <p>
     * 因此，floor 应该基于 searchInsert 实现：floor = searchInsert(target) - 1
     *
     * @param nums   升序整数数组，不允许为 null
     * @param target 要比较的目标值
     * @return 小于 target 的最大元素的索引；如果不存在，返回 -1
     * @example floor([1, 2, 2, 2, 3], 2)  → 返回 0（因为 1 是小于 2 的最大元素）
     * floor([1,2,3,5,6], 4)  → 返回 2（因为 3 是小于 4 的最大元素）
     * floor([1,2,3], 0)      → 返回 -1（没有元素小于 0）
     * floor([1,2,3], 4)      → 返回 2（3 是小于 4 的最大元素）
     */
    public int floor(int[] nums, int target) {
        int insertPos = searchInsert1(nums, target); // 第一个 >= target 的位置
        return insertPos - 1; // 它前面的位置就是最后一个 < target 的位置
    }

}


