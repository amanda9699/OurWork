package com.spring.teamwork.Service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spring.teamwork.Mapper.AccTransactionMapper;
import com.spring.teamwork.Mapper.SysUserMapper;
import com.spring.teamwork.Service.UserService;
import com.spring.teamwork.model.DTO.AuthRequest;
import com.spring.teamwork.model.Entity.AccTransaction;
import com.spring.teamwork.model.Entity.SysUser;
import com.spring.teamwork.model.Enum.ErrorCode;
import com.spring.teamwork.model.Enum.TransactionType;
import com.spring.teamwork.model.VO.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final int GIFT_AMOUNT = 1000;
    private final AccTransactionMapper accTransactionMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO.RegisterVO register(AuthRequest.RegisterRequest req) {

        // 用户名查重
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, req.getUsername())
        );
        if (count > 0) {
            ErrorCode.USERNAME_EXISTS.throwEx();
        }

        // 组装实体
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // BCrypt 加密
        user.setNickname(req.getUsername());                          // 默认昵称 = 用户名
        user.setBalance(0);                                           // 初始余额 0
        user.setFrozen(0);
        user.setIsDeleted(0);

        sysUserMapper.insert(user);

        AccTransaction giftTx = new AccTransaction();
        giftTx.setUserId(user.getId());
        giftTx.setContractId(null);
        giftTx.setType(TransactionType.GIFT);
        giftTx.setAmount(GIFT_AMOUNT);
        giftTx.setBalanceAfter(user.getBalance());
        giftTx.setFrozenAfter(0);
        giftTx.setRemark("注册赠送");
        accTransactionMapper.insert(giftTx);
        StpUtil.login(user.getId());
        return UserVO.RegisterVO.builder()
                .userId(user.getId())
                .token(StpUtil.getTokenValue())
                .giftAmount(GIFT_AMOUNT)
                .build();

    }

    @Override
    public UserVO.LoginVO login(AuthRequest.LoginRequest req) {
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, req.getUsername())
        );
        // 不区分"用户不存在"和"密码错误"（防用户名枚举）
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            ErrorCode.PASSWORD_WRONG.throwEx();
        }

        StpUtil.login(user.getId());

        // 接口文档 2.2：登录响应必须带余额
        return UserVO.LoginVO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .token(StpUtil.getTokenValue())
                .balance(user.getBalance())
                .frozen(user.getFrozen())
                .build();
    }

    // 查用户
    @Override
    public SysUser getById(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            ErrorCode.USER_NOT_FOUND.throwEx();
        }
        return user;
    }

    // ==================== 当前用户信息 ====================
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

    // ==================== 登出 ====================
    @Override
    public void logout() {
        StpUtil.logout();
    }
}
