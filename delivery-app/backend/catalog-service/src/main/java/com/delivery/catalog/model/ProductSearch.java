package com.delivery.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Representación del producto para el índice de Elasticsearch.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "products")
public class ProductSearch {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Double)
    private Double priceKg;

    @Field(type = FieldType.Keyword)
    private String principalImageUrl;

    @Field(type = FieldType.Keyword)
    private List<String> galleryImageUrls;

    @Field(type = FieldType.Double)
    private Double stockKg;

    @Field(type = FieldType.Long)
    private Long merchantId;

    @Field(type = FieldType.Text)
    private String merchantName;

    @Field(type = FieldType.Object)
    private Category category;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Category {
        @Field(type = FieldType.Long)
        private Long id;

        @Field(type = FieldType.Text)
        private String name;
    }
}

