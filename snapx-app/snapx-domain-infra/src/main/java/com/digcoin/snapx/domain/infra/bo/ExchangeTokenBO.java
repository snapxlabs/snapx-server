package com.digcoin.snapx.domain.infra.bo;

import com.digcoin.snapx.domain.infra.component.discord.model.TokensResponse;
import com.digcoin.snapx.domain.infra.component.discord.model.User;
import lombok.Data;

@Data
public class ExchangeTokenBO {

    private TokensResponse tokens;

    private User user;

}
