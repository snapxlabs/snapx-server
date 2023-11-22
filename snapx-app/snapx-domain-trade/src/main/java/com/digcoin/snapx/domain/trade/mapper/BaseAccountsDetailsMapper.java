package com.digcoin.snapx.domain.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digcoin.snapx.domain.trade.bo.BaseAccountRecentDayRanking;
import com.digcoin.snapx.domain.trade.bo.BaseAmountSumByDatetime;
import com.digcoin.snapx.domain.trade.bo.BaseAmountSumByDatetimeParameter;
import com.digcoin.snapx.domain.trade.bo.BaseAmountSumParameter;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BaseAccountsDetailsMapper extends BaseMapper<BaseAccountsDetails> {

    Long getAmountSum(@Param("p") BaseAmountSumParameter baseAmountSumParameter);

    List<BaseAmountSumByDatetime> getAmountSumByDatetime(@Param("p") BaseAmountSumByDatetimeParameter baseAmountSumByDatetimeParameter);

    Long getTotalAmountSum(@Param("p") BaseAmountSumByDatetimeParameter baseAmountSumByDatetimeParameter);

    List<BaseAccountRecentDayRanking> pageRecentDayRanking(@Param("offset") Long offset,
                                                           @Param("limit") Long limit,
                                                           @Param("startTime") LocalDateTime startTime,
                                                           @Param("accountsType") BaseAccountsType accountsType);
}
