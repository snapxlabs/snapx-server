package com.digcoin.snapx.domain.infra.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

import java.util.Collection;

@Data
@ParameterObject
public class PhoneAreaCodeQuery extends Pageable {

    @Schema(description = "检索词")
    private String search;

    @Schema(description = "国家地区代码集合", hidden = true)
    private Collection<String> countryCodes;

}
