package com.delivery.order.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantSalesSummary {
    private Long merchantId;
    private String merchantName;
    private Double totalRevenue;
    private Integer totalProductsSold;
    private Integer totalOrders;
}
