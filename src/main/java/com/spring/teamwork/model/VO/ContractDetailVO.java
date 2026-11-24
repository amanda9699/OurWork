package com.spring.teamwork.model.VO;

import com.spring.teamwork.model.Enum.ContractCategory;
import com.spring.teamwork.model.Enum.ContractStatus;
import com.spring.teamwork.model.Enum.Verdict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/** 详情 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailVO {
    private Long contractId;
    private String title;
    private String description;
    private ContractCategory category;
    private ContractStatus status;
    private Integer deposit;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime settledAt;
    private Verdict settleResult;
    private OwnerVO owner;
    private List<WitnessVO> witnesses;
    private ProofVO proof;
    private Integer viewCount;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class OwnerVO {
        private Long userId;
        private String nickname;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class WitnessVO {
        private Long userId;
        private String nickname;
        private Verdict vote;
        private LocalDateTime votedAt;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ProofVO {
        private String content;
        private List<String> images;
        private LocalDateTime submittedAt;
    }
}
