package com.bank.server.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatDetailResponse {
    private Long chatIndex;
    private String chatName;
    private List<ChatMemberResponse> members;
}