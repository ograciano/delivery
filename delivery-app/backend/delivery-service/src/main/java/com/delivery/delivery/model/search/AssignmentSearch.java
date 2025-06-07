package com.delivery.delivery.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "assignments")
public class AssignmentSearch {
    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long orderId;

    @Field(type = FieldType.Long)
    private Long driverId;

    @Field(type = FieldType.Text)
    private String status;
}
