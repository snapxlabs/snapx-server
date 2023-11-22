package com.digcoin.snapx.domain.trade.bo;

import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsVoucherType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BaseAccountsTransferDetail {

    /**
     * 科目
     * @see BaseAccountsFinancialSubject
     */
    private String subject;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 凭证
     */
    private String voucher;

    /**
     * 凭证类型
     * @see BaseAccountsVoucherType
     */
    private String voucherType;

    /**
     * 明细客户端是否可见
     */
    private Map<String, Boolean> customerVisibleMap = new HashMap<>();

}
