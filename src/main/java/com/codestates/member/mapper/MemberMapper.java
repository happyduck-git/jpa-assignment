package com.codestates.member.mapper;

import com.codestates.member.dto.MemberPatchDto;
import com.codestates.member.dto.MemberPostDto;
import com.codestates.member.dto.MemberResponseDto;
import com.codestates.member.entity.Member;
import com.codestates.stamp.Stamp;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberPostDto memberPostDto);

    /* 아래 적힌 코드가 mapping 시 기본적으로 적용되는 코드!*/
//    default Member memberPostDtoToMember(MemberPostDto memberPostDto) {
//
//        Member member = new Member();
//        //email
//        member.setEmail(memberPostDto.getEmail());
//        //name
//        member.setName(memberPostDto.getName());
//        //phone
//        member.setPhone(memberPostDto.getPhone());
//
//        return member;
//
//    }
    Member memberPatchDtoToMember(MemberPatchDto memberPatchDto);

    MemberResponseDto memberToMemberResponseDto(Member member);


/* 아래 적힌 코드가 mapping 시 기본적으로 적용되는 코드!*/
//    default MemberResponseDto memberToMemberResponseDto(Member member){
//
//        MemberResponseDto memberResponseDto = new MemberResponseDto();
//        memberResponseDto.setMemberId(member.getMemberId());
//        memberResponseDto.setEmail(member.getEmail());
//        memberResponseDto.setName(member.getName());
//        memberResponseDto.setPhone(member.getPhone());
//        memberResponseDto.setMemberStatus(member.getMemberStatus());
//        memberResponseDto.setStamp(member.getStamp());
//
//        return memberResponseDto;
//    }

    List<MemberResponseDto> membersToMemberResponseDtos(List<Member> members);
}
