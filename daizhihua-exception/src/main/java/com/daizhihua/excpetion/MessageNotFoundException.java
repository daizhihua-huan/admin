package com.daizhihua.excpetion;


/**
 * 消息找不到的异常
 */
public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
