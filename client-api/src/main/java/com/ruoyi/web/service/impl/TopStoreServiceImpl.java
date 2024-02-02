package com.ruoyi.web.service.impl;

import com.ruoyi.web.mapper.TopStoreMapper;
import com.ruoyi.web.service.TopStoreService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TopStoreServiceImpl implements TopStoreService {
    
    @Autowired
    private TopStoreMapper mapper;

}
