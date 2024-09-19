package com.example.meettify.exception.file;
/*
 *   worker : 유요한
 *   work   : 파일 다운로드 예외처리
 *   date   : 2024/09/19
 * */
public class FileDownloadException extends RuntimeException{
    public FileDownloadException(String message) {
        super(message);
    }
}
