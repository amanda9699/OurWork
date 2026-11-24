package com.spring.teamwork.Controller;

import com.spring.teamwork.Service.ContractService;
import com.spring.teamwork.model.DTO.ContractCreateRequest;
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

    @GetMapping("/contracts//contracts/my")
    public ResponseEntity<Result<PageVO<ContractListVO>>> myContract(@RequestParam String status,@RequestParam Integer page,
                                                             @RequestParam Integer size){
        Long userId = 1L;
        Result<PageVO<ContractListVO>> result = contractService.getMyContract(status,page,size,userId);
        if(result.getCode() == 0){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(result.getCode()).body(result);
    }
}
