package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AdminLoginDTO;
import com.ruoyi.web.entity.TopAdmin;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopAdminMapper;
import com.ruoyi.web.utils.LoginUtil;
import com.ruoyi.web.vo.AdminLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TopAdminService extends ServiceImpl<TopAdminMapper, TopAdmin> {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public AdminLoginVO login(AdminLoginDTO dto) {
        TopAdmin admin = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopAdmin>()
                        .eq(TopAdmin::getAccount, dto.getAccount())))
                .orElseThrow(() -> new ServiceException("账号或密码错误", 500));
        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new ServiceException("账号或密码错误", 500);
        }
        AdminLoginVO loginVO = new AdminLoginVO();
        loginVO.setToken(UUID.fastUUID().toString(true));
        LoginUtil.loginMap.put(loginVO.getToken(), admin.getAccount());
        return loginVO;
    }
}
