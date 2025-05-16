package com.delivery.order.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private Double totalAmount;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private List<Product> products;

    private String status;

    @Column(nullable = false)
    private Boolean paidToMerchant = false;

    private LocalDateTime createdDate;
    private LocalDateTime paymentDate;
}
