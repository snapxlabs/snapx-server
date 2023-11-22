package com.digcoin.snapx.server.app.restaurant.converter;

import com.digcoin.snapx.domain.restaurant.entity.JournalDaily;
import com.digcoin.snapx.server.app.restaurant.vo.JournalPageDailyJournalsVO;
import org.mapstruct.Mapper;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/2 16:53
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppJournalConverter extends CustomConverter {

    JournalPageDailyJournalsVO journalDaily2JournalPageDailyJournalsVO(JournalDaily item);

}
