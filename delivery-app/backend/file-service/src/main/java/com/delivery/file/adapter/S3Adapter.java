package com.delivery.file.adapter;

import com.delivery.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class S3Adapter implements FileService {

    private final S3AsyncClient s3Client;
    private final String bucketName = "delivery-files";
    private final List<String> allowedExtensions = List.of("jpg", "jpeg", "png", "gif", "webp");
    private static final String S3_URL_FORMAT = "https://%s.s3.%s.amazonaws.com/%s";
    private static final String REGION = "us-east-1"; // Cambia esto a tu región

    public Mono<String> uploadFile(FilePart filePart) {
        String fileName = UUID.randomUUID() + "-" + filePart.filename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(filePart.headers().getContentType().toString())
                .build();

        // Primero convierte el contenido del filePart en un Publisher<ByteBuffer>
        AsyncRequestBody requestBody = AsyncRequestBody.fromPublisher(
                filePart.content()
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            return ByteBuffer.wrap(bytes);
                        })
        );

        // Envuelve el CompletableFuture en un Mono
        return Mono.fromFuture(
                s3Client.putObject(putObjectRequest, requestBody)
        ).map(response -> {
            // Ya aquí es Mono, y puedes transformar
            return String.format(S3_URL_FORMAT, bucketName, REGION, fileName);
        });
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
