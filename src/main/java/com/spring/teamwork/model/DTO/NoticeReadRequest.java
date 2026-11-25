package com.spring.teamwork.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
/* 标记通知*/

@Data
public class NoticeReadRequest {
    private Long[] ids;
    private Boolean all;

}
