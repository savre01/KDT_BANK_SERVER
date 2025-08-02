package com.bank.server.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatSummaryResponse {
    private Long chatIndex;
    private String chatName;
}