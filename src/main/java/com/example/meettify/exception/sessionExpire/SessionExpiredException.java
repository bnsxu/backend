package com.example.meettify.exception.sessionExpire;
/*
 *   worker : 유요한
 *   work   : 인증 예외처리
 *   date   : 2024/09/19
 * */
public class SessionExpiredException extends RuntimeException{
    public SessionExpiredException(String message) {
        super(message);
    }
}
