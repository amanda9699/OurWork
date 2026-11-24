package com.spring.teamwork.model.DTO;

import com.spring.teamwork.model.Enum.ContractCategory;
import jakarta.validation.constraints.Min;
import lombok.Data;
/*广场查询*/
@Data
public class SquareQueryRequest {
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "页大小最小为1")
    private Integer pageSize = 10;

    private ContractCategory category;
}
