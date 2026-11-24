package com.spring.teamwork.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.spring.teamwork.Exception.common.BizException;
import com.spring.teamwork.Mapper.AccTransactionMapper;
import com.spring.teamwork.Mapper.BetContractWitnessMapper;
import com.spring.teamwork.Mapper.ContractMapper;
import com.spring.teamwork.Mapper.SysUserMapper;
import com.spring.teamwork.Service.ContractService;
import com.spring.teamwork.model.DTO.ContractCreateRequest;
import com.spring.teamwork.model.Entity.AccTransaction;
import com.spring.teamwork.model.Entity.BetContract;
import com.spring.teamwork.model.Entity.SysUser;
import com.spring.teamwork.model.Enum.ContractStatus;
import com.spring.teamwork.model.Enum.TransactionType;
import com.spring.teamwork.model.PageVO;
import com.spring.teamwork.model.Result;
import com.spring.teamwork.model.VO.ContractDetailVO;
import com.spring.teamwork.model.VO.ContractListVO;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private AccTransactionMapper accTransactionMapper;
    @Autowired
    private BetContractWitnessMapper betContractWitnessMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public Result<ContractDetailVO> create(ContractCreateRequest request, Long userId) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        return transactionTemplate.execute(status -> doCreate(request, userId));
    }

    @Override
    public Result<PageVO<ContractListVO>> getMyContract(String status, Integer page, Integer size, Long userId) {
        LambdaQueryWrapper<BetContract> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(BetContract::getUserId, userId);
        if (status != null) {
            queryWrapper.eq(BetContract::getStatus, ContractStatus.valueOf(status));
        }
        queryWrapper.orderByDesc(BetContract::getCreatedAt);
        //分页查询
        Page<BetContract> betContractPage = contractMapper.selectPage(new Page<>(page, size), queryWrapper);
        //Entity 映射 VO
        List<ContractListVO> records = betContractPage.getRecords().stream()
                .map(betContract -> {
                    ContractListVO vo = new ContractListVO();
                    vo.setContractId(betContract.getId());       // 注意：Entity 叫 id，VO 叫 contractId，名字对不上
                    vo.setTitle(betContract.getTitle());
                    vo.setCategory(betContract.getCategory());
                    vo.setStatus(betContract.getStatus());
                    vo.setDeposit(betContract.getDeposit());
                    vo.setDeadline(betContract.getDeadline());
                    vo.setCreatedAt(betContract.getCreatedAt());
                    vo.setWitnessCount(betContractWitnessMapper.countByContractId(betContract.getId()));
                    return vo;
                }).toList();
        return Result.ok(new PageVO<>(betContractPage.getTotal(), betContractPage.getCurrent(), betContractPage.getSize(), records));
    }

    private Result<ContractDetailVO> doCreate(ContractCreateRequest request, Long userId) {
        if (sysUserMapper.freezeDeposit(userId, request.getDeposit()) == 0) {
            throw new BizException("余额不足", 409);
        }

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

        SysUser user = sysUserMapper.selectById(userId);

        //记 FREEZE 流水：金额恒正，方向由 type 决定，balanceAfter/frozenAfter 用于对账
        AccTransaction tx = new AccTransaction();
        tx.setUserId(userId);
        tx.setContractId(contract.getId());
        tx.setType(TransactionType.FREEZE);
        tx.setAmount(request.getDeposit());
        tx.setBalanceAfter(user.getBalance());
        tx.setFrozenAfter(user.getFrozen());
        tx.setRemark("创建合约冻结押金");
        accTransactionMapper.insert(tx);

        return Result.ok(buildVO(contract, user));
    }

    /**
     * 组装 ContractDetailVO
     */
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
