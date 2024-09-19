package com.example.meettify.exception.order;
/*
 *   worker : 유요한
 *   work   : 주문 예외처리
 *   date   : 2024/09/19
 * */
public class OrderException extends RuntimeException{
    public OrderException(String message) {
        super(message);
    }
}
