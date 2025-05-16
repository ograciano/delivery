package com.delivery.file.controller;

import com.delivery.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart("file") Part filePart) {
        log.info("Recibiendo archivo: {}", filePart);
        if (!(filePart instanceof FilePart realFilePart)) {
            return Mono.error(new IllegalArgumentException("El part recibido no es un archivo"));
        }
        return fileService.uploadFile(realFilePart)
                .map(ResponseEntity::ok);
    }
}
