package com.spring.teamwork.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
//分页
public class PageVO<T> {
    private Long total;
    private Long page;
    private Long size;
    private List<T> records;

    public static <T> PageVO<T> of(IPage<T> p){
        return new PageVO<>(p.getTotal(), p.getCurrent(), p.getSize(), p.getRecords());
    }



}
