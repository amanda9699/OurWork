package com.spring.teamwork.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.teamwork.model.Entity.BetContractWitness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BetContractWitnessMapper extends BaseMapper<BetContractWitness> {

    @Select("SELECT COUNT(*) FROM bet_contract_witness WHERE contract_id = #{contractId}")
    int countByContractId(@Param("contractId") Long contractId);
}

