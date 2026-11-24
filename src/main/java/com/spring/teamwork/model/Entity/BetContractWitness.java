package com.spring.teamwork.model.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("bet_contract_witness")
public class BetContractWitness extends BaseEntity {

    private Long contractId;

    private Long userId;

    private LocalDateTime acceptedAt;
}
