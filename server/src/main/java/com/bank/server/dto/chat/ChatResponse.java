package com.bank.server.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ChatResponse {
    private Long chatIndex;
    private String chatName;
    private Date chatCreated;
    private List<ChatMemberResponse> members;
}
