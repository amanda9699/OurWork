package com.spring.teamwork.model.DTO;

import com.spring.teamwork.model.Enum.ContractCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 创建合约 */
@Data
public class ContractCreateRequest {
    @NotBlank
    @Size(max = 50)
    private String title;
    @Size(max = 500)
    private String description;
    private ContractCategory category = ContractCategory.OTHER;
    @NotNull @Future(message = "截止时间必须晚于当前时间")
    private LocalDateTime deadline;
    @NotNull
    @Min(value = 1, message = "押金至少为 1")
    private Integer deposit;
}
