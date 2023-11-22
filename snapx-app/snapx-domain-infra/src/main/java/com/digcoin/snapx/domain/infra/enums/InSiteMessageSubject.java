package com.digcoin.snapx.domain.infra.enums;

import lombok.Getter;

/**
 * 站内信主题枚举：点赞 THUMBS_UP；NEW_COMMENT；邀请码被使用 FRIEND_SIGNED_UP
 */
@Getter
public enum InSiteMessageSubject {
    THUMBS_UP("Thumbs up", "@%s liked your post."),
    NEW_COMMENT("New Comment", "@%s commented on your post."),
    FRIEND_SIGNED_UP("friend signed up", "A friend has just signed up using your invitation code. Well done Snapper!"),
    BATCH_NOTIFICATION("", "");

    private String title;
    private String template;

    InSiteMessageSubject(String title, String template) {
        this.title = title;
        this.template = template;
    }

    public String getContext(String... params) {
        return String.format(this.template, params);
    }
}

