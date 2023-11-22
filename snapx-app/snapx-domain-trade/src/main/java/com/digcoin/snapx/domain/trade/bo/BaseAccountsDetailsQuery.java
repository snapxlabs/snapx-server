package com.digcoin.snapx.domain.trade.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@ParameterObject
@Data
public class BaseAccountsDetailsQuery extends Pageable {

    @Schema(hidden = true)
    private Long walletAccountsId;

    @Schema(hidden = true)
    private Boolean customerVisible;

    @Schema(description = "记账方向：收入 debit；支出 credit；")
    private BaseAccountsFinancialType direction;

}
