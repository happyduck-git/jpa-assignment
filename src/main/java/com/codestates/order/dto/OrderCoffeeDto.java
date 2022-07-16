package com.codestates.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
public class OrderCoffeeDto {
    @Positive
    private long coffeeId;
    @Positive
    private int quantity;

    @Override
    public String toString() {
        return "OrderCoffeeDto-> Coffee Id: " + coffeeId + ", quantity: " + quantity;
    }
}
