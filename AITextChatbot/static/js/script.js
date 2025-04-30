document.addEventListener('DOMContentLoaded', function() {
    // DOM elements
    const chatForm = document.getElementById('chat-form');
    const messageInput = document.getElementById('message-input');
    const messagesContainer = document.getElementById('chat-messages');
    const typingIndicator = document.getElementById('typing-indicator');
    const suggestionChipsContainer = document.getElementById('suggestion-chips');
    
    // Generate a session ID for this chat session
    const sessionId = 'session_' + Date.now();
    
    // Set up suggestion chips
    setupSuggestionChips();
    
    // Load chat history
    loadChatHistory();
    
    // Pregnancy-related suggestions to display after AI responses
    const pregnancySuggestions = [
        { text: "Morning sickness remedies", message: "What helps with morning sickness?" },
        { text: "Safe exercises", message: "What exercises are safe during pregnancy?" },
        { text: "Baby's development", message: "How is my baby developing each trimester?" },
        { text: "Labor signs", message: "What are the early signs of labor?" },
        { text: "Maternity leave", message: "When should I start maternity leave?" },
        { text: "Sleep positions", message: "What sleep positions are best during pregnancy?" },
        { text: "Heartburn relief", message: "How can I relieve pregnancy heartburn?" },
        { text: "Hospital bag", message: "What should I pack in my hospital bag?" },
        { text: "Nutrition needs", message: "What nutrients do I need during pregnancy?" },
        { text: "Birthing plan", message: "How do I create a birth plan?" },
        { text: "Prenatal appointments", message: "What happens during prenatal checkups?" },
        { text: "Baby kicks", message: "When should I feel baby movements?" }
    ];
    
    // Form submission handler
    chatForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const message = messageInput.value.trim();
        if (!message) return;
        
        // Add user message to UI
        addMessageToUI(message, true);
        
        // Clear input
        messageInput.value = '';
        
        // Show typing indicator
        typingIndicator.classList.remove('d-none');
        
        // Send message to server
        sendMessage(message);
    });
    
    // Function to set up suggestion chips
    function setupSuggestionChips() {
        // Get all suggestion chips
        const suggestionChips = document.querySelectorAll('.suggestion-chip');
        
        // Add click event to each chip
        suggestionChips.forEach(chip => {
            chip.addEventListener('click', function() {
                const message = this.getAttribute('data-message');
                
                // Add user message to UI
                addMessageToUI(message, true);
                
                // Show typing indicator
                typingIndicator.classList.remove('d-none');
                
                // Send message to server
                sendMessage(message);
            });
        });
    }
    
    // Function to send message to server
    function sendMessage(message) {
        // Set a timeout for the request
        const timeoutPromise = new Promise((_, reject) => {
            setTimeout(() => reject(new Error('Request timed out')), 15000);
        });
        
        // Actual fetch request
        const fetchPromise = fetch('/api/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                message: message,
                session_id: sessionId
            })
        });
        
        // Race between fetch and timeout
        Promise.race([fetchPromise, timeoutPromise])
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server responded with status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            // Hide typing indicator
            typingIndicator.classList.add('d-none');
            
            // Add AI response to UI
            if (data && data.message) {
                addMessageToUI(data.message, false);
                
                // Update suggestion chips after each response
                updateSuggestionChips();
            } else if (data && data.error) {
                addErrorMessageToUI(data.error);
            } else {
                throw new Error('Invalid response format from server');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            typingIndicator.classList.add('d-none');
            addErrorMessageToUI('Failed to get response. Please try again.');
        });
    }
    
    // Function to add message to UI
    function addMessageToUI(text, isUser) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${isUser ? 'user-message' : 'ai-message'}`;
        
        // For user messages, just use text
        if (isUser) {
            messageDiv.textContent = text;
        } 
        // For AI messages, check for HTML or URLs
        else {
            // Prioritize processing URLs 
            if (text.includes('http')) {
                messageDiv.innerHTML = linkifyText(text);
            } 
            // Check if the response contains HTML tags
            else if (text.includes('<p>') || text.includes('<li>') || text.includes('<ul>')) {
                messageDiv.innerHTML = text;
            } 
            // Otherwise just use text
            else {
                messageDiv.textContent = text;
            }
        }
        
        messagesContainer.appendChild(messageDiv);
        
        // Scroll to bottom
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }
    
    // Function to convert URLs to clickable links
    function linkifyText(text) {
        const urlRegex = /(https?:\/\/[^\s]+)/g;
        return text.replace(urlRegex, function(url) {
            return '<a href="' + url + '" target="_blank" rel="noopener noreferrer">' + url + '</a>';
        });
    }
    
    // Function to add error message
    function addErrorMessageToUI(text) {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message ai-message text-danger';
        messageDiv.textContent = `Error: ${text}`;
        
        messagesContainer.appendChild(messageDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }
    
    // Function to load chat history
    function loadChatHistory() {
        fetch(`/api/history?session_id=${sessionId}`)
        .then(response => response.json())
        .then(data => {
            if (data.history && data.history.length > 0) {
                data.history.forEach(msg => {
                    addMessageToUI(msg.content, msg.is_user);
                });
                
                // Update suggestion chips after history is loaded
                updateSuggestionChips();
            }
        })
        .catch(error => {
            console.error('Error loading chat history:', error);
        });
    }
    
    // Function to update suggestion chips with new random suggestions
    function updateSuggestionChips() {
        // Clear current suggestions
        suggestionChipsContainer.innerHTML = '';
        
        // Shuffle and select 4 random suggestions
        const shuffledSuggestions = [...pregnancySuggestions]
            .sort(() => Math.random() - 0.5)
            .slice(0, 4);
            
        // Create new suggestion chips
        shuffledSuggestions.forEach(suggestion => {
            const chip = document.createElement('button');
            chip.className = 'btn btn-sm btn-outline-info suggestion-chip';
            chip.setAttribute('data-message', suggestion.message);
            chip.textContent = suggestion.text;
            
            // Add click event
            chip.addEventListener('click', function() {
                const message = this.getAttribute('data-message');
                addMessageToUI(message, true);
                typingIndicator.classList.remove('d-none');
                sendMessage(message);
            });
            
            suggestionChipsContainer.appendChild(chip);
        });
    }
});
