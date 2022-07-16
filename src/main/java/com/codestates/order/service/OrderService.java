package com.codestates.order.service;

import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import com.codestates.coffee.service.CoffeeService;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.service.MemberService;
import com.codestates.order.entity.Order;
import com.codestates.order.repository.OrderRepository;
import com.codestates.stamp.Stamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final CoffeeService coffeeService;

    private final MemberRepository memberRepository;

    public OrderService(MemberService memberService,
                        OrderRepository orderRepository, CoffeeService coffeeService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.orderRepository = orderRepository;
        this.coffeeService = coffeeService;
        this.memberRepository = memberRepository;
    }

    public Order createOrder(Order order) {
        // 회원이 존재하는지 확인
        memberService.findVerifiedMember(order.getMember().getMemberId());
        Order savedOrder = orderRepository.save(order);
        System.out.println("FROM ORDER SERVICE: " + savedOrder);
        updateStamp(savedOrder);

        // TODO 커피가 존재하는지 조회하는 로직이 포함되어야 합니다.

        order.getOrderCoffeeList().
                forEach(orderCoffee -> orderCoffee.setCoffee(coffeeService.findVerifiedCoffee(orderCoffee.getCoffee().getCoffeeId())));
//        List<OrderCoffee> orderCoffeeList = order.getOrderCoffeeList();
//        System.out.println("OrderCoffeeList" + orderCoffeeList);
//        List<Coffee> coffeeList = orderCoffeeList.stream()
//                .map(orderCoffee -> orderCoffee.getCoffee())
//                .collect(Collectors.toList());
//        System.out.println("CoffeeList" + coffeeList);
//        coffeeList.stream()
//                .forEach(coffee -> coffeeService.verifyExistCoffee(coffee.getCoffeeCode()));

        return savedOrder;
    }


    // 메서드 추가
    public Order updateOrder(Order order) {
        Order findOrder = findVerifiedOrder(order.getOrderId());

        Optional.ofNullable(order.getOrderStatus())
                .ifPresent(orderStatus -> findOrder.setOrderStatus(orderStatus));
        findOrder.setModifiedAt(LocalDateTime.now());
        return orderRepository.save(findOrder);
    }

    public Order findOrder(long orderId) {
        return findVerifiedOrder(orderId);
    }

    public Page<Order> findOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size,
                Sort.by("orderId").descending()));
    }

    public void cancelOrder(long orderId) {
        Order findOrder = findVerifiedOrder(orderId);
        int step = findOrder.getOrderStatus().getStepNumber();

        // OrderStatus의 step이 2 이상일 경우(ORDER_CONFIRM)에는 주문 취소가 되지 않도록한다.
        if (step >= 2) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
        }
        findOrder.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        findOrder.setModifiedAt(LocalDateTime.now());
        orderRepository.save(findOrder);
    }

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order findOrder =
                optionalOrder.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        return findOrder;
    }

    private void updateStamp(Order order) {

        //존재하는 멤버인지 확인
        Member member = memberService.findMember(order.getMember().getMemberId());

        //주문한 커피 수량 확인
        int totalQuantity = order.getOrderCoffeeList().stream()
                .map(orderCoffee -> orderCoffee.getQuantity())
                .mapToInt(q -> q)
                .sum();

        Stamp stamp = member.getStamp(); //여기가 문제

        stamp.setStampCount(stamp.getStampCount() + totalQuantity);
        member.setStamp(stamp);

        stamp.setModifiedAt(LocalDateTime.now());

        memberService.updateMember(member);

    }




















}
