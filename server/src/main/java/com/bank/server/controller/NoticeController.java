package com.bank.server.controller;
import com.bank.server.dto.notice.NoticeSummaryResponse;

import com.bank.server.model.Notice;
import com.bank.server.service.NoticeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 전체 조회 - 누구나
    @GetMapping
    public ResponseEntity<List<NoticeSummaryResponse>> getAll() {
        List<NoticeSummaryResponse> result = noticeService.getAllNotices().stream()
            .map(n -> new NoticeSummaryResponse(
                n.getNoticeIndex(),
                n.getNoticeTitle(),
                n.getCreatedAt().toLocalDate()))
            .toList();

        return ResponseEntity.ok(result);
    }

    // 단건 조회 - 누구나
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getById(@PathVariable Long id) {
        return noticeService.getNotice(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 작성 - 관리자만
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Notice> create(@RequestBody Notice notice) {
        return ResponseEntity.ok(noticeService.createNotice(notice));
    }

    // 수정 - 관리자만
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Notice> update(@PathVariable Long id, @RequestBody Notice notice) {
        return noticeService.updateNotice(id, notice)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 삭제 - 관리자만
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return noticeService.deleteNotice(id)
                .map(n -> ResponseEntity.ok("삭제 완료")) 
                .orElse(ResponseEntity.notFound().build()); 
    }
}
