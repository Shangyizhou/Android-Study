package com.example.androidnote.model;

import java.util.List;

public class TagRequest {
    private List<String> text;
    private int num;
    public void setText(List<String> text) {
        this.text = text;
    }
    public List<String> getText() {
        return text;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public int getNum() {
        return num;
    }
}