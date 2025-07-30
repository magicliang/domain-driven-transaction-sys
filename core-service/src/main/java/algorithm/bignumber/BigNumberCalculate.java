package algorithm.bignumber;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2025-07-30 21:10
 */
public class BigNumberCalculate {

    static Map<Character, Integer> atoiMap = new HashMap<>();
    static Map<Integer, Character> itoaMap = new HashMap<>();

    static {
        atoiMap.put('0', 0);
        atoiMap.put('1', 1);
        atoiMap.put('2', 2);
        atoiMap.put('3', 3);
        atoiMap.put('4', 4);
        atoiMap.put('5', 5);
        atoiMap.put('6', 6);
        atoiMap.put('7', 7);
        atoiMap.put('8', 8);
        atoiMap.put('9', 9);

        itoaMap.put(0, '0');
        itoaMap.put(1, '1');
        itoaMap.put(2, '2');
        itoaMap.put(3, '3');
        itoaMap.put(4, '4');
        itoaMap.put(5, '5');
        itoaMap.put(6, '6');
        itoaMap.put(7, '7');
        itoaMap.put(8, '8');
        itoaMap.put(9, '9');
    }

    public static void main(String[] args) {
        System.out.println(add("1", "999"));          // 1000 ✔
        System.out.println(add("999", "1"));          // 1000 ✔
        System.out.println(add("0", "0"));            // 0    ✔
        System.out.println(add("123456789", "987654321")); // 1111111110 ✔
    }

    public static String add(String a, String b) {
        return add(toCharArray(a), toCharArray(b));
    }

    public static String add(char[] a, char[] b) {
        // 1. 找出两个数组中更长的作为写入的目标数组
        // 2. 逆序从高位取值，直到两个数组中更短的遍历完
        // 3. 把char转成int，然后做加法，得到结果以后如果大于10，求出余数存到 temp变量里，在下次循环以后用掉

        // 易错点：1. 是多指针同时移动，要用 while -- 的解法，而不能用嵌套 for 循环。而且 a、b、i、j 的搭配不能写错，复制粘贴最容易出错了。如果用 temp 移动，则还需要一个额外指针。不要共用指针。
        //        2. 计算 extra 要知道要从 10 开始计算起，所以要 result >= 10，而不是 result > 10。
        //        3. extra 要提前加，清掉，再算下一个 extra，extra 是1，而不是真的余数，【因为高位只进一】。
        //        4. 终止完了以后，extra 还剩下，还要继续加，要考虑连续进位，继续对长数组进位，把结果写回 temp 数组。
        //        5. 用 while 来代替 if 更好
        //        6. 最高位仍然可能越位，所以不能使用普通的 temp，最好使用更长的临时数组。每次相加其实是temp比长数组更长，长数组比短数组更长，计算到后面要保证extra能够和每一个更长数组继续处理的问题。
        //        7. 前导0只去掉前导位
        if (a == null || b == null || a.length == 0 || b.length == 0) {
            throw new IllegalArgumentException("illegal argument:" + Arrays.toString(a) + "," + Arrays.toString(b));
        }

        // 更优的解可能是这里把长短数组分出来，而不是使用 a 来区分，这样说的话最好用个 temp 来交换 ab
        if (a.length < b.length) {
            char[] temp = a;
            a = b;
            b = temp;
        }

        int tempLength = a.length;
        char[] temp = new char[tempLength + 1];

        // 先赋0，而且要考虑多余的0的问题
        for (int i = 0; i < temp.length; i++) {
            temp[i] = '0';
        }

        int extra = 0;

        // 一起移动的指针不可以用嵌套for循环叉乘移动，而要在一个 while 里面

        int i = a.length - 1;
        int j = b.length - 1;
        int tempPiv = temp.length - 1;

        // 默认 a 是更长的部分
        while (i >= 0 && j >= 0) {
            final Integer ai = atoi(a[i]);
            final Integer bj = atoi(b[j]);

            // 把上一轮的用在这里
            int result = extra + ai + bj;
            // 用掉
            extra = 0;

            // 而且减法要从10开始，而不是11
            if (result >= 10) {
                extra = 1;
                result -= 10;
            }

            // 不要忘记有3个指针要移动
            temp[tempPiv--] = itoa(result);
            i--;
            j--;
        }

        // 如果较短的数组遍历完了，extra还没用尽，那么继续加下去，考虑连续进位问题，只对长数组做减法
        // 有可能 i 遍历完了，仍然要往下进一位
        while (extra > 0 && i >= 0) {
                // 能从长数组里面取值就从长数组里面取值
                // 用长数组继续进行 extra 加法
                char cur = a[i--];
            int result = atoi(cur);
                result += extra;
                extra = 0;
                if (result >= 10) {
                    extra = 1;
                    result -= 10;
                }

            temp[tempPiv--] = itoa(result);
        }

        // 最后一位进位直接赋值就行，无需加法
        if (extra > 0) {
            temp[tempPiv--] = itoa(extra);
        }

        StringBuilder sb = new StringBuilder();

        boolean metNoneZero = false;
        for (char c : temp) {
            if (c != '0' || metNoneZero) {
                metNoneZero = true;
                sb.append(c);
            }
        }
        // 还要考虑全0相加的问题
        if (sb.length() == 0) {
            return "0";
        }

        return sb.toString();
    }

    private static Integer atoi(char c) {
        int i = c - '0';
        if (i < 0 || i > 9) {
            throw new IllegalArgumentException("atoi error: " + c);
        }
        return i;
    }

    private static char itoa(int i) {
        final Character c = itoaMap.get(i);
        if (i < 0 || i > 9) {
            throw new IllegalArgumentException("itoa error: " + c);
        }
        return (char) (i + '0');
    }

    private static char[] toCharArray(String a) {
        if (a == null || a.isEmpty()) {
            return new char[]{};
        }
        char[] result = new char[a.length()];
        for (int i = 0; i < a.length(); i++) {
            result[i] = a.charAt(i);
        }
        return result;
    }
}
