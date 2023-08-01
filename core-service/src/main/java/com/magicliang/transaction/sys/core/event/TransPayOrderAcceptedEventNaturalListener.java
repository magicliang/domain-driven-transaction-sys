package com.magicliang.transaction.sys.core.event;

import com.magicliang.transaction.sys.core.model.event.TransPayOrderAcceptedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: TransPayOrderAcceptedEvent 事件监听器
 *
 * @author magicliang
 *         <p>
 *         date: 2022-12-29 17:28
 */
@Slf4j
@Component
public class TransPayOrderAcceptedEventNaturalListener implements ApplicationListener<TransPayOrderAcceptedEvent> {


    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(final TransPayOrderAcceptedEvent event) {
        log.info("1 receive event: {}", event);
    }

}
