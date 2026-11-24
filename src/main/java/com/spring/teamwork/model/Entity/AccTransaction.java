package com.spring.teamwork.model.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.teamwork.model.Enum.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("acc_transaction")
public class AccTransaction {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    /** GIFT 类型为 null */
    private Long contractId;

    private TransactionType type;

    /** 变动金额，恒为正数，方向由 type 决定 */
    private Integer amount;

    private Integer balanceAfter;

    private Integer frozenAfter;

    private String remark;

    private LocalDateTime createdAt;
}
