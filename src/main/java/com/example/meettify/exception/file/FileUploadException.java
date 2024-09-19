package com.example.meettify.exception.file;
/*
 *   worker : 유요한
 *   work   : 파일 업로드 예외처리
 *   date   : 2024/09/19
 * */
public class FileUploadException extends RuntimeException{
    public FileUploadException(String message) {
        super(message);
    }
}
