package algorithm.beautiful.chinesechess;


/**
 * project name: domain-driven-transaction-sys
 *
 * description: 中国象棋遍历游戏
 * 1. 对将和帅进行建模：将帅在 3 * 3 的棋盘上变化，它们所在的列就是宽的余数。所以如果是 1based 的矩阵，比较容易得到正整数的列。
 * 2. 在一个 byte 上用前 4bit 表示一个棋子，后 4bit 表示另一个棋子。每个棋子从1到9变化，每四位都用 0001到 0101变化来表示
 * 3. 定义两位函数，把某些数先设置到右边四位，然后再决定是不是要位移四位。
 * 4. 遍历全部的组合，不再同一列上则输出
 *
 * 掩码用左右移制造0
 * 用 0 & 清理特定位
 * 用 1 & 保留特定位
 * 用 1 | 赋值特定位
 *
 * @author magicliang
 *
 *         date: 2025-08-04 19:43
 */
public class Chess {

    private static final int BASIC_MASK = 0b11111111;

    private static final int HALF = 4;

    // 注意这个限制技巧
    private static final int LEFT_MASK =
            (BASIC_MASK << HALF) & BASIC_MASK; // 左移四位首先变成 111111110000，再 & BASIC_MASK 才变成 11110000

    private static final int RIGHT_MASK = BASIC_MASK >> HALF; // 0b00001111

    private static final int WIDTH = 3;

    /**
     * b:        00000101
     * n:        01000000
     * 或运算:   01000101
     *
     * @param b 00110101
     * @param n 00000100
     * @return 01000101
     */
    private static int lSet(int b, int n) {
        b = b & RIGHT_MASK; // 清掉左边，保留右边
        n = (n << HALF) & LEFT_MASK; // 把要设置的数移位
        return b | n;
    }

    private static int lGet(int b) {
        return (b >> HALF) & RIGHT_MASK;
    }

    private static int rSet(int b, int n) {
        b = b & LEFT_MASK; // 清掉右边，保留左边
        return b | n;
    }

    private static int rGet(int b) {
        return b & RIGHT_MASK; // 清掉左边，保留右边
    }

    private static int getColumn(int n) {
        // 如果是从 1 开始 base，则返回1、2、3都可能
        return n % WIDTH;
    }

    public static void main(String[] args) {
        method1();
    }

    private static void method1() {
        System.out.println(Integer.toBinaryString(LEFT_MASK));
        System.out.println(Integer.toBinaryString(RIGHT_MASK));

        // 引用一个字节就是一个 char
        byte b = 0;
        int totalPos = WIDTH * WIDTH;

        // 先布局两个初始数
//        b = (byte)lSet(b, 1); // 00010000
//        System.out.println(Integer.toBinaryString(b));
//        b = (byte) rSet(b, 1); // 00010001
//        System.out.println(Integer.toBinaryString(b));

//        while (lGet(b) <= totalPos) {
//            while(rGet(b) <= totalPos) {
//                if (getColumn(lGet(b)) != getColumn(rGet(b))) {
//                    System.out.println("A = " + lGet(b) + ", B = " + rGet(b));
//                }
//                b = (byte)rSet(b, rGet(b) + 1);
//            }
//           b = (byte)lSet(b, lGet(b) + 1);
//            // 易错的点，while循环容易忘记重新充值 while 的内存，所以还是 for 循环好
//            b = (byte)rSet(b, 1);
//        }

        // 易错的点：从1开始，则需要等号结尾
        // 易错的点：对 byte 的重新赋值
        for (b = (byte) lSet(b, 1); lGet(b) <= totalPos; b = (byte) lSet(b, lGet(b) + 1)) {
            for (b = (byte) rSet(b, 1); rGet(b) <= totalPos; b = (byte) rSet(b, rGet(b) + 1)) {
                if (getColumn((lGet(b))) != getColumn((rGet(b)))) {
                    // 易错的点：容易把列和原始的数据搞错
                    System.out.println("A = " + lGet(b) + ", B = " + rGet(b));
                }
            }
        }
    }
}
