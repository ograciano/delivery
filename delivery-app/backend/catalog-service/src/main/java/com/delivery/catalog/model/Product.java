package com.delivery.catalog.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double pricePerKg;
    private Double stockKg;
    private Long merchantId;
    private String merchantName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String principalImageUrl; // Imagen principal
    private List<String> galleryImageUrls; // Lista de URLs de galería
}
