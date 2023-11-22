package com.digcoin.snapx.server.admin.member.service;

import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.camera.bo.CameraQueryBO;
import com.digcoin.snapx.domain.camera.entity.Camera;
import com.digcoin.snapx.domain.camera.entity.CameraMember;
import com.digcoin.snapx.domain.camera.enums.CameraSource;
import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import com.digcoin.snapx.domain.camera.service.CameraMemberService;
import com.digcoin.snapx.domain.camera.service.CameraService;
import com.digcoin.snapx.domain.camera.service.FilmMemberService;
import com.digcoin.snapx.domain.member.bo.InviteeMemberQuery;
import com.digcoin.snapx.domain.member.bo.MemberEventQuery;
import com.digcoin.snapx.domain.member.bo.MemberQuery;
import com.digcoin.snapx.domain.member.constant.MemberEventConst;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.entity.MemberInvitation;
import com.digcoin.snapx.domain.member.service.MemberEventAggregationService;
import com.digcoin.snapx.domain.member.service.MemberInvitationService;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.system.entity.SystemSetting;
import com.digcoin.snapx.domain.system.service.SystemSettingService;
import com.digcoin.snapx.server.admin.member.converter.MemberConverter;
import com.digcoin.snapx.server.admin.member.converter.MemberEventConverter;
import com.digcoin.snapx.server.admin.member.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAppService {

    private final MemberEventAggregationService memberEventAggregationService;
    private final MemberInvitationService memberInvitationService;
    private final MemberService memberService;
    private final MemberConverter memberConverter;
    private final MemberEventConverter memberEventConverter;
    private final FilmMemberService filmMemberService;
    private final SystemSettingService systemSettingService;
    private final CameraService cameraService;
    private final CameraMemberService cameraMemberService;

    public MemberEventStatisticsDTO getMemberStatistics(LocalDateTime currentTime) {
        Long dailyAccess = memberEventAggregationService.getEventSumDaily(MemberEventConst.MEMBER_ACCESS, currentTime);
        Long recentDailyAccess = memberEventAggregationService.getEventSumRecentDaily(MemberEventConst.MEMBER_ACCESS, currentTime);
        Long monthAccess = memberEventAggregationService.getEventSumMonth(MemberEventConst.MEMBER_MONTH_ACCESS, currentTime);
        Long recentMonthAccess = memberEventAggregationService.getEventSumRecentMonth(MemberEventConst.MEMBER_MONTH_ACCESS, currentTime);

        Long dailyVisit = memberEventAggregationService.getEventSumDaily(MemberEventConst.MEMBER_VISIT, currentTime);
        Long recentDailyVisit = memberEventAggregationService.getEventSumRecentDaily(MemberEventConst.MEMBER_VISIT, currentTime);
        Long monthVisit = memberEventAggregationService.getEventSumMonth(MemberEventConst.MEMBER_VISIT, currentTime);
        Long recentMonthVisit = memberEventAggregationService.getEventSumRecentMonth(MemberEventConst.MEMBER_VISIT, currentTime);

        Long totalVisit = memberEventAggregationService.getEventSum(MemberEventConst.MEMBER_VISIT);

        Long dailySignUp = memberEventAggregationService.getEventSumDaily(MemberEventConst.SIGN_UP, currentTime);
        Long recentDailySignUp = memberEventAggregationService.getEventSumRecentDaily(MemberEventConst.SIGN_UP, currentTime);
        Long monthSignUp = memberEventAggregationService.getEventSumMonth(MemberEventConst.SIGN_UP, currentTime);
        Long recentMonthSignUp = memberEventAggregationService.getEventSumRecentMonth(MemberEventConst.SIGN_UP, currentTime);

        MemberEventStatisticsDTO result = new MemberEventStatisticsDTO();
        result.setDailyAccess(dailyAccess);
        result.setRecentDailyAccess(recentDailyAccess);
        result.setMonthAccess(monthAccess);
        result.setRecentMonthAccess(recentMonthAccess);

        result.setDailyVisit(dailyVisit);
        result.setRecentDailyVisit(recentDailyVisit);
        result.setMonthVisit(monthVisit);
        result.setRecentMonthVisit(recentMonthVisit);

        result.setTotalVisit(totalVisit);

        result.setDailySignUp(dailySignUp);
        result.setRecentDailySignUp(recentDailySignUp);
        result.setMonthSignUp(monthSignUp);
        result.setRecentMonthSignUp(recentMonthSignUp);

        return result;
    }

    public PageResult<MemberEventDTO> pageEventPerMonth(MemberEventQuery query) {
        return PageResult.fromPageResult(memberEventAggregationService.pageEventPerMonth(query), memberEventConverter::intoDTO);
    }

    public PageResult<MemberEventDTO> pageEventPerDay(MemberEventQuery query) {
        return PageResult.fromPageResult(memberEventAggregationService.pageEventPerDay(query), memberEventConverter::intoDTO);
    }

    public PageResult<MemberEventDTO> pageEventPerHour(MemberEventQuery query) {
        return PageResult.fromPageResult(memberEventAggregationService.pageEventPerHour(query), memberEventConverter::intoDTO);
    }

    public PageResult<MemberDTO> pageMember(MemberQuery query) {
        PageResult<Member> pageResult = memberService.pageMember(query);
        Set<Long> memberIds = pageResult.getData().stream().map(Member::getId).collect(Collectors.toSet());
        Map<Long, Long> inviteeCountMap = memberInvitationService.getInviteeCountMap(memberIds);
        Map<Long, Member> inviterMemberMap = memberInvitationService.mappingInviterMember(memberIds);
        Function<Member, MemberDTO> intoDTO = member -> {
            MemberDTO dto = memberConverter.intoDTO(member);
            dto.setInviteeCount(0L);
            Optional.ofNullable(inviteeCountMap.get(member.getId())).ifPresent(dto::setInviteeCount);

            Member inviterMember = inviterMemberMap.get(member.getId());
            if (Objects.nonNull(inviterMember)) {
                dto.setInviterMember(memberConverter.intoDTO(inviterMember));
            }

            return dto;
        };
        return PageResult.fromPageResult(pageResult, intoDTO);
    }

    public PageResult<InviteeMemberDTO> pageInviteeMember(InviteeMemberQuery query) {
        List<MemberInvitation> invitationList = memberInvitationService.getInvitationByInviterMemberIds(query.getInviterMemberId());
        if (CollectionUtils.isEmpty(invitationList)) {
            return PageResult.emptyResult();
        }
        Map<Long, MemberInvitation> invitationMap = invitationList.stream()
                .collect(Collectors.toMap(MemberInvitation::getInviteeMemberId, Function.identity()));

        MemberQuery memberQuery = query.toMemberQuery();
        memberQuery.setMemberIds(invitationMap.keySet());
        PageResult<MemberDTO> memberPageResult = pageMember(memberQuery);
        return PageResult.fromPageResult(memberPageResult, memberDTO -> {
            InviteeMemberDTO inviteeMemberDTO = new InviteeMemberDTO();
            inviteeMemberDTO.setMember(memberDTO);
            Optional.ofNullable(invitationMap.get(memberDTO.getId())).ifPresent(invitation -> {
                inviteeMemberDTO.setInviterGiftCount(invitation.getInviterGiftCount());
                inviteeMemberDTO.setInviteeGiftCount(invitation.getInviteeGiftCount());
            });
            return inviteeMemberDTO;
        });
    }

    public MemberDTO findMember(Long id) {
        return memberConverter.intoDTO(memberService.findMember(id));
    }

    public void updateMemberFreeze(Long id, MemberFreezeDTO memberFreeze) {
        Member member = memberService.findMember(id);
        if (Objects.isNull(member)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("Member not exist");
        }
        Member example = new Member();
        example.setId(id);
        example.setFreeze(memberFreeze.getFreeze());
        example.setFreezeReason(memberFreeze.getFreezeReason());
        memberService.updateMember(example);
    }

    public Long createMember(MemberDTO member) {
        Member entity = memberConverter.fromMember(member);
        Long memberId = memberService.createMember(entity);
        createCameraMember(memberId, CameraSource.REGISTER_GIFT);
        SystemSetting systemSetting = systemSettingService.findSystemSetting();
        if (systemSetting.getIsRegisterGift()) {
            Long registerGiftCount = systemSetting.getRegisterGiftCount();
            filmMemberService.saveOrUpdateFilmMember(memberId, registerGiftCount, FilmChangeType.REGISTER_GIFT);
        }
        return memberId;
    }

    public void updateMember(Long id, MemberDTO member) {
        Member entity = memberConverter.fromMember(member);
        entity.setId(id);
        memberService.updateMember(entity);
    }

    private void createCameraMember(Long memberId, CameraSource cameraSource) {
        List<Camera> cameras = cameraService.listCamera(new CameraQueryBO()).stream()
                .filter(camera -> Boolean.TRUE.equals(camera.getIsGift())).collect(Collectors.toList());
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(cameras)) {
            return;
        }
        List<CameraMember> cameraMembers = cameras.stream().map(camera -> {
            CameraMember cameraMember = new CameraMember();
            cameraMember.setCameraId(camera.getId());
            cameraMember.setMemberId(memberId);
            cameraMember.setCameraSource(cameraSource);
            return cameraMember;
        }).collect(Collectors.toList());
        cameraMemberService.batchSaveCameraMember(cameraMembers);
    }
}
