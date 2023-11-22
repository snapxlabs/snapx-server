package com.digcoin.snapx.domain.camera.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digcoin.snapx.domain.camera.entity.FilmMemberDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 14:32
 * @description
 */
@Mapper
public interface FilmMemberDetailMapper extends BaseMapper<FilmMemberDetail> {


    @Select("select sum(variable_quantity) from cam_film_member_detail " +
            "where member_id = #{memberId} and variable_quantity > 0")
    Long getTotalQuantity(@Param("memberId")Long memberId);

}
