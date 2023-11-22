package com.digcoin.snapx.domain.trade.event;

import java.io.Serializable;

public class BaseAccountsTransferOutEvent extends AbstractBaseAccountsEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    public BaseAccountsTransferOutEvent(Object source) {
        super(source);
    }
}
