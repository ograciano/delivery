package com.delivery.order.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantFinancialSummary {
    private Long merchantId;
    private String merchantName;
    private Double totalRevenue;
    private Double totalPaid;
    private Double totalPending;
    private Integer totalOrders;
}
