package com.codestates.order.entity;

import com.codestates.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Column(nullable = false)
    @Min(1)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderCoffee> orderCoffeeList = new ArrayList<>();


    public Order(int quantity) {
        this.quantity = quantity;
    }

    public void addMember(Member member) {
        this.member = member;
    }

//    public void addOrderCoffeeToList(OrderCoffee orderCoffee) {
//        orderCoffeeList.add(orderCoffee);
//    }

    public void addOrderCoffeeToList(OrderCoffee orderCoffee) {
        this.orderCoffeeList.add(orderCoffee);
        if (orderCoffee.getOrder() != this) {
            orderCoffee.addOrder(this);
        }
    }
    public enum OrderStatus {
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRM(2, "주문 확정"),
        ORDER_COMPLETE(3, "주문 처리 완료"),
        ORDER_CANCEL(4, "주문 취소");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }

    @Override
    public String toString() {
        return "Order -> Order Id: " + orderId + " , Order quantity " + quantity + ", OrderCoffees " +
                orderCoffeeList;
    }
}