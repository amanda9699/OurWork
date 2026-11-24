package com.spring.teamwork.Service;

import com.spring.teamwork.model.DTO.ContractCreateRequest;
import com.spring.teamwork.model.Result;
import com.spring.teamwork.model.VO.ContractDetailVO;

public interface ContractService {
    Result<ContractDetailVO> create(ContractCreateRequest request, Long userId);
}
