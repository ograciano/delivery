package com.delivery.file.adapter;

import com.delivery.file.service.FileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("local")
public class MinioAdapter implements FileService {

    private final MinioClient minioClient;
    private final String bucketName = "delivery-files";
    private final List<String> allowedExtensions = List.of("jpg", "jpeg", "png", "gif", "webp");

    @Value("${minio.urlExpose}")
    private String minioUrl;
    private String urlExpose = "http://localhost:9000/%s/%s";

    @Override
    public Mono<String> uploadFile(FilePart filePart) {
        return filePart.content()
                .map(dataBuffer -> dataBuffer.asInputStream(true))
                .reduce(this::mergeStreams)
                .flatMap(inputStream -> {
                    String fileName = UUID.randomUUID() + "-" + filePart.filename();
                    return Mono.fromCallable(() -> {
                        minioClient.putObject(PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, -1, 10485760) // 10 MB chunks
                                .contentType(filePart.headers().getContentType().toString())
                                .build());
                        return String.format(urlExpose, bucketName, fileName); // Aquí construimos la URL final
                    });
                });
    }

    private InputStream mergeStreams(InputStream is1, InputStream is2) {
        return new java.io.SequenceInputStream(is1, is2);
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
