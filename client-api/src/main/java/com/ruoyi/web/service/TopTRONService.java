package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.proto.Response;

@Slf4j
@Service
public class TopTRONService extends ServiceImpl<TopUserMapper, TopUser> {
    public void queryTransactionInfoByHash(ApiWrapper wrapper, String hash){
        try{
//            System.setProperty("http.proxySet", "true");
//            System.setProperty("proxyHost", "127.0.0.1"); // PROXY_HOST：代理的IP地址
//            System.setProperty("proxyPort",  "10080"); // PROXY_PORT：代理的端口号
            Response.TransactionInfo transactionInfoById = wrapper.getTransactionInfoById(hash);
            log.info("transactionInfoById is:{}",transactionInfoById);
        }catch (Exception e){
            log.error("query tron transaction error",e);
            throw new ServiceException(e.getMessage());
        }finally {
            wrapper.close();
        }
    }
}
