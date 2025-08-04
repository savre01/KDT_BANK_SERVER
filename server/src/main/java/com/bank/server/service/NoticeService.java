package com.bank.server.service;

import com.bank.server.model.Notice;
import com.bank.server.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNotice(Long id) {
        return noticeRepository.findById(id);
    }

    public Notice createNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    public Optional<Notice> updateNotice(Long id, Notice updated) {
        return noticeRepository.findById(id).map(notice -> {
            notice.setNoticeTitle(updated.getNoticeTitle());
            notice.setNoticeContent(updated.getNoticeContent());
            return noticeRepository.save(notice);
        });
    }

    public Optional<Notice> deleteNotice(Long id) {
    return noticeRepository.findById(id).map(notice -> {
        noticeRepository.delete(notice);
        return notice;
    });
    }
}