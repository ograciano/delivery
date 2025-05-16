package com.delivery.catalog.service;

import com.delivery.catalog.client.FileClientService;
import com.delivery.catalog.model.Product;
import com.delivery.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageService {

    private final FileClientService fileClientService;
    private final ProductRepository productRepository;

    public Mono<Product> uploadImages(Long productId, Mono<FilePart> principalImage, Flux<FilePart> galleryImages) {
        return Mono.fromCallable(() -> productRepository.findById(productId))
                .flatMap(optionalProduct -> optionalProduct
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new RuntimeException("Producto no encontrado")))
                )
                .flatMap(product -> {
                    // Subir la imagen principal
                    Mono<String> principalMono = principalImage.flatMap(fileClientService::uploadFile);

                    // Subir las imágenes de la galería (si existen)
                    Flux<String> galleryFlux = galleryImages != null
                            ? galleryImages.flatMap(fileClientService::uploadFile)
                            : Flux.empty();

                    return principalMono.zipWith(galleryFlux.collectList(), (principalUrl, galleryUrls) -> {
                        product.setPrincipalImageUrl(principalUrl);
                        product.setGalleryImageUrls(galleryUrls);
                        return product;
                    });
                })
                .flatMap(product -> Mono.fromCallable(() -> productRepository.save(product)));
    }
}
