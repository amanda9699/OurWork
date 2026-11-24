package com.spring.teamwork.Service;

import com.spring.teamwork.model.DTO.ContractCreateRequest;
import com.spring.teamwork.model.PageVO;
import com.spring.teamwork.model.Result;
import com.spring.teamwork.model.VO.ContractDetailVO;
import com.spring.teamwork.model.VO.ContractListVO;

public interface ContractService {
    Result<ContractDetailVO> create(ContractCreateRequest request, Long userId);

    Result<PageVO<ContractListVO>> getMyContract(String status, Integer page, Integer size, Long userId);
}
