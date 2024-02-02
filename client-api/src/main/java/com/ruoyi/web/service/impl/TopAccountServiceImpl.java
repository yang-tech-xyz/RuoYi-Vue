package com.ruoyi.web.service.impl;

import com.ruoyi.web.mapper.TopAccountMapper;
import com.ruoyi.web.service.TopAccountService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TopAccountServiceImpl implements TopAccountService {
    
    @Autowired
    private TopAccountMapper mapper;

}
