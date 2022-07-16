package com.codestates.order.entity;


import com.codestates.coffee.entity.Coffee;
import com.codestates.order.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderCoffee {

    @Id
    @GeneratedValue
    private Long orderCoffeeId;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;

    public void addOrder(Order order) {
        this.order = order;
        if (!this.order.getOrderCoffeeList().contains(this)) {
            this.order.getOrderCoffeeList().add(this);
        }
    }

    public void addCoffee(Coffee coffee) {
        this.coffee = coffee;
        if (!this.coffee.getOrderCoffeeList().contains(this)) {
            this.coffee.addOrderCoffee(this);
        }
    }

    public void addQuantity(Integer quantity) {this.quantity = quantity;}

    @Override
    public String toString() {
        return getCoffee().getEngName();
    }
}
