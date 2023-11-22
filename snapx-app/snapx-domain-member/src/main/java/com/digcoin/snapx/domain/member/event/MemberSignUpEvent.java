package com.digcoin.snapx.domain.member.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MemberSignUpEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long memberId;
    private LocalDateTime signUpTime;

    public MemberSignUpEvent(Long memberId) {
        this.memberId = memberId;
        this.signUpTime = LocalDateTime.now();
    }

}
