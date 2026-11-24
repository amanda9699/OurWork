package com.spring.teamwork.Service;

import com.spring.teamwork.model.DTO.AuthRequest;
import com.spring.teamwork.model.Entity.SysUser;
import com.spring.teamwork.model.VO.UserVO;


public interface UserService {

    /** 注册：用户名查重 → BCrypt 加密 → 插入 */
    UserVO.RegisterVO register(AuthRequest.RegisterRequest req);

    /** 登录：校验用户名密码 → Sa-Token 签发 → 返回 token + 基础信息 */
    UserVO.LoginVO login(AuthRequest.LoginRequest req);

    /** 根据 id 查用户（内部使用） */
    SysUser getById(Long userId);

    /** 获取当前登录用户的完整信息 */
    UserVO.UserProfileVO getCurrentUserProfile();

    /** 登出：Sa-Token 注销 + Redis 会话清理 */
    void logout();
}