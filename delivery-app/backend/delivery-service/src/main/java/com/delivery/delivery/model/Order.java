package com.delivery.delivery.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private Long id;
    private String customerName;
    private Double totalAmount;
    private List<Product> products;
    private String status;
    private Boolean paidToMerchant;

    private LocalDateTime createdDate;
    private LocalDateTime paymentDate;
}

