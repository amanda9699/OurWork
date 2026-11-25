package com.spring.teamwork.Controller;

import cn.dev33.satoken.stp.StpUtil;
import com.spring.teamwork.Service.NoticeService;
import com.spring.teamwork.model.DTO.NoticeReadRequest;
import com.spring.teamwork.model.PageVO;
import com.spring.teamwork.model.Result;
import com.spring.teamwork.model.VO.NoticeUnreadCountVO;
import com.spring.teamwork.model.VO.NotificationVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/api/notifications")
@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    /** 未读小红点（8.1） */
    @GetMapping("/unread-count")
    public Result<NoticeUnreadCountVO> unreadCount() {
        return Result.ok(noticeService.getUnreadCount(StpUtil.getLoginIdAsLong()));
    }

    /** 通知列表（8.2） */
    @GetMapping
    public Result<PageVO<NotificationVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        size = Math.min(size, 50);
        return Result.ok(noticeService.pageQuery(StpUtil.getLoginIdAsLong(), page, size));
    }

    /** 标记已读（8.3）：ids 数组 或 all=true */
    @PostMapping("/read")
    public Result<Void> markRead(@RequestBody @Valid NoticeReadRequest req) {
        long userId = StpUtil.getLoginIdAsLong();

        if (Boolean.TRUE.equals(req.getAll())
                || req.getIds() == null
                || req.getIds().length == 0) {
            noticeService.markAllRead(userId);
        } else {
            List<Long> ids = Arrays.asList(req.getIds());
            noticeService.markRead(userId, ids);
        }
        return Result.ok();
    }
}
