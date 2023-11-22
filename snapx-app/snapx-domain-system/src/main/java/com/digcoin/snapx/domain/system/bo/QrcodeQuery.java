package com.digcoin.snapx.domain.system.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@Data
@ParameterObject
public class QrcodeQuery extends Pageable {

    @Schema(hidden = true)
    private String codeType;

}
