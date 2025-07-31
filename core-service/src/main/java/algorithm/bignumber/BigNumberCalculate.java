package algorithm.bignumber;


import java.util.Arrays;

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

    public static void main(String[] args) {

        System.out.println("-----------------------------add1"); // 12

        System.out.println(add("1", "999"));          // 1000 ✔
        System.out.println(add("999", "1"));          // 1000 ✔
        System.out.println(add("0", "0"));            // 0    ✔
        System.out.println(add("123456789", "987654321")); // 1111111110 ✔
        System.out.println(add("12345", "67")); // 12412 ✔

        System.out.println("-----------------------------add2"); // 12

        System.out.println(add2("1", "999"));          // 1000 ✔
        System.out.println(add2("999", "1"));          // 1000 ✔
        System.out.println(add2("0", "0"));            // 0    ✔
        System.out.println(add2("123456789", "987654321")); // 1111111110 ✔
        System.out.println(add2("12345", "67")); // 12412 ✔

    }

    public static String add(String a, String b) {
        return add(a.toCharArray(), b.toCharArray());
    }

    public static String add2(String a, String b) {
        return add2(a.toCharArray(), b.toCharArray());
    }

    public static String add(char[] a, char[] b) {
        // 1. 找出两个数组中更长的作为写入的目标数组
        // 2. 逆序从高位取值，直到两个数组中更短的遍历完-类似一个 merge sort，所有要按照长短，多轮遍历到尽头-余数也可能是剩余的数据结构的一种
        // 3. 把char转成int，然后做加法，得到结果以后如果大于10，求出余数存到 temp变量里，在下次循环以后用掉

        // 易错点：1. 是多指针同时移动，要用 while -- 的解法，而不能用嵌套 for 循环。而且 a、b、i、j 的搭配不能写错，复制粘贴最容易出错了。如果用 temp 移动，则还需要一个额外指针。不要共用指针。
        //        2. 计算 carry 要知道要从 10 开始计算起，所以要 result >= 10，而不是 result > 10。
        //        3. carry 要提前加，清掉，再算下一个 carry，carry 是1，而不是真的余数，【因为高位只进一】。
        //        4. 终止完了以后，carry 还剩下，还要继续加，要考虑连续进位，继续对长数组进位，把结果写回 temp 数组。
        //        5. 用 while 来代替 if 更好
        //        6. 最高位仍然可能越位，所以不能使用普通的 temp，最好使用更长的临时数组。每次相加其实是temp比长数组更长，长数组比短数组更长，计算到后面要保证extra能够和每一个更长数组继续处理的问题。
        //        7. 前导0只去掉前导位
        //        8. atoi 和 itoa，可以用字符或者数字和0的相对距离来计算
        //        9. 不要写 i >= 0 && carry > 0 或者 i >= 0 || carry > 0，前者可能carry为0了，长数组还有后半部分没走完。后者可能carry不为0，导致 a 越位。
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
        // 这一步是必须的，不然前导空位不能当0去掉
        Arrays.fill(temp, '0');

        int carry = 0;

        // 一起移动的指针不可以用嵌套for循环叉乘移动，而要在一个 while 里面

        int i = a.length - 1;
        int j = b.length - 1;
        int k = temp.length - 1;

        // 默认 a 是更长的部分
        while (i >= 0 && j >= 0) {
            final int ai = atoi(a[i]);
            final int bj = atoi(b[j]);

            // 把上一轮的用在这里
            int result = carry + ai + bj;
            // 用掉
            carry = 0;

            // 而且减法要从10开始，而不是11
            if (result >= 10) {
                carry = 1;
                result -= 10;
            }

            // 不要忘记有3个指针要移动
            temp[k--] = itoa(result);
            i--;
            j--;
        }

        // 如果较短的数组遍历完了，extra还没用尽，那么继续加下去，考虑连续进位问题，只对长数组做减法
        // 有可能 i 遍历完了，仍然要往下进一位
        while (i >= 0) {
            // 能从长数组里面取值就从长数组里面取值
            // 用长数组继续进行 carry 加法
            char cur = a[i];
            int result = atoi(cur);
            result += carry;
            carry = 0;
            if (result >= 10) {
                carry = 1;
                result -= 10;
            }

            temp[k] = itoa(result);
            i--;
            k--;
        }

        // 最后一位进位直接赋值就行，无需加法
        if (carry > 0) {
            temp[k--] = itoa(carry);
        }

        StringBuilder sb = new StringBuilder();

        // 找第一个非零位置
        int start = 0;
        while (start < temp.length - 1 && temp[start] == '0') {
            start++;
        }
        String result = new String(temp, start, temp.length - start);
        return result;
    }

    /**
     * 只用一个循环来写作加法的 merge
     *
     * @param a
     * @param b
     * @return
     */
    public static String add2(char[] a, char[] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) {
            throw new IllegalArgumentException("illegal argument:" + Arrays.toString(a) + "," + Arrays.toString(b));
        }

        int tempLength = a.length;
        if (b.length > tempLength) {
            tempLength = b.length;
        }
        tempLength++;
        char[] temp = new char[tempLength];

        int i = a.length - 1;
        int j = b.length - 1;

        for (int tmp = 0; tmp < tempLength; tmp++) {
            temp[tmp] = '0';
        }

        int carry = 0;
        int k = tempLength - 1;
        // 在一个 while 循环里，用 || 导出循环，用 if 再细分是不是符合循环条件
        while (i >= 0 || j >= 0 || carry > 0 || k > 0) {
            int sum = carry;
            if (i >= 0) {
                sum += a[i--] - '0';
            }
            if (j >= 0) {
                sum += b[j--] - '0';
            }
            // 为下一轮的 carry 做准备
            carry = sum / 10;
            sum %= 10;
            // 因为 i、j、k 走尽以前，k是不会走尽的，所以这里的 if (k >= 0) 和上面的 ||  k > 0 其实是不必要的，但是这里为了谨慎先留着
            if (k >= 0) {
                temp[k--] = (char) ('0' + sum);
            }
        }
//        while (i >= 0 || j >= 0 || carry > 0) {
//            int sum = carry;
//            if (i >= 0) sum += a[i--] - '0';
//            if (j >= 0) sum += b[j--] - '0';
//
//            carry = sum / 10;
//            temp[k--] = (char)(sum % 10 + '0');
//        }

        StringBuilder sb = new StringBuilder();

        int start = 0;
        // 这里要引入一个寻找等于0的计算位，但是不去掉最后一位
        while (start < tempLength - 1 && temp[start] == '0') {
            start++;
        }

        for (int p = start; p < tempLength; p++) {
            sb.append(temp[p]);
        }
        return sb.toString();
    }

    private static int atoi(char c) {
        int i = c - '0';
        if (i < 0 || i > 9) {
            throw new IllegalArgumentException("atoi error: " + c);
        }
        return i;
    }

    private static char itoa(int i) {
        if (i < 0 || i > 9) {
            throw new IllegalArgumentException("itoa error: " + i);
        }
        return (char) (i + '0');
    }
}
