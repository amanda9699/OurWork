package com.spring.teamwork.model.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.teamwork.model.Enum.Verdict;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("bet_vote")
public class BetVote {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long contractId;

    private Long userId;

    private Verdict verdict;

    private String comment;

    private LocalDateTime createdAt;
}
