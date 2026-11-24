package com.spring.teamwork.model.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 由数据库 DEFAULT CURRENT_TIMESTAMP 兜底，插入时传 null 即可 */
    private LocalDateTime createdAt;

    /** 由数据库 ON UPDATE CURRENT_TIMESTAMP 自动维护 */
    private LocalDateTime updatedAt;
}
