package com.digcoin.snapx.server.admin.infra.converter;

import com.digcoin.snapx.domain.infra.entity.Notification;
import com.digcoin.snapx.server.admin.infra.dto.NotificationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationConverter {

    NotificationDTO intoDTO(Notification entity);

    Notification fromDTO(NotificationDTO dto);

}
