package com.digcoin.snapx.domain.member.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MemberAccessEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long memberId;
    private LocalDateTime accessTime;

    public MemberAccessEvent(Long memberId) {
        this.memberId = memberId;
        this.accessTime = LocalDateTime.now();
    }

}
