package com.digcoin.snapx.domain.trade.event;

import java.io.Serializable;

public class BaseAccountsTransferInEvent extends AbstractBaseAccountsEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    public BaseAccountsTransferInEvent(Object source) {
        super(source);
    }
}
