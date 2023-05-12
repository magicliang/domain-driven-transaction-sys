package com.magicliang.transaction.sys.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String input = "Field error in object 'leadsQueryConditionForGetRequest' on field 'hideRepeatRegisterLeads': rejected value [1235]; codes [typeMismatch.leadsQueryConditionForGetRequest.hideRepeatRegisterLeads,typeMismatch.hideRepeatRegisterLeads,typeMismatch.boolean,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [leadsQueryConditionForGetRequest.hideRepeatRegisterLeads,hideRepeatRegisterLeads]; arguments []; default message [hideRepeatRegisterLeads]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'boolean' for property 'hideRepeatRegisterLeads'; nested exception is java.lang.IllegalArgumentException: Invalid boolean value [1235]]";
        Pattern pattern = Pattern.compile("field (.*?);");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String captured = matcher.group(1);
            System.out.println(captured);  // 输出：zzz
        } else {
            System.out.println("No match found.");
        }

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
