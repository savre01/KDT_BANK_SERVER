package com.bank.server.dto.chat;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ChatRequest {
    private String name;
    private List<Long> member;
}