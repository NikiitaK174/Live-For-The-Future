package com.example.aichatapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private static final String BASE_URL = "http://10.0.2.2:5000/"; // For Android emulator to access localhost

    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private View loadingIndicator;

    private MessageAdapter messageAdapter;
    private List<Message> messagesList;
    private String sessionId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get session ID from intent
        sessionId = getIntent().getStringExtra("SESSION_ID");
        if (sessionId == null) {
            sessionId = "default_session";
        }

        // Initialize UI components
        recyclerView = findViewById(R.id.messagesRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        // Set up RecyclerView
        messagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messagesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Set up Retrofit for API calls
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Load chat history
        loadChatHistory();

        // Set up send button click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadChatHistory() {
        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);

        // Make API call to get history
        apiService.getChatHistory(sessionId).enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                loadingIndicator.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Message> history = response.body().getHistory();
                    
                    if (history != null && !history.isEmpty()) {
                        messagesList.clear();
                        messagesList.addAll(history);
                        messageAdapter.notifyDataSetChanged();
                        scrollToBottom();
                    } else {
                        // Add default welcome message if no history
                        Message welcomeMessage = new Message();
                        welcomeMessage.setContent("Hello! I'm your AI assistant. How can I help you today?");
                        welcomeMessage.setUser(false);
                        messagesList.add(welcomeMessage);
                        messageAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "Error loading history: " + response.code());
                    Toast.makeText(ChatActivity.this, "Failed to load chat history", Toast.LENGTH_SHORT).show();
                    
                    // Add default welcome message
                    Message welcomeMessage = new Message();
                    welcomeMessage.setContent("Hello! I'm your AI assistant. How can I help you today?");
                    welcomeMessage.setUser(false);
                    messagesList.add(welcomeMessage);
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Log.e(TAG, "Network error loading history", t);
                Toast.makeText(ChatActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                
                // Add default welcome message on failure
                Message welcomeMessage = new Message();
                welcomeMessage.setContent("Hello! I'm your AI assistant. How can I help you today?");
                welcomeMessage.setUser(false);
                messagesList.add(welcomeMessage);
                messageAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        // Add user message to UI
        Message userMessage = new Message();
        userMessage.setContent(messageText);
        userMessage.setUser(true);
        messagesList.add(userMessage);
        messageAdapter.notifyItemInserted(messagesList.size() - 1);
        scrollToBottom();

        // Clear input field
        messageInput.setText("");

        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);

        // Create chat request object
        ChatRequest chatRequest = new ChatRequest(messageText, sessionId);

        // Make API call
        apiService.sendMessage(chatRequest).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                loadingIndicator.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    // Add AI response to UI
                    Message aiMessage = new Message();
                    aiMessage.setContent(response.body().getMessage());
                    aiMessage.setUser(false);
                    messagesList.add(aiMessage);
                    messageAdapter.notifyItemInserted(messagesList.size() - 1);
                    scrollToBottom();
                } else {
                    Log.e(TAG, "Error from server: " + response.code());
                    // Add error message
                    Message errorMessage = new Message();
                    errorMessage.setContent("Sorry, there was an error processing your request.");
                    errorMessage.setUser(false);
                    messagesList.add(errorMessage);
                    messageAdapter.notifyItemInserted(messagesList.size() - 1);
                    scrollToBottom();
                    
                    Toast.makeText(ChatActivity.this, "Error: Failed to get response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Log.e(TAG, "Network error", t);
                
                // Add error message
                Message errorMessage = new Message();
                errorMessage.setContent("Network error. Please check your connection.");
                errorMessage.setUser(false);
                messagesList.add(errorMessage);
                messageAdapter.notifyItemInserted(messagesList.size() - 1);
                scrollToBottom();
                
                Toast.makeText(ChatActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scrollToBottom() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }
}
