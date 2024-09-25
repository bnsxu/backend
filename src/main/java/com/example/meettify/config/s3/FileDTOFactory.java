package com.example.meettify.config.s3;

@FunctionalInterface
public interface FileDTOFactory<T> {
    T createFileDTO(String oriFileName, String uploadFileName, String uploadFilePath, String uploadFileUrl);
}
