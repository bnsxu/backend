package com.example.meettify.exception.meetBoard;

/*
 *   worker : 조영흔
 *   work   : 모임 게시판 에러
 *   date   : 2024/09/24
 * */
public class MeetBoardException extends RuntimeException{
    public MeetBoardException(String msg) {
        super(msg);
    }
}
