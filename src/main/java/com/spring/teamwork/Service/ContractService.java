package com.spring.teamwork.Service;

import com.spring.teamwork.model.DTO.ContractCreateRequest;
import com.spring.teamwork.model.DTO.ProofRequest;
import com.spring.teamwork.model.Enum.ContractStatus;
import com.spring.teamwork.model.PageVO;
import com.spring.teamwork.model.Result;
import com.spring.teamwork.model.VO.AcceptInviteVO;
import com.spring.teamwork.model.VO.ContractDetailVO;
import com.spring.teamwork.model.VO.ContractListVO;
import com.spring.teamwork.model.VO.WitnessInviteVO;

public interface ContractService {
    Result<ContractDetailVO> create(ContractCreateRequest request, Long userId);

    Result<PageVO<ContractListVO>> getMyContract(ContractStatus status, Integer page, Integer size, Long userId);

    Result<ContractDetailVO> getContractDetail(Integer contractId);

    Result<WitnessInviteVO> createInviteUrl(Integer contractId, Long userId);

    Result<AcceptInviteVO> accept(String inviteCode, Long userId);

    Result<ContractDetailVO.ProofVO> submitProof(Long contractId, ProofRequest request, Long userId);
}
