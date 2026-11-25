package com.spring.teamwork.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.teamwork.model.Entity.SysNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysNotificationMapper extends BaseMapper<SysNotification> {
    /** 统计未读数（高频查询，用索引 idx_user_read） */
    @Select("SELECT COUNT(*) FROM sys_notification WHERE user_id = #{userId} AND is_read = 0")
    int countUnread(@Param("userId") Long userId);


}
