package com.example.meettify.exception.member;

/*
 *   worker : 유요한
 *   work   : 유저 예외 처리
 *   date   : 2024/09/19
 * */
public class MemberException extends RuntimeException{
    public MemberException(String msg) {
        super(msg);
    }
}
