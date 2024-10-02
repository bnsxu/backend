package com.example.meettify.config.s3;

import org.springframework.context.annotation.Configuration;

@FunctionalInterface
@Configuration
public interface FileDTOFactory<T> {
    T createFileDTO(String oriFileName, String uploadFileName, String uploadFilePath, String uploadFileUrl);
}
