package com.app.chat.controllers;

import com.app.chat.models.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMsg(@Payload ChatMessage msg)
    {
        return msg;
    }


    /**
     * Handles requests to add a new user to the chat.
     * The username is stored in the session attributes, and the message is broadcasted
     * to all subscribers of the "/topic/public" destination.
     * msg the chat message payload containing the username
     * accessor the header accessor for accessing session attributes
     * @return the chat message to be sent to the subscribers
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage msg,
                               SimpMessageHeaderAccessor accessor)
    {
        accessor.getSessionAttributes().put("username", msg.getSender());
        return msg;
    }

}
