package algorithm.expand;


import java.util.ArrayList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 目标
 * 假设：
 * 一级类型 1（图片）：[imgA, imgB]
 * 一级类型 2（文案）：[txtX, txtY]
 * 则输出为 4 个组合：
 *
 * [imgA, txtX]
 * [imgA, txtY]
 * [imgB, txtX]
 * [imgB, txtY]
 * 扩充过程是：
 * [[]]
 * [[imgA]]
 * [[imgA], [imgB]]
 * [[imgA, txtX], [imgB]]
 * [[imgA, txtX], [imgA, txtY], [imgB]]
 * [[imgA, txtX], [imgA, txtY], [imgB, txtX], [imgB]]
 * [[imgA, txtX], [imgA, txtY], [imgB, txtX], [imgB, txtY]]
 *
 * @author magicliang
 *
 *         date: 2025-07-30 16:48
 */
public class ExpandExperiment {

    public static void main(String[] args) {
        List<List<String>> componentLists = new ArrayList<>();
        List<String> imageComponents = new ArrayList<>();
        componentLists.add(imageComponents);
        imageComponents.add("imgA");
        imageComponents.add("imgB");

        List<String> textComponents = new ArrayList<>();
        textComponents.add("txtX");
        textComponents.add("txtY");
        componentLists.add(textComponents);

        final List<List<String>> results = expandToCombinations1(componentLists);
        System.out.println(results);
    }

    /**
     * 算法的本质是：
     * 1. 从一个空的列表套列表 {{}} 开始
     * 2. 然后用 originTypes 的每个元素进行叉乘
     * 3. 每一轮就把这个 result 升级一次
     * 4. 所以要在 originTypes 的每一行里准备一个 temp 替换新的 result
     * 5. 遍历完这一轮的整个 originType 列表以后，才实现替换
     * 6. 在遍历 originType 内部的时候，从 result 的内部用元素与元素乘出来我们需要的元素，然后作为结果的新行插进 temp 结果里
     * 7. 然后完成替换
     *
     * @param originTypes 输入的列表套列表
     * @param <T> 泛型类型
     * @return 所有可能的组合结果
     */
    public static <T> List<List<T>> expandToCombinations1(List<List<T>> originTypes) {
        // 插入一个空集合进行迭代
        List<List<T>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (List<T> originType : originTypes) {
            // 每一个列的子元素全部插入，组件嵌套表升级一轮
            List<List<T>> tempResult = new ArrayList<>();
            for (T originTypeEle : originType) {
                // 如果把 升级放在这里，第一级的 size list 的嵌套元素的始终只有一个，只在一个嵌套元素里插入升级，最后结果就变成 [imgA, imgB, txtX, txtY]
                for (List<T> currentRow : result) {
                    // 化新元素为行
                    List<T> newRows = new ArrayList<>(currentRow.size());
                    // 插入新行
                    newRows.addAll(currentRow);
                    newRows.add(originTypeEle);
                    tempResult.add(newRows);
                }
            }
            result = tempResult;
        }

        return result;
    }
}
