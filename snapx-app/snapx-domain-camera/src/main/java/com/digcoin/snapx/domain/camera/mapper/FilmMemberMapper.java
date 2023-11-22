package com.digcoin.snapx.domain.camera.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.digcoin.snapx.domain.camera.entity.FilmMember;
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
public interface FilmMemberMapper extends BaseMapper<FilmMember> {

    @Select("SELECT cfm.*, mm.account AS memberAccount, " +
            "mm.nickname AS memberNickName FROM `cam_film_member` AS cfm " +
            "LEFT JOIN `mem_member` AS mm ON mm.id = cfm.member_id " +
            "${ew.customSqlSegment}")
    IPage<FilmMember> pageFilmMember(IPage<FilmMember> page,@Param(Constants.WRAPPER) QueryWrapper<FilmMember> queryWrapper);
}
