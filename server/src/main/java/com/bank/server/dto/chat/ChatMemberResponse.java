package com.bank.server.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMemberResponse {
    private String userName;
    private String department;
    private String position;
}
