package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AdminAddDTO;
import com.ruoyi.web.dto.AdminLoginDTO;
import com.ruoyi.web.dto.AdminUpdateDTO;
import com.ruoyi.web.entity.TopAdmin;
import com.ruoyi.web.enums.Status;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopAdminMapper;
import com.ruoyi.web.otp.OtpAuthenticator;
import com.ruoyi.web.utils.LoginUtil;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.utils.StringUtils;
import com.ruoyi.web.vo.AdminLoginVO;
import com.ruoyi.web.vo.AdminVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopAdminService extends ServiceImpl<TopAdminMapper, TopAdmin> {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public AdminLoginVO login(AdminLoginDTO dto) {
        TopAdmin admin = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopAdmin>()
                        .eq(TopAdmin::getAccount, dto.getAccount())))
                .orElseThrow(() -> new ServiceException("账号或密码错误", 500));
        if (!admin.getStatus().equals(Status._1._value)) {
            throw new ServiceException("状态错误", 500);
        }
        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new ServiceException("账号或密码错误", 500);
        }
        if (!OtpAuthenticator.checkCode(admin.getGoogleSecret(), dto.getGoogleCode())) {
            throw new ServiceException("谷歌验证码错误", 500);
        }
        AdminLoginVO loginVO = new AdminLoginVO();
        loginVO.setToken(UUID.fastUUID().toString(true));
        LoginUtil.loginMap.put(loginVO.getToken(), admin.getAccount());
        return loginVO;
    }

    public List<AdminVO> getList() {
        return baseMapper.selectListVO();
    }

    public Boolean add(AdminAddDTO dto) {
        TopAdmin admin = new TopAdmin();
        BeanUtils.copyProperties(dto, admin);
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setCreatedBy(RequestUtil.getAdminId());
        admin.setUpdatedDate(LocalDateTime.now());
        admin.setUpdatedBy(RequestUtil.getAdminId());
        admin.setUpdatedDate(LocalDateTime.now());
        baseMapper.insert(admin);
        return true;
    }

    public Boolean edit(AdminUpdateDTO dto) {
        TopAdmin admin = baseMapper.selectOne(new LambdaQueryWrapper<TopAdmin>().eq(TopAdmin::getAccount, dto.getAccount()));
        if (StringUtils.isNotBlank(dto.getPassword())) {
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        admin.setGoogleSecret(dto.getGoogleSecret());
        admin.setStatus(dto.getStatus());
        admin.setCreatedBy(RequestUtil.getAdminId());
        admin.setUpdatedDate(LocalDateTime.now());
        admin.setUpdatedBy(RequestUtil.getAdminId());
        admin.setUpdatedDate(LocalDateTime.now());
        baseMapper.insert(admin);
        return true;
    }

    public String getGoogleSecret(String account) {
        return OtpAuthenticator.secretKey();
    }

}