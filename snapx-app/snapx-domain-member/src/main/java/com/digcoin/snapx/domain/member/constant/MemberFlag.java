package com.digcoin.snapx.domain.member.constant;

public class MemberFlag {

    /**
     * 被邀请用户有待领取的胶卷奖励
     */
    public static final long INVITE_BONUS = 1 << 0;

    /**
     * 是否第一次登录系统
     */
    public static final long LOGIN_FOR_FIRST_TIME = 1 << 1;

    /**
     * 是否已发送2天离开通知
     */
    public static final long NOTIFY_TWO_DAYS_LEAVE = 1 << 2;

    /**
     * 是否已发送steak即将丧失通知
     */
    public static final long NOTIFY_STEAK_LOST = 1 << 3;

}
