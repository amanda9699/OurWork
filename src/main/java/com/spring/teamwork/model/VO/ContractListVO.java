package com.spring.teamwork.model.VO;

import com.spring.teamwork.model.Enum.ContractCategory;
import com.spring.teamwork.model.Enum.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractListVO {
    private Long contractId;
    private String title;
    private ContractCategory category;
    private ContractStatus status;
    private Integer deposit;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private Integer witnessCount;
    private String ownerNickname;   // 广场列表填充
    private Integer viewCount;      // 广场列表填充
}
