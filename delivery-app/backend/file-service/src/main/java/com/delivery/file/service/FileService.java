package com.delivery.file.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<String> uploadFile(FilePart file);
}