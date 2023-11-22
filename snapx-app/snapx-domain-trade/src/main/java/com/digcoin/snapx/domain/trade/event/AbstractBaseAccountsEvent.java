package com.digcoin.snapx.domain.trade.event;

import org.springframework.context.ApplicationEvent;

public class AbstractBaseAccountsEvent extends ApplicationEvent {

    protected final Long baseAccountsDetailsId;

    public AbstractBaseAccountsEvent(Object source) {
        super(source);
        this.baseAccountsDetailsId = Long.valueOf(String.valueOf(source));
    }

    public Long getBaseAccountsDetailsId() {
        return baseAccountsDetailsId;
    }
}
