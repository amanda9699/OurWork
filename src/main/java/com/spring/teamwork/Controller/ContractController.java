package com.spring.teamwork.Controller;

import com.spring.teamwork.Service.ContractService;
import com.spring.teamwork.model.DTO.ContractCreateRequest;
import com.spring.teamwork.model.Enum.ContractStatus;
import com.spring.teamwork.model.PageVO;
import com.spring.teamwork.model.Result;
import com.spring.teamwork.model.VO.ContractDetailVO;
import com.spring.teamwork.model.VO.ContractListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 合约模块
 */
@RestController
@RequestMapping("/api")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @PostMapping("/contracts")
    public ResponseEntity<Result<ContractDetailVO>> createContract(@RequestBody @Validated ContractCreateRequest request){
        Long userId = 1L;
        Result<ContractDetailVO> result = contractService.create(request,userId);
        if(result.getCode() == 0){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }

    @GetMapping("/contracts/my")
    public ResponseEntity<Result<PageVO<ContractListVO>>> myContract(
            @RequestParam(required = false) ContractStatus status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
        Long userId = 1L;
        size = Math.min(size, 50);
        return ResponseEntity.ok(contractService.getMyContract(status, page, size, userId));
    }

    @GetMapping("/contracts/{contractId}")
    public ResponseEntity<Result<ContractDetailVO>> contractDetail(@PathVariable Integer contractId){
        return ResponseEntity.ok(contractService.getContractDetail(contractId));
    }
}
