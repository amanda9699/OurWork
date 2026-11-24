package com.spring.teamwork.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
/* 标记通知*/

@Data
public class NoticeReadRequest {
    @NotNull(message = "通知id不能为空")
    private Long noticeId;
}
