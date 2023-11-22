package com.digcoin.snapx.server.app.member.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.digcoin.snapx.core.common.enums.Operator;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.common.util.PasswordUtil;
import com.digcoin.snapx.core.common.util.TransactionUtil;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.camera.enums.CameraSource;
import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import com.digcoin.snapx.domain.camera.service.FilmMemberService;
import com.digcoin.snapx.domain.infra.service.AwsSnsMobilePushService;
import com.digcoin.snapx.domain.infra.service.InSiteMessageService;
import com.digcoin.snapx.domain.member.bo.MemberQuery;
import com.digcoin.snapx.domain.member.component.MemberNicknameModifyTimeCache;
import com.digcoin.snapx.domain.member.constant.MemberFlag;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.entity.MemberInvitation;
import com.digcoin.snapx.domain.member.error.MemberDomainError;
import com.digcoin.snapx.domain.member.service.MemberInvitationService;
import com.digcoin.snapx.domain.member.service.MemberPointsHistoryRankingService;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.system.constant.QrcodeType;
import com.digcoin.snapx.domain.system.entity.Qrcode;
import com.digcoin.snapx.domain.system.entity.SystemSetting;
import com.digcoin.snapx.domain.system.service.QrcodeService;
import com.digcoin.snapx.domain.system.service.SystemSettingService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.domain.trade.service.UsdcAccountsService;
import com.digcoin.snapx.server.app.camera.service.AppCameraMemberService;
import com.digcoin.snapx.server.app.member.config.MemberExPassProperties;
import com.digcoin.snapx.server.app.member.converter.CurrentUserConverter;
import com.digcoin.snapx.server.app.member.converter.MemberConverter;
import com.digcoin.snapx.server.app.member.converter.MemberProfileConverter;
import com.digcoin.snapx.server.app.member.converter.TokenInfoConverter;
import com.digcoin.snapx.server.app.member.dto.*;
import com.digcoin.snapx.server.app.member.error.MemberAppError;
import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.digcoin.snapx.server.base.infra.component.CaptchaManager;
import com.digcoin.snapx.server.base.infra.component.CaptchaManagerFactory;
import com.digcoin.snapx.server.base.infra.component.CaptchaType;
import com.digcoin.snapx.server.base.infra.dto.CaptchaDTO;
import com.digcoin.snapx.server.base.system.config.QrcodeProperties;
import com.digcoin.snapx.server.base.system.converter.QrcodeConverter;
import com.digcoin.snapx.server.base.system.dto.QrcodeDTO;
import com.digcoin.snapx.server.base.system.service.InviteCodeAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAppService {

    private final MemberService memberService;
    private final MemberConverter memberConverter;
    private final CurrentUserConverter currentUserConverter;
    private final TokenInfoConverter tokenInfoConverter;
    private final MemberProfileConverter memberProfileConverter;
    private final CaptchaManagerFactory captchaManagerFactory;
    private final QrcodeService qrcodeService;
    private final QrcodeConverter qrcodeConverter;
    private final QrcodeProperties qrcodeProperties;
    private final MemberInvitationService memberInvitationService;
    private final FilmMemberService filmMemberService;
    private final SystemSettingService systemSettingService;
    private final AppCameraMemberService appCameraMemberService;
    private final InviteCodeAppService inviteCodeAppService;
    private final MemberPointsHistoryRankingService memberPointsHistoryRankingService;
    private final MemberExPassProperties memberExPassProperties;
    private final UsdcAccountsService usdcAccountsService;
    private final PointsAccountsService pointsAccountsService;

    private final MemberNicknameModifyTimeCache memberNicknameModifyTimeCache;
    private final AwsSnsMobilePushService awsSnsMobilePushService;
    private final InSiteMessageService inSiteMessageService;

    public MemberDTO findMember(Long id) {
        MemberDTO memberDTO = memberConverter.intoDTO(memberService.findMember(id));
        memberDTO.setTotalUsdcCount(usdcAccountsService.getTotalDebit(id));
        memberDTO.setTotalPointCount(pointsAccountsService.getTotalDebit(id));
        return memberDTO;
    }

    public CaptchaDTO sendSignUpCode(CaptchaChannel captchaChannel, String receiver) {
        Member member = memberService.findMemberByAccount(receiver);
        if (Objects.nonNull(member)) {
            throw MemberDomainError.EMAIL_HAS_BEEN_BOUND.withDefaults();
        }
        return sendCaptcha(captchaChannel, CaptchaType.SIGN_UP, receiver);
    }

    public CaptchaDTO sendSignInCode(CaptchaChannel captchaChannel, String receiver) {
        Member member = memberService.findMemberByAccount(receiver);
        if (Objects.isNull(member)) {
            throw MemberAppError.EMAIL_NOT_REGISTERED.withDefaults();
        }
        if (Boolean.TRUE.equals(member.getVirtualUser())) {
            captchaChannel = CaptchaChannel.CONST;
        }
        return sendCaptcha(captchaChannel, CaptchaType.SIGN_IN, receiver);
    }

    public CaptchaDTO sendForgotPasswordCode(CaptchaChannel captchaChannel, String receiver) {
        Member member = memberService.findMemberByAccount(receiver);
        if (Objects.isNull(member)) {
            throw MemberAppError.EMAIL_NOT_REGISTERED.withDefaults();
        }
        return sendCaptcha(captchaChannel, CaptchaType.FORGOT_PASSWORD, receiver);
    }

    public CaptchaDTO sendModifyPasswordCode(CaptchaChannel captchaChannel, String account, String receiver) {
        if (!Objects.equals(account, receiver)) {
            throw MemberAppError.EMAIL_NOT_MATCH.withDefaults();
        }
        Member member = memberService.findMemberByAccount(account);
        if (Objects.isNull(member)) {
            throw MemberAppError.EMAIL_NOT_REGISTERED.withDefaults();
        }
        return sendCaptcha(captchaChannel, CaptchaType.MODIFY_PASSWORD, receiver);
    }

    public CaptchaDTO sendBindPhoneCode(CaptchaChannel captchaChannel, String areaCode, String receiver) {
        Member member = memberService.findMemberByPhone(areaCode, receiver);
        if (Objects.nonNull(member)) {
            throw MemberDomainError.PHONE_HAS_BEEN_BOUND.withDefaults();
        }
        return sendCaptcha(captchaChannel, CaptchaType.BIND_PHONE, areaCode+receiver);
    }

    public CaptchaDTO sendCaptcha(CaptchaChannel captchaChannel, CaptchaType captchaType, String receiver) {
        return captchaManagerFactory.get(captchaChannel).send(captchaType, receiver);
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthDTO createMember(MemberDTO member) {
        Qrcode qrcode = findQrcode(member.getInviteId());
        CaptchaManager captchaManager = captchaManagerFactory.get(member.getCaptchaChannel());
        if (!captchaManager.verify(CaptchaType.SIGN_UP, member.getAccount(), member.getCaptcha())) {
            throw CommonError.PARAMETER_ERROR.withMessage("verification code incorrect");
        } else {
            captchaManager.clean(CaptchaType.SIGN_UP, member.getAccount());
        }
        if (!Arrays.equals(member.getPasswordInput(), member.getPasswordConfirm())) {
            throw CommonError.PARAMETER_ERROR.withMessage("The two passwords are inconsistent");
        }

        String encryption = null;
        if (Objects.nonNull(member.getPasswordInput()) && member.getPasswordInput().length > 0) {
            encryption = PasswordUtil.getEncryption(member.getPasswordInput());
        }

        Member memberEntity = memberConverter.fromDTO(member);
        memberEntity.setPassword(encryption);
        Long memberId = memberService.createMember(memberEntity);

        if (Objects.nonNull(qrcode) && qrcodeService.updateQrcodeUsed(qrcode)) {
            log.info("new invite inviter:[{}] invitee:[{}] code:[{}]", qrcode.getMemberId(), memberId, member.getInviteId());
            memberInvitationService.createInvitation(qrcode.getMemberId(), memberId);
            memberService.updateMemberFlagOn(memberId, MemberFlag.LOGIN_FOR_FIRST_TIME | MemberFlag.INVITE_BONUS);
            sendNotify(qrcode.getMemberId(), memberId);
        } else {
            memberService.updateMemberFlagOn(memberId, MemberFlag.LOGIN_FOR_FIRST_TIME);
        }

        appCameraMemberService.createCameraMember(memberId, CameraSource.REGISTER_GIFT);
        SystemSetting systemSetting = systemSettingService.findSystemSetting();
        if (systemSetting.getIsRegisterGift()) {
            Long registerGiftCount = systemSetting.getRegisterGiftCount();
            filmMemberService.saveOrUpdateFilmMember(memberId, registerGiftCount, FilmChangeType.REGISTER_GIFT);
        }

        return createMemberToken(memberService.findMember(memberId));
    }

    private void sendNotify(Long memberId, Long inviteeMemberId) {
        try {
            inSiteMessageService.sendFriendSignedUp(memberId, inviteeMemberId);
        } catch (Exception e) {
            log.error("sendNotify memberId:[{}]", memberId, e);
        }
    }

    private Qrcode findQrcode(Long inviteId) {
        SystemSetting systemSetting = systemSettingService.findSystemSetting();
        Boolean inviteCodeRequired = Optional.ofNullable(systemSetting.getInviteCodeRequired()).orElse(Boolean.FALSE);
        if (Objects.isNull(inviteId)) {
            if (inviteCodeRequired) {
                throw MemberAppError.INVITE_CODE_REQUIRED.withDefaults();
            }
            return null;
        }

        Qrcode qrcode = qrcodeService.findQrcode(inviteId);
        if (Objects.isNull(qrcode)) {
            if (inviteCodeRequired) {
                throw MemberAppError.INVITE_CODE_NOT_EXIST.withDefaults();
            }
            return null;
        }

        return qrcode;
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthDTO signIn(String account, String password) {
        Member member = memberService.findMemberByAccount(account);
        if (Objects.isNull(member)) {
            throw MemberAppError.ACCOUNT_OR_PASSWORD_INCORRECT.withDefaults();
        }
        if (StringUtils.isBlank(password) || !PasswordUtil.match(member.getPassword(), password.getBytes(Charset.defaultCharset()))) {
            throw MemberAppError.ACCOUNT_OR_PASSWORD_INCORRECT.withDefaults();
        }
        return createMemberToken(member);
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthDTO signIn(String account, CaptchaChannel captchaChannel, String captcha) {
        Member member = memberService.findMemberByAccount(account);
        if (Objects.isNull(member)) {
            throw MemberAppError.ACCOUNT_OR_PASSWORD_INCORRECT.withDefaults();
        }
        CaptchaManager captchaManager = captchaManagerFactory.get(captchaChannel);
        if (!isExPassMember(account, captcha)
                && !captchaManager.verify(CaptchaType.SIGN_IN, account, captcha)) {
            throw MemberAppError.ACCOUNT_OR_PASSWORD_INCORRECT.withDefaults();
        } else {
            captchaManager.clean(CaptchaType.SIGN_IN, account);
        }
        return createMemberToken(member);
    }

    @Transactional(rollbackFor = Exception.class)
    public void issueBonus(AuthDTO token) {
        // 发送胶卷奖励
        Long memberId = token.getMember().getId();
        if (memberService.isInviteBonus(memberId)) {
            Optional<MemberInvitation> memberInvitation = Optional.ofNullable(memberInvitationService.findMemberInvitationByInvitee(memberId));
            SystemSetting systemSetting = systemSettingService.findSystemSetting();
            if (systemSetting.getIsInviteGift()) {
                Long inviterGiftCount = systemSetting.getInviteGiftCount();
                Long inviteeGiftCount = systemSetting.getBeInvitedGiftCount();
                filmMemberService.saveOrUpdateFilmMember(memberId, inviteeGiftCount, FilmChangeType.BE_INVITED_GIFT);
                if (memberInvitation.isPresent()) {
                    filmMemberService.saveOrUpdateFilmMember(memberInvitation.get().getInviterMemberId(), inviterGiftCount, FilmChangeType.INVITE_GIFT);
                    memberInvitationService.updateInvitationGifCount(
                            memberInvitation.get().getInviterMemberId(),
                            memberInvitation.get().getInviteeMemberId(),
                            inviterGiftCount,
                            inviteeGiftCount);
                }

            }
            memberService.updateMemberFlagOff(memberId, MemberFlag.INVITE_BONUS);
        }

        if (memberService.isLoginForFirstTime(memberId)) {
            memberService.updateMemberFlagOff(memberId, MemberFlag.LOGIN_FOR_FIRST_TIME);
            token.setLoginForFirstTime(true);
        }
    }

    public void logout() {
        StpUtil.logout();
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthDTO updateMemberPassword(MemberPasswordDTO memberPasswordDTO) {
        if (StringUtils.isBlank(memberPasswordDTO.getAccount())) {
            throw CommonError.PARAMETER_ERROR.withMessage("account is blank");
        }
        if (Objects.isNull(memberPasswordDTO.getMemberId())) {
            Member member = memberService.findMemberByAccount(memberPasswordDTO.getAccount());
            if (Objects.isNull(member)) {
                throw MemberAppError.EMAIL_NOT_REGISTERED.withDefaults();
            }
            memberPasswordDTO.setMemberId(member.getId());
        }
        CaptchaManager captchaManager = captchaManagerFactory.get(memberPasswordDTO.getCaptchaChannel());
        if (!captchaManager.verify(memberPasswordDTO.getCaptchaType(), memberPasswordDTO.getAccount(), memberPasswordDTO.getCaptcha())) {
            throw CommonError.PARAMETER_ERROR.withMessage("verification code incorrect");
        } else {
            captchaManager.clean(memberPasswordDTO.getCaptchaType(), memberPasswordDTO.getAccount());
        }
        if (Objects.isNull(memberPasswordDTO.getPasswordInput()) || memberPasswordDTO.getPasswordInput().length == 0) {
            throw CommonError.PARAMETER_ERROR.withMessage("passwords invalidate");
        }
        if (!Arrays.equals(memberPasswordDTO.getPasswordInput(), memberPasswordDTO.getPasswordConfirm())) {
            throw CommonError.PARAMETER_ERROR.withMessage("the two passwords are inconsistent");
        }
        String encryption = PasswordUtil.getEncryption(memberPasswordDTO.getPasswordInput());
        Member member = new Member();
        member.setId(memberPasswordDTO.getMemberId());
        member.setPassword(encryption);
        memberService.updateMember(member);

        StpUtil.logout();
        member = memberService.findMember(memberPasswordDTO.getMemberId());

        return createMemberToken(member);
    }

    @Transactional(rollbackFor = Exception.class)
    public MemberDTO updateMemberProfile(MemberProfileDTO memberProfileDTO) {
        Member member = memberService.findMember(memberProfileDTO.getMemberId());
        if (Objects.isNull(member)) {
            throw MemberAppError.MEMBER_NOT_EXIST.withDefaults();
        }

        // 判断是否修改昵称，检查用户上次修改昵称时间距离当前时间是否满30天，未满30天抛错
        if (StringUtils.isNotBlank(memberProfileDTO.getNickname())
                && !Objects.equals(member.getNickname(), memberProfileDTO.getNickname())) {
            LocalDateTime lastModifyTime = memberNicknameModifyTimeCache.getLastModifyTime(memberProfileDTO.getMemberId());
            if (Objects.nonNull(lastModifyTime)) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expired = lastModifyTime.plusDays(30);
                if (expired.isAfter(now)) {
                    Duration between = Duration.between(now, expired);
                    throw MemberAppError.CAN_NOT_UPDATE_NICKNAME.withDefaults(between.toDays() + 1);
                }
            }
            TransactionUtil.doAfterTransactionCommit(() -> {
                memberNicknameModifyTimeCache.updateModifyTime(memberProfileDTO.getMemberId(), LocalDateTime.now());
            });
        }

        member = memberProfileConverter.fromDTO(memberProfileDTO);
        memberService.updateMember(member);
        return findMember(memberProfileDTO.getMemberId());
    }

    public void bindPhone(MemberPhoneDTO memberPhoneDTO) {
        CaptchaManager captchaManager = captchaManagerFactory.get(memberPhoneDTO.getCaptchaChannel());
        if (!captchaManager.verify(CaptchaType.BIND_PHONE, memberPhoneDTO.getAreaCode()+memberPhoneDTO.getPhone(), memberPhoneDTO.getCaptcha())) {
            throw CommonError.PARAMETER_ERROR.withMessage("verification code incorrect");
        } else {
            captchaManager.clean(CaptchaType.BIND_PHONE, memberPhoneDTO.getAreaCode()+memberPhoneDTO.getPhone());
        }
        Member member = memberService.findMemberByPhone(memberPhoneDTO.getAreaCode(), memberPhoneDTO.getPhone());
        if (Objects.nonNull(member)) {
            throw MemberDomainError.PHONE_HAS_BEEN_BOUND.withDefaults();
        }
        Member currentUser = memberService.findMember(memberPhoneDTO.getMemberId());
        if (Objects.isNull(currentUser)) {
            throw MemberAppError.MEMBER_NOT_EXIST.withDefaults();
        }
        if (StringUtils.isNotBlank(currentUser.getPhone())
                && (!Objects.equals(currentUser.getPhoneAreaCode(), memberPhoneDTO.getAreaCode())
                || !Objects.equals(currentUser.getPhone(), memberPhoneDTO.getPhone()))) {
            throw MemberAppError.CAN_NOT_BIND_PHONE.withDefaults();
        }
        Member example = new Member();
        example.setId(memberPhoneDTO.getMemberId());
        example.setPhoneAreaCode(memberPhoneDTO.getAreaCode());
        example.setPhone(memberPhoneDTO.getPhone());
        memberService.updateMember(example);
    }

    public QrcodeDTO createInviteQrcode(Long memberId) {
        // 邀请码未被使用之前一直返回同一个邀请码
        QrcodeDTO latestUnusedQrCode = inviteCodeAppService.getLatestUnusedInviteCode(memberId);
        if (Objects.nonNull(latestUnusedQrCode)) {
            return latestUnusedQrCode;
        }
        return inviteCodeAppService.createInviteCode(Operator.MEMBER, memberId);
    }

    public void validateInviteCode(Long codeId) {
        QrcodeDTO inviteCode = inviteCodeAppService.findInviteCode(codeId);
        if (Objects.isNull(inviteCode)) {
            throw MemberAppError.INVITE_CODE_NOT_EXIST.withDefaults();
        }
        if (inviteCode.getUseLimit() > 0 && inviteCode.getUsedCount() >= inviteCode.getUseLimit()) {
            throw MemberAppError.INVITE_CODE_USED.withDefaults();
        }
    }

    public QrcodeDTO createShareQrcode() {
        // 邀请码未被使用之前一直返回同一个分享二维码
        Qrcode latestUnusedQrCode = qrcodeService.getLatestUnusedQrCode(0L, QrcodeType.SHARE);
        if (Objects.nonNull(latestUnusedQrCode)) {
            return qrcodeConverter.intoDTO(latestUnusedQrCode);
        }
        QrcodeProperties.CodeDescription inviteProp = qrcodeProperties.getInvite();
        Qrcode entity = new Qrcode();
        entity.setMemberId(0L);
        entity.setOperator(Operator.SYSTEM);
        entity.setCodeType(QrcodeType.SHARE);
        entity.setContent(inviteProp.getUrl());
        entity.setWidth(inviteProp.getWidth());
        entity.setImageType(inviteProp.getImageType());
        Long id = qrcodeService.createQrcode(entity);
        Qrcode qrcode = qrcodeService.findQrcode(id);
        return qrcodeConverter.intoDTO(qrcode);
    }

    public byte[] getInviteQrcode(Long qrcodeId) {
        Qrcode qrcode = qrcodeService.findQrcode(qrcodeId);
        if (StringUtils.isBlank(qrcode.getCodeBase64())) {
            qrcodeService.updateQrcodeBase64(qrcode);
        }
        return qrcodeService.decodeBase64(qrcode);
    }

    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
        memberPointsHistoryRankingService.remove(LocalDate.now(), memberId);
    }

    public void validCaptcha(CaptchaValidateDTO captchaValidateDTO) {

        CaptchaManager captchaManager = captchaManagerFactory.get(captchaValidateDTO.getCaptchaChannel());
        if (!captchaManager.verify(captchaValidateDTO.getCaptchaType(), captchaValidateDTO.getAccount(), captchaValidateDTO.getCaptcha())) {
            throw CommonError.PARAMETER_ERROR.withMessage("verification code incorrect");
        }

    }

    public MemberInvitationDTO pageInviteeMember(MemberInvitationQuery query) {
        MemberInvitationDTO result = new MemberInvitationDTO();
        MemberInvitation invitation = memberInvitationService.findMemberInvitationByInvitee(query.getInviterMemberId());
        if (Objects.nonNull(invitation)) {
            Member member = memberService.findMember(invitation.getInviterMemberId());
            if (Objects.nonNull(member)) {
                result.setInviter(memberConverter.intoDTO(member));
            }
        }
        List<MemberInvitation> invitationList = memberInvitationService.getInvitationByInviterMemberIds(query.getInviterMemberId());
        if (CollectionUtils.isEmpty(invitationList)) {
            result.setInvited(PageResult.emptyResult());
            return result;
        }
        Map<Long, MemberInvitation> invitationMap = invitationList.stream()
                .collect(Collectors.toMap(MemberInvitation::getInviteeMemberId, Function.identity()));

        MemberQuery memberQuery = query.toMemberQuery();
        memberQuery.setMemberIds(invitationMap.keySet());
        result.setInvited(PageResult.fromPageResult(memberService.pageMember(memberQuery), memberConverter::intoDTO));
        return result;
    }

    public void updateMemberTimezone(Long memberId, Integer timezone) {
        Member example = new Member();
        example.setId(memberId);
        example.setTimezone(timezone);
        memberService.updateMember(example);
    }

    private AuthDTO createMemberToken(Member member) {
        CurrentUser currentUser = currentUserConverter.fromMember(member);
        StpUtil.login(member.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        SaSession session = StpUtil.getSession();
        session.set(CurrentUser.IDENTITY, currentUser);

        AuthDTO result = new AuthDTO();
        result.setMember(memberConverter.intoDTO(member));
        result.setToken(tokenInfoConverter.intoDTO(tokenInfo));
        result.setLoginForFirstTime(false);
        return result;
    }

    private boolean isExPassMember(String account, String captcha) {
        List<String> accounts = memberExPassProperties.getAccounts();
        String passCode = memberExPassProperties.getPassCode();
        return accounts.contains(account) && passCode.equals(captcha);
    }

}
