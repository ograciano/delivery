package com.delivery.order.model.search;

import com.delivery.order.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "orders")
public class OrderSearch {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String customerName;

    @Field(type = FieldType.Double)
    private Double totalAmount;

    @Field(type = FieldType.Object)
    private List<Product> products;

    @Field(type = FieldType.Text)
    private String status;

    @Field(type = FieldType.Boolean)
    private Boolean paidToMerchant;

    @Field(type = FieldType.Date)
    private LocalDateTime createdDate;

    @Field(type = FieldType.Date)
    private LocalDateTime paymentDate;
}
