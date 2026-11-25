package com.spring.teamwork.model.VO;

import com.spring.teamwork.model.Enum.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/*通知列表项*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationVO {
    private Long notificationId;
    private NotificationType type;
    private String title;
    private Long contractId;
    private Boolean read;
    private LocalDateTime createdAt;
}
