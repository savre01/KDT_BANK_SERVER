package com.bank.server.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageIndex;
    private String content;
    private Date sentTime;
    private Long senderIndex;
    private String senderName;
}
