package com.spring.teamwork.model.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
/*生成见证人邀请*/
@Data
public class WitnessInviteRequest {
    @NotNull(message = "合约id不能为空")
    private Long contractId;
}
