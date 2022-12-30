package com.magicliang.transaction.sys.core.model.event;

import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.shared.DomainEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单被受理事件
 *
 * @author magicliang
 * <p>
 * date: 2022-12-29 15:09
 */
@Getter
public class TransPayOrderAcceptedEvent extends ApplicationEvent implements DomainEvent<TransPayOrderAcceptedEvent> {

    /**
     * 模仿 EventObject，保留一个 source，这样就可以不用扩展自 ApplicationEvent，利用 Spring 4 以后的特性，使用普通对象作为事件体
     */
    protected transient Object source;

    private TransPayOrderEntity payOrderEntitySnapShot;

    public static TransPayOrderAcceptedEvent create(Object source, final TransPayOrderEntity payOrderEntitySnapShot) {
        return new TransPayOrderAcceptedEvent(source, payOrderEntitySnapShot);
    }

    protected TransPayOrderAcceptedEvent(Object source, final TransPayOrderEntity payOrderEntitySnapShot) {
        super(source);
        this.source = source;
        // 做一个快照拷贝
        if (payOrderEntitySnapShot != null) {
            this.payOrderEntitySnapShot = payOrderEntitySnapShot.shallowCopy();
        }
    }

    /**
     * @param other The other domain event.
     * @return <code>true</code> if the given domain event and this event are regarded as being the same event.
     */
    @Override
    public boolean sameEventAs(final TransPayOrderAcceptedEvent other) {
        if (null == this.payOrderEntitySnapShot || null == other.payOrderEntitySnapShot) {
            return false;
        }
        return Objects.equals(this.payOrderEntitySnapShot.getPayOrderNo(),
                other.payOrderEntitySnapShot.getPayOrderNo());
    }
}
