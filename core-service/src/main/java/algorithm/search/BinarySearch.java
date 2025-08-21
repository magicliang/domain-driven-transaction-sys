package algorithm.search;

/**
 * @author liangchuan
 */
public class BinarySearch {

    /**
     * 在有序数组中查找目标值的插入位置。
     * <p>
     * 如果目标值存在于数组中，则返回其索引；
     * 如果不存在，则返回目标值应该插入的位置索引，保持数组有序。
     * <p>
     * 使用闭区间 [l, r] 模型实现二分查找。
     *
     * @param nums 升序排列的整数数组，不允许为 null
     * @param target 要查找或插入的目标整数值
     * @return 目标值的索引或其应该插入的位置索引
     * @throws NullPointerException 如果 nums 为 null
     * @example searchInsert1([1, 3, 5, 6], 5) → 返回 2
     *         searchInsert1([1,3,5,6], 2) → 返回 1
     *         searchInsert1([1,3,5,6], 7) → 返回 4
     *         searchInsert1([1,3,5,6], 0) → 返回 0
     */
    public int searchInsert1(int[] nums, int target) {

        // 这道题易错的点在于理论插入位置可能是在现行数组边界之外的
        int l = 0;
        int r = nums.length - 1; // 易错的点：闭区间问题需要 -1

        // 使用闭区间 [l, r] 进行二分查找
        // 循环条件 l <= r 确保区间有效
        while (l <= r) {
            // 计算中间位置，使用 l + (r - l) / 2 避免整数溢出
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                // 找到目标值，直接返回其索引
                return mid;
            } else if (nums[mid] < target) {
                // 区间右移
                // 当前值小于目标值，说明目标值在右半部分
                l = mid + 1;
            } else if (nums[mid] > target) {
                // 区间左移
                // 当前值大于目标值，说明目标值在左半部分
                r = mid - 1;
            }
        }

        // 终结以后，r + 1 = l，这里的 l 是理论插入位，可能是 nums.length，如果是二分搜索应该返回-1的。
        // nums[r] <= target <= nums[l]
        // 循环结束时，l == r + 1，此时：
        // - 如果 r >= 0，则 nums[r] < target
        // - l 是第一个 >= target 的位置，即插入位置
        // 这个性质源于二分查找的区间收缩单调性
        // 此时 l 指向的位置就是 target 应该插入的位置
        return l;
    }

    /**
     * 在一个升序排列的整数数组中执行二分查找，返回目标值 target 的索引。
     * 如果目标值不存在于数组中，则返回 -1。
     *
     * @param nums 升序排列的整数数组，不允许为 null，允许为空数组
     * @param target 要查找的目标整数值
     * @return 目标值在数组中的索引；若未找到，返回 -1
     * @throws NullPointerException 如果 nums 为 null
     */
    public int binarySearch(int[] nums, int target) {
        // 二分查找标准实现：在有序数组中查找 target 的索引
        // 使用闭区间 [l, r]，所以 r 初始化为 nums.length - 1
        int l = 0;
        int r = nums.length - 1;

        // 标准二分查找循环
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                return mid;  // 找到目标，直接返回索引
            } else if (nums[mid] < target) {
                // 当前值太小，搜索右半部分
                l = mid + 1;
            } else if (nums[mid] > target) {
                // 当前值太大，搜索左半部分
                r = mid - 1;
            }
        }

        // 循环结束表示未找到 target
        // 此时 l > r，常见情况是 l = r + 1
        // 其实这时候 r + 1 = l 是有可能得到某一个索引越界的情况（如 l 超出数组范围），
        // 但由于我们只在合法范围内访问 nums[mid]，且循环条件保证了 l 和 r 的有效使用，
        // 因此不会发生数组越界访问。这里只需返回 -1 表示未找到。
        return -1;
    }

    /**
     * 在有序数组中查找目标值的插入位置。
     * <p>
     * 如果目标值存在于数组中，则返回其索引；
     * 如果不存在，则返回目标值应该插入的位置索引，保持数组有序。
     * <p>
     * 使用左闭右开区间 [l, r) 模型实现二分查找。
     *
     * @param nums 升序排列的整数数组，不允许为 null
     * @param target 要查找或插入的目标整数值
     * @return 目标值的索引或其应该插入的位置索引
     * @throws NullPointerException 如果 nums 为 null
     * @example searchInsert2([1, 3, 5, 6], 5) → 返回 2
     *         searchInsert2([1,3,5,6], 2) → 返回 1
     *         searchInsert2([1,3,5,6], 7) → 返回 4
     *         searchInsert2([1,3,5,6], 0) → 返回 0
     */
    public int searchInsert2(int[] nums, int target) {
        // 这道题易错的点在于插入位置可能是在现行数组边界之外的
        int l = 0;
        int r = nums.length; // 易错的点：这里 r 初始化为 nums.length，表示右边界是开区间，搜索区间是 [l, r)

        // 使用左闭右开区间 [l, r) 进行二分查找
        // 循环条件 l < r 确保区间有效
        while (l < r) {
            // 计算中间位置
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                // 找到目标值，直接返回其索引
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

    /**
     * 在升序数组中查找目标值的左边界（即第一个等于 target 的元素的索引）。
     * <p>
     * 如果目标值不存在于数组中，则返回 -1。
     * <p>
     * 使用左闭右开区间 [l, r) 模型实现二分查找。
     *
     * @param nums 升序排列的整数数组，不允许为 null
     * @param target 要查找的目标整数值
     * @return 目标值的左边界索引；如果不存在，返回 -1
     * @throws NullPointerException 如果 nums 为 null
     * @example leftBound1([1, 2, 2, 2, 3], 2) → 返回 1
     *         leftBound1([1,2,3,4,5], 6) → 返回 -1（不存在）
     *         leftBound1([2,3,4], 1)     → 返回 -1（比所有数都小）
     */
    public int leftBound1(int[] nums, int target) {
        int l = 0;
        int r = nums.length;

        // 使用左闭右开区间 [l, r) 寻找左边界
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                // 找到等价的值的时候先别处理，想一想要左搜索还是右搜索，我们要找左边界，任意一个点可能都是在左边界右边的，所以往左收敛
                // 找到目标值，但可能不是最左边的，继续向左搜索
                r = mid;
            } else if (nums[mid] < target) {
                // 当前值小于目标值，左边界在右边
                l = mid + 1;
            } else if (nums[mid] > target) {
                // 当前值大于目标值，左边界在左边
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
     * searchInsert 找的是"插入位置"——第一个 >= target 的位置，这是一种"理论边界"，即使 target 不存在也有意义。
     * leftBound   找的是"第一个等于 target 的位置"——是一种"实际边界"，只有 target 存在时才有意义。
     * floor 要找理论边界的另一个临界点
     * <p>
     * 因此，floor 应该基于 searchInsert 实现：floor = searchInsert(target) - 1
     *
     * @param nums 升序整数数组，不允许为 null
     * @param target 要比较的目标值
     * @return 小于 target 的最大元素的索引；如果不存在，返回 -1
     * @example floor([1, 2, 2, 2, 3], 2)  → 返回 0（因为 1 是小于 2 的最大元素）
     *         floor([1,2,3,5,6], 4)  → 返回 2（因为 3 是小于 4 的最大元素）
     *         floor([1,2,3], 0)      → 返回 -1（没有元素小于 0）
     *         floor([1,2,3], 4)      → 返回 2（3 是小于 4 的最大元素）
     */
    public int floor(int[] nums, int target) {
        // 使用 searchInsert1 找到第一个 >= target 的位置
        int insertPos = searchInsert1(nums, target); // 第一个 >= target 的位置
        // 这个位置的前一个位置就是最后一个 < target 的位置，即 floor 值
        return insertPos - 1; // 它前面的位置就是最后一个 < target 的位置
    }

    /**
     * 在升序数组中查找目标值的左边界（即第一个等于 target 的元素的索引）。
     * <p>
     * 如果目标值不存在于数组中，则返回 -1。
     * <p>
     * 使用闭区间 [l, r] 模型实现二分查找。
     *
     * @param nums 升序排列的整数数组，不允许为 null
     * @param target 要查找的目标整数值
     * @return 目标值的左边界索引；如果不存在，返回 -1
     * @throws NullPointerException 如果 nums 为 null
     * @example leftBound2([1, 2, 2, 2, 3], 2) → 返回 1
     *         leftBound2([1,2,3,4,5], 6) → 返回 -1（不存在）
     *         leftBound2([2,3,4], 1)     → 返回 -1（比所有数都小）
     */
    public int leftBound2(int[] nums, int target) {
        int l = 0;
        int r = nums.length - 1; // 闭区间 [l, r]，所以 r 初始化为最后一个合法索引

        // 使用闭区间 [l, r] 寻找左边界
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                // 找到 target，但不一定是最左边的，继续向左收缩
                // 为了找左边界，必须继续向左收缩：把右边界 r 拉到 mid 左边
                r = mid - 1;
            } else if (nums[mid] < target) {
                // 当前值太小，说明左边界在 mid 右边
                // mid 本身不满足 >= target，所以从 mid+1 开始
                l = mid + 1;  // 当前值太小，往右找
            } else if (nums[mid] > target) {
                // 当前值太大，说明左边界在 mid 左边
                // mid 本身大于 target，不是目标位置，可以排除
                r = mid - 1;  // 当前值太大，往左找
            }
        }

        // 循环结束时，l == r + 1
        // 此时：
        //   - l 是第一个 >= target 的位置（插入位置）
        //   - r 是最后一个 < target 的位置
        // 但我们不能直接返回 l，因为：
        //   1. l 可能越界（比如 target 比所有数都大）
        //   2. nums[l] 可能不等于 target（target 根本不存在）
        if (l >= nums.length) {
            return -1; // 越界了，说明 target 比所有元素都大
        }

        // 此时 l 在数组范围内，但 nums[l] 不一定是 target
        // 比如 target = 4, nums = [1,2,3,5]，l=3，nums[3]=5 ≠ 4
        // 所以必须再检查一下是否真的找到了 target
        return nums[l] == target ? l : -1;
    }

    /**
     * 在升序数组中查找 target 的右边界（即最后一个等于 target 的元素的索引）。
     * <p>
     * 如果 target 不存在于数组中，则返回 -1。
     * <p>
     * 使用左闭右开区间 [l, r) 模型实现二分查找。
     * 核心口诀：l 永远是第一个 >= target 的位置 → 但右边界要靠"第一个 > target 的位置 - 1"
     *
     * @param nums 升序整数数组，不能为空或 null
     * @param target 要查找的目标值
     * @return target 的右边界索引；如果不存在，返回 -1
     * @throws NullPointerException 如果 nums 为 null
     * @example rightBound1([1, 2, 2, 2, 3], 2) → 返回 3
     *         rightBound1([1,2,3,4,5], 6) → 返回 -1（不存在）
     *         rightBound1([2,3,4], 1)     → 返回 -1（比所有数都小）
     *         rightBound1([1], 1)         → 返回 0
     */
    public int rightBound1(int[] nums, int target) {
        int l = 0;
        int r = nums.length; // 搜索区间 [l, r)：左闭右开，r 是"边界标记"，不是数据位置

        // 使用左闭右开区间 [l, r) 寻找右边界
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                // 找到 target，但不一定是最右边的
                // 为了找右边界，必须继续向右找：把左边界提到 mid + 1
                // 因为 right 本身不是确切坐标（是开区间），所以我们可以放心地让 l 走出去
                // 想象我们找到了最后一个 target，仍然尝试"走出去一步"，所以最终 l 会停在第一个 > target 的位置
                l = mid + 1;
            } else if (nums[mid] < target) {
                // 当前值太小，往右找
                l = mid + 1;
            } else if (nums[mid] > target) {
                // 当前值太大，往左找
                r = mid; // 收缩右边界，但不包含 mid
            }
        }

        // 循环结束时 l == r
        // 此时：
        //   - l 是第一个 > target 的位置（注意：不是 >=，而是 >）
        //   - 所以 l - 1 是最后一个 <= target 的位置
        //   - 如果 nums[l - 1] == target，那它就是右边界
        //
        // 但我们必须检查：
        //   1. l == 0？→ 说明所有元素都 > target，l-1 越界
        //   2. nums[l - 1] == target？→ 否则 target 根本不存在
        if (l == 0) {
            return -1; // l - 1 = -1，越界，说明 target 比所有元素都小
        }

        // 想象极端情况：nums = [5], target = 5
        //   l=0, r=1 → mid=0, nums[0]==5 → l = 1
        //   l == r == 1，退出
        //   l-1 = 0，nums[0] == 5 → 返回 0 ✅
        //
        // 所以即使 l == r，我们也要返回 l - 1 才是右边界
        return nums[l - 1] == target ? l - 1 : -1;
    }

    /**
     * 在升序数组中查找 target 的右边界（即最后一个等于 target 的元素的索引）。
     * <p>
     * 如果 target 不存在于数组中，则返回 -1。
     * <p>
     * 使用闭区间 [l, r] 模型实现，是二分查找的经典变种之一。
     * 核心思想：通过不断向右推进左边界，让 l 最终停在"第一个大于 target 的位置"，
     * 然后返回 l - 1 作为右边界候选。
     *
     * @param nums 升序排列的整数数组，不能为空或 null
     * @param target 要查找的目标值
     * @return target 的右边界索引；如果不存在，返回 -1
     * @throws NullPointerException 如果 nums 为 null
     * @example rightBound2([1, 2, 2, 2, 3], 2) → 返回 3
     *         rightBound2([1,2,3,4,5], 6) → 返回 -1（target 不存在）
     *         rightBound2([2,3,4], 1)     → 返回 -1（target 比所有元素都小）
     *         rightBound2([1], 1)         → 返回 0
     *         rightBound2([1,2,3], 0)     → 返回 -1（越界）
     * @note - 闭区间模型：循环结束时 l == r + 1
     *         - l 是第一个 > target 的位置
     *         - 因此 l - 1 是最后一个 <= target 的位置
     *         - 必须检查 l - 1 是否越界，以及 nums[l - 1] 是否等于 target
     */
    public int rightBound2(int[] nums, int target) {
        int l = 0;
        int r = nums.length - 1;

        // 使用闭区间 [l, r] 寻找右边界
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                // 找到 target，继续向右找，试图找到更靠右的 target
                l = mid + 1;  // 继续向右找，试图找到更靠右的 target
            } else if (nums[mid] < target) {
                // 当前值太小，往右找
                l = mid + 1;  // 当前值太小，往右找
            } else if (nums[mid] > target) {
                // 当前值太大，往左找
                r = mid - 1;  // 当前值太大，往左找
            }
        }

        // 退出时，r + 1 = l
        // 此时 l 是第一个 > target 的位置
        // 所以 l - 1 是最后一个 <= target 的位置
        // 如果 nums[l - 1] == target，那它就是右边界
        if (l - 1 < 0) {
            return -1; // 越界，说明 target 比所有元素都小，l-1 = -1 不合法
        }
        // 检查 l-1 位置是否确实等于 target
        return nums[l - 1] == target ? l - 1 : -1;
    }

}


