package com.ruoyi.web.service.impl;

import com.ruoyi.web.mapper.TopStoreIncomeMapper;
import com.ruoyi.web.service.TopStoreIncomeService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TopStoreIncomeServiceImpl implements TopStoreIncomeService {
    
    @Autowired
    private TopStoreIncomeMapper mapper;

}
