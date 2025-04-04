package generic.aireport;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 测试状态
 *
 * @author magicliang
 *         <p>
 *         date: 2022-08-05 15:26
 */
@Getter
@AllArgsConstructor
public enum PlaneStatus {
    /**
     *
     */
    Flying(1),

    /**
     *
     */
    Landed(2),

    ;

    private final int code;
}
