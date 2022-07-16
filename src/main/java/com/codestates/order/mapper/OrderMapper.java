package com.codestates.order.mapper;

import com.codestates.coffee.entity.Coffee;
import com.codestates.member.entity.Member;
import com.codestates.order.dto.*;
import com.codestates.order.entity.Order;
import com.codestates.order.entity.OrderCoffee;
import org.mapstruct.Mapper;


import java.util.List;
import java.util.stream.Collectors;

/*
코멘트 지운 후
 */

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) { //여기 코드 reference랑 비교해보기!!

        Order order = new Order();

        //member
        Member member = orderPostDto.getMember();

        //List<OrderCoffee>

        /* 내 코드 => 수정해보기*/
//        List<OrderCoffee> orderCoffeeList = orderPostDto.getOrderCoffeeDtos().stream()
//                .map(orderCoffeeDto -> orderCoffeeDtoToOrderCoffee(orderCoffeeDto))
//                .collect(Collectors.toList());



        //레퍼런스 코드
        List<OrderCoffee> orderCoffeeList = orderPostDto.getOrderCoffeeDtos().stream()
                .map(orderCoffeeDto -> {
                    OrderCoffee orderCoffee = new OrderCoffee();
                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());

                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                    orderCoffee.addOrder(order);
                    orderCoffee.addCoffee(coffee);

                    return orderCoffee;
                }).collect(Collectors.toList());

        //quantity
        Integer quantitySum = orderPostDto.getOrderCoffeeDtos().stream()
                .map(orderCoffeeDto -> orderCoffeeDto.getQuantity())
                .mapToInt(Integer::intValue)
                .sum();


        order.setMember(member);
        order.setQuantity(quantitySum);
        order.setOrderCoffeeList(orderCoffeeList);

        return order;
    }


    //orderCoffeeDtoToOrderCoffee => error 발생
    default OrderCoffee orderCoffeeDtoToOrderCoffee(OrderCoffeeDto orderCoffeeDto) {
        OrderCoffee orderCoffee = new OrderCoffee();

        //quantity
        orderCoffee.setQuantity(orderCoffeeDto.getQuantity());

        Order order = new Order();

        Coffee coffee = new Coffee();
        coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());

        orderCoffee.addCoffee(coffee);
        orderCoffee.addOrder(order);

        return orderCoffee;

    }

    /* 수정해보기 */
    default OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee) {

        return OrderCoffeeResponseDto.builder()
                .coffeeId(orderCoffee.getCoffee().getCoffeeId())
                .korName(orderCoffee.getCoffee().getKorName())
                .engName(orderCoffee.getCoffee().getEngName())
                .price(orderCoffee.getCoffee().getPrice())
                .quantity(orderCoffee.getQuantity())
                .build();

    }

    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);

    default OrderResponseDto orderToOrderResponseDto(Order order, List<Coffee> coffees) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        //orderId
        orderResponseDto.setOrderId(order.getOrderId());
        //memberId
        orderResponseDto.setMemberId(order.getMember().getMemberId());
        //Order.status
        orderResponseDto.setOrderStatus(order.getOrderStatus());
        //List<OrderCoffeeResponseDto>
        List<OrderCoffeeResponseDto> orderCoffeeResponseDtos =
                coffees.stream()
                        .map(coffee -> {
                            OrderCoffeeResponseDto orderCoffeeResponseDto = new OrderCoffeeResponseDto();
                            orderCoffeeResponseDto.setCoffeeId(coffee.getCoffeeId());
                            orderCoffeeResponseDto.setKorName(coffee.getKorName());
                            orderCoffeeResponseDto.setEngName(coffee.getEngName());
                            orderCoffeeResponseDto.setPrice(coffee.getPrice());
                            List<Integer> quantity = order.getOrderCoffeeList().stream().map(orderCoffee ->
                                    orderCoffee.getQuantity()).collect(Collectors.toList());
                            orderCoffeeResponseDto.setQuantity(quantity.get(0));//수정필요
                            return orderCoffeeResponseDto;
                        }).collect(Collectors.toList());
        orderResponseDto.setOrderCoffees(orderCoffeeResponseDtos);
        orderResponseDto.setCreatedAt(order.getCreatedAt());

        return orderResponseDto;
    }
    default List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders) {


        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(order -> {
                    //List<Coffee> 알아내기
                    List<OrderCoffee> orderCoffeeList = order.getOrderCoffeeList();
                    List<Coffee> coffeeList = orderCoffeeList.stream()
                            .map(OrderCoffee::getCoffee)
                            .collect(Collectors.toList());
                    return orderToOrderResponseDto(order, coffeeList);
                }).collect(Collectors.toList());

        return orderResponseDtos;

    }
}
