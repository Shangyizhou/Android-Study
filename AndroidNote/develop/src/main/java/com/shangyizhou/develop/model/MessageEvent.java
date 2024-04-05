package com.shangyizhou.develop.model;

public class MessageEvent {
    public final int name;
    public final String message;

    public MessageEvent(int name, String message) {
        this.name = name;
        this.message = message;
    }
}
