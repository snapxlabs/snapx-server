package com.digcoin.snapx.domain.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import org.apache.ibatis.annotations.Param;

public interface BaseAccountsMapper extends BaseMapper<BaseAccounts> {

    int updateAccountsBalance(@Param("baseAccountsId") Long baseAccountsId,
                              @Param("balanceAfter") Long balanceAfter,
                              @Param("balanceBefore") Long balanceBefore);

    int updateAccountsTotalDebit(@Param("baseAccountsId") Long baseAccountsId,
                                 @Param("totalDebitAfter") Long totalDebitAfter,
                                 @Param("totalDebitBefore") Long totalDebitBefore);

    int updateAccountsTotalCredit(@Param("baseAccountsId") Long baseAccountsId,
                                  @Param("totalCreditAfter") Long totalCreditAfter,
                                  @Param("totalCreditBefore") Long totalCreditBefore);

}
