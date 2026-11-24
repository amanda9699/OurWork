package com.spring.teamwork.model.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.teamwork.model.Enum.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_notification")
public class SysNotification {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private NotificationType type;

    private String title;

    private Long contractId;

    private Boolean isRead;

    private LocalDateTime createdAt;
}
