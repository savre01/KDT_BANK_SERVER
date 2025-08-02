package com.bank.server.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessagePayload {
    private Long chatIndex;
    private Long userIndex;
    private String content;
}
