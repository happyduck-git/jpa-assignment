package com.codestates.order.controller;

import com.codestates.order.dto.OrderResponseDto;
import com.codestates.order.entity.OrderCoffee;
import com.codestates.coffee.entity.Coffee;
import com.codestates.coffee.service.CoffeeService;
import com.codestates.response.MultiResponseDto;
import com.codestates.response.SingleResponseDto;
import com.codestates.order.dto.OrderPatchDto;
import com.codestates.order.dto.OrderPostDto;
import com.codestates.order.entity.Order;
import com.codestates.order.mapper.OrderMapper;
import com.codestates.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v11/orders")
@Validated
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper mapper;
    private final CoffeeService coffeeService;

    public OrderController(OrderService orderService,
                           OrderMapper mapper,
                           CoffeeService coffeeService) {
        this.orderService = orderService;
        this.mapper = mapper;
        this.coffeeService = coffeeService;
    }

    @PostMapping
    public ResponseEntity postOrder(@Valid @RequestBody OrderPostDto orderPostDto) {
        System.out.println("Order Controller (OrderPostDto): " + orderPostDto);
        Order mappedOrder = mapper.orderPostDtoToOrder(orderPostDto);
        System.out.println("Order Controller (mapped): " + mappedOrder);
        Order savedOrder = orderService.createOrder(mappedOrder);
        System.out.println("Order Controller (saved): " + savedOrder);

        // TODO JPA 기능에 맞춰서 회원이 주문한 커피 정보를 ResponseEntity에 포함 시키세요.
        List<OrderCoffee> orderCoffeeList = savedOrder.getOrderCoffeeList();

        List<Coffee> coffeeList = orderCoffeeList.stream()
                .map(orderCoffee -> orderCoffee.getCoffee())
                .collect(Collectors.toList());


        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.orderToOrderResponseDto(savedOrder, coffeeList)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{order-id}")
    public ResponseEntity patchOrder(@PathVariable("order-id") @Positive long orderId,
                                     @Valid @RequestBody OrderPatchDto orderPatchDto) {
        orderPatchDto.setOrderId(orderId);
        Order order =
                orderService.updateOrder(mapper.orderPatchDtoToOrder(orderPatchDto));

        // patchOrder는 수정하지 마세요. 레퍼런스 코드에서 주문한 커피 정보가 포함 됩니다.
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.orderToOrderResponseDto(order, null))
                , HttpStatus.OK);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity getOrder(@PathVariable("order-id") @Positive long orderId) {
        Order order = orderService.findOrder(orderId);

        System.out.println(order);

        // TODO JPA 기능에 맞춰서 회원이 주문한 커피 정보를 ResponseEntity에 포함 시키세요.
        List<OrderCoffee> orderCoffeeList = order.getOrderCoffeeList();
        List<Coffee> coffees = orderCoffeeList.stream()
                .map(orderCoffee -> orderCoffee.getCoffee())
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.orderToOrderResponseDto(order, coffees)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size) {
        Page<Order> pageOrders = orderService.findOrders(page - 1, size);
        List<Order> orders = pageOrders.getContent();

        // TODO JPA 기능에 맞춰서 회원이 주문한 커피 정보 목록을 ResponseEntity에 포함 시키세요.

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.ordersToOrderResponseDtos(orders), pageOrders),
                HttpStatus.OK);
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity cancelOrder(@PathVariable("order-id") @Positive long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
