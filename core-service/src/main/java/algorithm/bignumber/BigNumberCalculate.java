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
//        final String addResult = add("99", "197");
//        System.out.println(addResult);

        final String addResult2 = add("999", "1");
        System.out.println(addResult2);
    }

    public static String add(String a, String b) {
        return add(toCharArray(a), toCharArray(b));
    }

    public static String add(char[] a, char[] b) {
        // 找出两个数组中更长的作为写入的目标数组
        // 逆序从高位取值，直到两个数组中更短的遍历完
        // 把char转成int，然后做加法，得到结果以后如果大于10，求出余数存到 temp变量里，在下次循环以后用掉

        // 易错点：1. 是双指针同时移动，要用 while -- 的解法，而且 a、b、i、j 的搭配不能漏。如果用 temp 移动，则还需要一个额外指针。
        //        2.  计算 extra 要知道要从 10 开始计算起，所以要 >= 10
        //        3. extra 要提前加，清掉，再算下一个 extra，extra 是1，而不是真的余数，【因为高位只进一】
        //        4. 终止完了以后，extra 还要再好几次，要考虑连续进位，而且只对长数组进位，而不是 temp 数组进位。
        //        5. 用 while 来代替 if 更好
        //        6. 最高位仍然可能越位，所以不能使用普通的 temp，最好使用更长的临时数组，而且临时数组最好先赋0，否则以后做加法取值会不均衡
        //        7. 前导0只去掉前导位
        if (a == null || b == null || a.length == 0 || b.length == 0) {
            throw new IllegalArgumentException("illegal argument:" + Arrays.toString(a) + "," + Arrays.toString(b));
        }

        int tempLength = a.length;
        // 更优的解可能是这里把长短数组分出来，而不是使用 a 来区分，这样说的话最好用个 temp 来交换 ab
        if (a.length < b.length) {
            char[] temp = a;
            a = b;
            b = temp;
        }

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

        // 如果较短的数组遍历完了，extra还没用尽，那么继续加下去，考虑连续进位问题，
        // 这里面有个小 tricky，temp 天然比长数组要长一个格，所以要越界是长数组先越界
        while (extra > 0 && tempPiv - 1 >= 0) {
            // 用长数组继续进行 extra 加法
            char cur = a[i];
            int result = atoi(cur);
            result += extra;
            extra = 0;
            if (result >= 10) {
                extra = 1;
                result -= 10;
            }
            temp[tempPiv] = itoa(result);
            tempPiv--;
            i--;
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
        final Integer i = atoiMap.get(c);
        if (i == null) {
            throw new IllegalArgumentException("atoi error: " + c);
        }
        return i;
    }

    private static char itoa(int i) {
        final Character c = itoaMap.get(i);
        if (c == null) {
            throw new IllegalArgumentException("itoa error: " + i);
        }
        return c;
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
