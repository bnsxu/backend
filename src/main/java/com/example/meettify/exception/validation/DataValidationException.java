package com.example.meettify.exception.validation;
/*
 *   worker : 유요한
 *   work   : 검증 예외처리
 *            데이터 유효성 검사 실패 시 발생하는 예외
 *   date   : 2024/09/19
 * */
public class DataValidationException extends RuntimeException{
    public DataValidationException(String message) {
        super(message);
    }
}
