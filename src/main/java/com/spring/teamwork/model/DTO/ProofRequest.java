package com.spring.teamwork.model.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ProofRequest {
    @Size(max = 500)
    private String content;
    private List<String> images;
}
