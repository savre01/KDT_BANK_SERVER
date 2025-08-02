package com.bank.server.dto.chat;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ChatMemberRequest {
    private Long chatIndex;
    private List<Long> member;
}
