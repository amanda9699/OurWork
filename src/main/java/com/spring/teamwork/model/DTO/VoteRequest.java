package com.spring.teamwork.model.DTO;

import com.spring.teamwork.model.Enum.Verdict;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
/*裁决投票*/
@Data
public class VoteRequest {
    @NotNull(message = "合约id不为空")
    private Long contractId;
    @NotNull(message = "投票结果不为空")
    private Verdict verdict;
}
