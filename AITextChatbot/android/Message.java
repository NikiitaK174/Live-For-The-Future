package com.example.aichatapp;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    private int id;
    
    @SerializedName("content")
    private String content;
    
    @SerializedName("is_user")
    private boolean isUser;
    
    @SerializedName("timestamp")
    private String timestamp;
    
    public Message() {
        // Default constructor required for JSON deserialization
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isUser() {
        return isUser;
    }
    
    public void setUser(boolean user) {
        isUser = user;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
