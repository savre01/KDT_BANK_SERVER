package com.bank.server.dto.notice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeRequest {
    private String title;
    private String content;
}
