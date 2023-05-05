package com.magicliang.transaction.sys.common.util;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 自定义异常工具类
 *
 * @author magicliang
 * <p>
 * date: 2023-05-05 19:03
 */
public class CustomExceptionUtil {

    public static void main(String[] args) {
        final IllegalArgumentException a = new IllegalArgumentException("123");
        final RuntimeException b = new RuntimeException(a);
        final RuntimeException c = new RuntimeException(b);

        Throwable throwable = unwrapAndFindException(c, IllegalArgumentException.class);
        System.out.println(throwable);

        throwable = unwrapAndFindException(c, RuntimeException.class);
        System.out.println(throwable);
    }

    public static Throwable unwrapAndFindException(Throwable throwable, Class<? extends Throwable> toFind) {
        if (throwable == null || toFind.isAssignableFrom(throwable.getClass())) {
            return throwable;
        } else {
            Throwable cause = throwable.getCause();
            if (cause == null) {
                return throwable;
            }
            return unwrapAndFindException(cause, toFind);
        }
    }
}
