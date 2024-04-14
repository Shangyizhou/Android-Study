package com.example.androidnote.model;

public class ChatModel {
    public static final int  LOADING = 0;
    public static final int  START_SHOW = 1;
    public static final int  HAS_SHOW = 2;
    private String id = null;
    private String name = null;
    private String imageUrl = null;
    private String message = null;
    /**
     * 0: LOADING
     * 1: START_SHOW
     * 2: HAS_SHOW
     */
    private int status;
    /**
     * 0 left
     * 1 right
     */
    private int type;

    public ChatModel() {

    }

    public ChatModel(String id, String name, String imageUrl, int type) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
