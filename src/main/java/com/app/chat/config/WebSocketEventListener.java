package com.app.chat.config;

import com.app.chat.models.ChatMessage;
import com.app.chat.models.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    // Automatically injects SimpMessageSendingOperations bean to send messages to specific destinations
    @Autowired
    SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * Listens for WebSocket disconnection events.
     * When a user disconnects, retrieves the username from the session attributes,
     * logs the disconnection, and sends a notification to all subscribers
     * on the "/topic/public" destination that the user has left the chat.
     * param event the session disconnect event that triggers this listener
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) accessor.getSessionAttributes().get("username");

        if (username != null) {
            log.info("User disconnected: {}", username);

            // Create a chat message indicating that the user has left
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVER)
                    .sender(username)
                    .build();

            // Send the message to the "/topic/public" destination
            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }
}
