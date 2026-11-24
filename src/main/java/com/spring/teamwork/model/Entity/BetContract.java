package com.spring.teamwork.model.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.spring.teamwork.model.Enum.ContractCategory;
import com.spring.teamwork.model.Enum.ContractStatus;
import com.spring.teamwork.model.Enum.Verdict;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "bet_contract", autoResultMap = true)
public class BetContract extends BaseEntity {

    private Long userId;

    private String title;

    private String description;

    private ContractCategory category;

    /** 押金 */
    private Integer deposit;

    private ContractStatus status;

    private LocalDateTime deadline;

    private LocalDateTime settledAt;

    private Verdict settleResult;

    private String proofContent;

    /** 凭证图片 URL 列表，对应 JSON 字段 */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> proofImages;

    private LocalDateTime proofSubmittedAt;

    private Integer viewCount;

    @TableLogic
    private Integer isDeleted;
}
