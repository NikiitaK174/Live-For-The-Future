package com.example.aichatapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    
    @POST("/api/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest request);
    
    @GET("/api/history")
    Call<HistoryResponse> getChatHistory(@Query("session_id") String sessionId);
    
    // Request and response classes
    class ChatRequest {
        private String message;
        private String session_id;
        
        public ChatRequest(String message, String sessionId) {
            this.message = message;
            this.session_id = sessionId;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getSessionId() {
            return session_id;
        }
        
        public void setSessionId(String sessionId) {
            this.session_id = sessionId;
        }
    }
    
    class ChatResponse {
        private String message;
        private int messageId;
        private String error;
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public int getMessageId() {
            return messageId;
        }
        
        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
    }
    
    class HistoryResponse {
        private java.util.List<Message> history;
        private String error;
        
        public java.util.List<Message> getHistory() {
            return history;
        }
        
        public void setHistory(java.util.List<Message> history) {
            this.history = history;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
    }
}
