package com.bank.server.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NoticeSummaryResponse {
    private Long noticeIndex;
    private String noticeTitle;
    private LocalDate createdAt;
}
