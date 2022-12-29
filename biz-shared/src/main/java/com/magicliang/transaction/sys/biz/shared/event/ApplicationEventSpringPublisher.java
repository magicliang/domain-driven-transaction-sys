package com.magicliang.transaction.sys.biz.shared.event;

import com.magicliang.transaction.sys.core.model.event.TransPayOrderAcceptedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-12-29 15:23
 */
@Component
public class ApplicationEventSpringPublisher implements ApplicationEvents {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ApplicationEventSpringPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void transPayOrderAccepted(final TransPayOrderAcceptedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
