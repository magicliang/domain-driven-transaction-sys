package generic;

import java.util.List;

/**
 * @author liangchuan
 */
public class WildcardCapture {
    // 辅助泛型方法：捕获通配符类型
    private static <T> T swapHelper(List<T> list, int i, int j) {
        // 只要不返回这个 temp，就不会有编译问题，在方法体内可以随意使用 a。用有值的实例来获取泛型变量。
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
        return temp;
    }

    // 对外暴露的通配符方法。这里换成 List<Pair<?>>，内部也换成 List<Pair<T>>，就无法编译了。这是因为泛型里List的元素1和2完全可能是不同的 Pair<?>。? 不是实参的意思
    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j); // 通配符捕获在此发生
    }

    public static void main(String[] args) {

    }
}
