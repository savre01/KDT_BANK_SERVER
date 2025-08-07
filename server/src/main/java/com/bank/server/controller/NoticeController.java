package com.bank.server.controller;

import com.bank.server.dto.notice.NoticeSummaryResponse;
import com.bank.server.model.Notice;
import com.bank.server.model.User;
import com.bank.server.service.NoticeService;
import com.bank.server.service.UserService;
import com.bank.server.service.notification.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;
    private final NotificationService notificationService;
    private final UserService userService;

    public NoticeController(NoticeService noticeService,
                            NotificationService notificationService,
                            UserService userService) {
        this.noticeService = noticeService;
        this.notificationService = notificationService;
        this.userService = userService;
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
    public ResponseEntity<Notice> create(@RequestBody Notice notice,
                                         Authentication authentication) {
        String userId = authentication.getName();  // 로그인한 사용자 ID
        User user = userService.getUserByUserId(userId);  // DB 조회로 userIndex 확보

        Notice saved = noticeService.createNotice(notice);

        // 🔔 WebSocket 알림 전송
        notificationService.notifyNoticeCreated(saved.getNoticeIndex(), user.getUserIndex());

        return ResponseEntity.ok(saved);
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
