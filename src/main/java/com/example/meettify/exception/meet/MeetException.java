package com.example.meettify.exception.meet;

/*
 *   worker : 조영흔
 *   work   : 모임 예외 처리
 *   date   : 2024/09/24
 * */
public class MeetException extends RuntimeException{
    public MeetException(String msg) {
        super(msg);
    }
}
