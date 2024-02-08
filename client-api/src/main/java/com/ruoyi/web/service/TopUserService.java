package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.mapper.TopUserMapper;
import com.ruoyi.web.vo.InviteInfoVO;
import com.ruoyi.web.vo.InviteVO;
import com.ruoyi.web.vo.UserProcessVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopUserService extends ServiceImpl<TopUserMapper, TopUserEntity> {
    public Optional<TopUserEntity> getByWallet(String walletAddress) {
        LambdaQueryWrapper<TopUserEntity> queryWallet = Wrappers.lambdaQuery();
        queryWallet.eq(TopUserEntity::getWallet, walletAddress);
        return this.getOneOpt(queryWallet);
    }

    public Optional<TopUserEntity> getByInviteCode(String invitedCode) {
        LambdaQueryWrapper<TopUserEntity> query = Wrappers.lambdaQuery();
        query.eq(TopUserEntity::getInvitedCode, invitedCode);
        return this.getOneOpt(query);
    }

    public List<UserProcessVO> getUserVOList() {
        return baseMapper.selectUserVOList();
    }

    /**
     * 处理用户关系
     */
    public void process(List<UserProcessVO> userVOList) {
        for (int i = 0; i < userVOList.size(); i++) {
            UserProcessVO userVO = userVOList.get(i);
            userVO.setDirectChildMebList(getDirectChildMebList(userVO.getId(), userVOList));
        }
    }

    public List<UserProcessVO> getDirectChildMebList(Long userId, List<UserProcessVO> processMebList) {
        return processMebList.stream().filter(v -> v.getInvitedUserId().equals(userId)).collect(Collectors.toList());
    }

    /**
     * 查询用户邀请数据
     */
    public InviteInfoVO getInviteInfo(String walletAddress) {
        return new InviteInfoVO();
    }

    public List<InviteVO> getInviteList(String walletAddress) {
        Optional<TopUserEntity> optional = getByWallet(walletAddress);
        if (optional.isEmpty()) {
            return new ArrayList<>();
        }
        return baseMapper.selectInviteListById(optional.get().getId());
    }
}
