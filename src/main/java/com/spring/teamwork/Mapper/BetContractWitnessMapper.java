package com.spring.teamwork.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.teamwork.model.Entity.BetContractWitness;
import com.spring.teamwork.model.VO.ContractDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BetContractWitnessMapper extends BaseMapper<BetContractWitness> {

    @Select("SELECT COUNT(*) FROM bet_contract_witness WHERE contract_id = #{contractId}")
    int countByContractId(@Param("contractId") Long contractId);
    @Select("SELECT w.user_id AS userId, " +
            "       u.nickname AS nickname, " +
            "       v.verdict AS vote, " +
            "       v.created_at AS votedAt " +
            "FROM bet_contract_witness w " +
            "LEFT JOIN sys_user u ON u.id = w.user_id " +
            "LEFT JOIN bet_vote v ON v.contract_id = w.contract_id AND v.user_id = w.user_id " +
            "WHERE w.contract_id = #{contractId}")
    List<ContractDetailVO.WitnessVO> getWitnesses(@Param("contractId") Long contractId);

}

