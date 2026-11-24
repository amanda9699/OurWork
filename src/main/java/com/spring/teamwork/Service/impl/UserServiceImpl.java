package com.spring.teamwork.Service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spring.teamwork.Mapper.SysUserMapper;
import com.spring.teamwork.Service.UserService;
import com.spring.teamwork.model.DTO.AuthRequest;
import com.spring.teamwork.model.Entity.SysUser;
import com.spring.teamwork.model.Enum.ErrorCode;
import com.spring.teamwork.model.VO.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser register(AuthRequest.RegisterRequest req) {

        // 1. 用户名查重
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, req.getUsername())
        );
        if (count > 0) {
            ErrorCode.USERNAME_EXISTS.throwEx();
        }

        // 2. 组装实体（id、createdAt、updatedAt 交给 MyBatis-Plus 自动填充 / DB 兜底）
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // BCrypt 加密
        user.setNickname(req.getUsername());                          // 默认昵称 = 用户名
        user.setBalance(0);                                           // 初始余额 0
        user.setFrozen(0);
        user.setIsDeleted(0);

        sysUserMapper.insert(user);
        return user;
    }


    @Override
    public UserVO.LoginVO login(AuthRequest.LoginRequest req) {

        // 1. 查用户
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, req.getUsername())
        );
        if (user == null) {
            ErrorCode.PASSWORD_WRONG.throwEx(); // 不区分"用户不存在"和"密码错"，防枚举
        }

        // 2. BCrypt 验密
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            ErrorCode.PASSWORD_WRONG.throwEx();
        }

        // 3. Sa-Token 签发登录态
        StpUtil.login(user.getId());

        // 4. 构造返回
        return UserVO.LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .userId(user.getId())
                .nickname(user.getNickname())
                .build();
    }

    @Override
    public SysUser getById(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            ErrorCode.USER_NOT_FOUND.throwEx();
        }
        return user;
    }

    @Override
    public UserVO.UserProfileVO getCurrentUserProfile() {
        long userId = StpUtil.getLoginIdAsLong();
        SysUser user = getById(userId);
        return UserVO.UserProfileVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .balance(user.getBalance())
                .frozen(user.getFrozen())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public void logout() {
        StpUtil.logout();   // 清当前 token 会话（Sa-Token + Redis 一并清）
    }

}
