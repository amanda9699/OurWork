package com.spring.teamwork.Service.impl;

import com.spring.teamwork.Exception.common.BizException;
import com.spring.teamwork.Mapper.AccTransactionMapper;
import com.spring.teamwork.Mapper.ContractMapper;
import com.spring.teamwork.Mapper.SysUserMapper;
import com.spring.teamwork.Service.ContractService;
import com.spring.teamwork.model.DTO.ContractCreateRequest;
import com.spring.teamwork.model.Entity.AccTransaction;
import com.spring.teamwork.model.Entity.BetContract;
import com.spring.teamwork.model.Entity.SysUser;
import com.spring.teamwork.model.Enum.ContractStatus;
import com.spring.teamwork.model.Enum.TransactionType;
import com.spring.teamwork.model.Result;
import com.spring.teamwork.model.VO.ContractDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private AccTransactionMapper accTransactionMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 创建对赌合约（接口文档 3.1）
     * TransactionTemplate 编程式事务：execute() 内的所有数据库操作在同一个事务中提交
     */
    @Override
    public Result<ContractDetailVO> create(ContractCreateRequest request, Long userId) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        return transactionTemplate.execute(status -> doCreate(request, userId));
    }

    /**
     * 事务内执行的业务逻辑：原子冻结押金 → 插入合约 → 查回余额 → 记 FREEZE 流水
     * 抛出 RuntimeException（如 BizException）时事务自动回滚，规则与 @Transactional 一致
     */
    private Result<ContractDetailVO> doCreate(ContractCreateRequest request, Long userId) {
        // ① 原子冻结押金：影响行数 0 = 余额不足
        if (sysUserMapper.freezeDeposit(userId, request.getDeposit()) == 0) {
            throw new BizException("余额不足", 409);
        }

        // ② 插入合约，创建后为 ACTIVE，广场立即可见
        BetContract contract = new BetContract();
        contract.setUserId(userId);
        contract.setTitle(request.getTitle());
        contract.setDescription(request.getDescription());
        contract.setCategory(request.getCategory());
        contract.setDeposit(request.getDeposit());
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setDeadline(request.getDeadline());
        contract.setViewCount(0);
        contractMapper.insert(contract);

        // ③ 同事务内查回冻结后的余额，作为流水快照（此时读到的是①刚写入的值）
        SysUser user = sysUserMapper.selectById(userId);

        // ④ 记 FREEZE 流水：金额恒正，方向由 type 决定，balanceAfter/frozenAfter 用于对账
        AccTransaction tx = new AccTransaction();
        tx.setUserId(userId);
        tx.setContractId(contract.getId());
        tx.setType(TransactionType.FREEZE);
        tx.setAmount(request.getDeposit());
        tx.setBalanceAfter(user.getBalance());
        tx.setFrozenAfter(user.getFrozen());
        tx.setRemark("创建合约冻结押金");
        accTransactionMapper.insert(tx);

        // ⑤ 组装响应（结构同接口文档 3.3）
        return Result.ok(buildVO(contract, user));
    }

    /** 组装合约详情 VO。创建时刻无见证人、无凭证；3.3 详情查询在此基础上补充见证人/投票信息 */
    private ContractDetailVO buildVO(BetContract contract, SysUser user) {
        ContractDetailVO vo = new ContractDetailVO();
        vo.setContractId(contract.getId());
        vo.setTitle(contract.getTitle());
        vo.setDescription(contract.getDescription());
        vo.setCategory(contract.getCategory());
        vo.setStatus(contract.getStatus());
        vo.setDeposit(contract.getDeposit());
        vo.setDeadline(contract.getDeadline());
        vo.setCreatedAt(contract.getCreatedAt());
        vo.setSettledAt(contract.getSettledAt());
        vo.setSettleResult(contract.getSettleResult());

        ContractDetailVO.OwnerVO owner = new ContractDetailVO.OwnerVO();
        owner.setUserId(user.getId());
        owner.setNickname(user.getNickname());
        vo.setOwner(owner);

        vo.setWitnesses(new ArrayList<>());
        vo.setProof(null);
        vo.setViewCount(contract.getViewCount());
        return vo;
    }
}
