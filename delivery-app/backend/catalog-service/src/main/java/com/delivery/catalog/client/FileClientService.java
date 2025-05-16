package com.delivery.catalog.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileClientService {

    private final WebClient.Builder webClientBuilder;

    public Mono<String> uploadFile(FilePart file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.asyncPart("file", file.content(), DataBuffer.class)
                .header("Content-Disposition", "form-data; name=\"file\"; filename=\"" + file.filename() + "\"")
                .header("Content-Type", Objects.requireNonNull(file.headers().getContentType()).toString());

        return webClientBuilder.build()
                .post()
                .uri("http://file-service:8086/api/files/upload")
                .header("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class); // Esperamos que file-service nos regrese la URL como string
    }
}

