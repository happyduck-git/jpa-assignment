package com.codestates.stamp;

import com.codestates.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.codestates.audit.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Stamp extends Auditable {

    @Id
    @GeneratedValue
    private Long stampId;

    @Column(name = "STAMP_COUNT")
    private int stampCount = 0;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(nullable = false, name = "LAST_MODIFIED_AT")
//    private LocalDateTime modifiedAt = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")//양방향 필요 없을 수도
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if (member.getStamp() != this) {
            member.setStamp(this);
        }
    }

}
