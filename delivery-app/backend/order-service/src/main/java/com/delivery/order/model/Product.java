package com.delivery.order.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;
    private String name;
    private Double pricePerKg;
    private Double stockKg;
    private Double quantityKg;
    private Double totalPrice;

    private Long merchantId;
    private String merchantName;
}