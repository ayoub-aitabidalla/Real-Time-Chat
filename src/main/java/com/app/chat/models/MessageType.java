package com.app.chat.models;

import lombok.Data;
import lombok.Getter;

@Getter

public enum MessageType {
    CHAT,
    JOIN,
    LEAVER
}
