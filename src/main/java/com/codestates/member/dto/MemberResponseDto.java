package com.codestates.member.dto;

import com.codestates.member.entity.Member;
import com.codestates.stamp.Stamp;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private long memberId;
    private String email;
    private String name;
    private String phone;
    private Member.MemberStatus memberStatus;   // 추가된 부분
    private Stamp stamp;

    // 추가된 부분
    public String getMemberStatus() {
        return memberStatus.getStatus();
    }
    public int getStamp() {
        return stamp.getStampCount();
    }
}
