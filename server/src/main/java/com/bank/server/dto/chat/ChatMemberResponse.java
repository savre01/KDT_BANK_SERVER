package com.bank.server.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMemberResponse {
    private Long userIndex;
    private String userId;
    private String userName;
}
