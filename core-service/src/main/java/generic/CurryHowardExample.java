package generic;

import java.util.function.Function;


/**
 * project name: domain-driven-transaction-sys
 *
 * description: Curry-Howard 对应（Curry-Howard Correspondence）
 *
 * @author magicliang
 *
 *         date: 2025-04-14 14:02
 */
public class CurryHowardExample {

    // 逻辑蕴含的实践：函数组合 (B → C) → (A → B) → (A → C)
    public static <A, B, C> Function<A, C> compose(
            Function<B, C> g,
            Function<A, B> f
    ) {
        return a -> g.apply(f.apply(a));
    }

    // 命题证明示例：(A → B) → (A → C) → (A → B ∧ C)
    public static <A, B, C> Function<A, Pair<B, C>> proveConjunction(
            Function<A, B> f,
            Function<A, C> g
    ) {
        return a -> new Pair<>(f.apply(a), g.apply(a));
    }

    // 处理联合类型 Either<A, B>
    public static String processEither(Either<Integer, String> either) {
        if (either instanceof Left) {
            Left<Integer, String> left = (Left<Integer, String>) either;
            return "Left value: " + left.a;
        } else if (either instanceof Right) {
            Right<Integer, String> right = (Right<Integer, String>) either;
            return "Right value: " + right.b;
        }
        return "Unknown";
    }

    public static void main(String[] args) {
        // 1. 验证逻辑与（Pair）
        Pair<String, Integer> pair = new Pair<>("Hello", 42);
        System.out.println("Pair: " + pair.a + ", " + pair.b); // Pair: Hello, 42

        // 2. 验证逻辑或（Either）
        Either<Integer, String> left = new Left<>(100);
        Either<Integer, String> right = new Right<>("World");
        System.out.println(processEither(left));  // Left value: 100
        System.out.println(processEither(right)); // Right value: World

        // 3. 验证逻辑蕴含（函数组合）
        Function<Integer, String> intToStr = Object::toString;
        Function<String, Integer> strLength = String::length;
        Function<Integer, Integer> composed = compose(strLength, intToStr);
        System.out.println(composed.apply(12345)); // 输出 5（"12345"的长度）

        // 4. 验证命题证明
        Function<String, Integer> proofF = String::length;
        Function<String, String> proofG = s -> s + "!";
        Function<String, Pair<Integer, String>> proof = proveConjunction(proofF, proofG);

        Pair<Integer, String> result = proof.apply("Test");
        System.out.println(result.a + ", " + result.b); // 输出 4, Test!
    }

    // 逻辑或（A ∨ B）的联合类型
    public interface Either<A, B> {

    }

    // 逻辑与（A ∧ B）的乘积类型
    public static class Pair<A, B> {

        public final A a;
        public final B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    public static class Left<A, B> implements Either<A, B> {

        public final A a;

        public Left(A a) {
            this.a = a;
        }
    }

    public static class Right<A, B> implements Either<A, B> {

        public final B b;

        public Right(B b) {
            this.b = b;
        }
    }
}
