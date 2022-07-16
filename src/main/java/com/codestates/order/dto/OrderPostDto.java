package com.codestates.order.dto;

import com.codestates.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderPostDto {
    @Positive
    private long memberId;
    @Valid
    private List<OrderCoffeeDto> orderCoffeeDtos;

    public Member getMember() {
        Member member = new Member();
        member.setMemberId(memberId);
        return member;
    }

    @Override
    public String toString() {
        return "OrderPostDto -> Member Id: " + memberId + ", OrderCoffeeDtos(List): " + orderCoffeeDtos ;
    }
}
