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

        final List<List<String>> results = expandToCombinations(componentLists);
        System.out.println(results);
    }

    public static <T> List<List<T>> expandToCombinations(List<List<T>> originTypes) {
        // 插入一个空集合进行迭代
        List<List<T>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (List<T> originType : originTypes) {
            // 每一个列的子元素全部插入，组件嵌套表升级一轮
            List<List<T>> tempResult = new ArrayList<>();
            for (T originTypeEle : originType) {
                // 如果把 升级放在这里，第一级的 size list 的嵌套元素的始终只有一个，只在一个嵌套元素里插入升级，最后结果就变成 [imgA, imgB, txtX, txtY]
                for (List<T> currentRow : result) {
                    List<T> newRows = new ArrayList<>(currentRow.size());
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
