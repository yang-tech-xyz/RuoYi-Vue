package com.ruoyi.web.service.impl;

import com.ruoyi.web.mapper.TopAccountTxMapper;
import com.ruoyi.web.service.TopAccountTxService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TopAccountTxServiceImpl implements TopAccountTxService {
    
    @Autowired
    private TopAccountTxMapper mapper;

}
