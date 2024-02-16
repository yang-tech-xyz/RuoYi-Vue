package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopUserMapper;
import com.ruoyi.web.vo.InviteInfoVO;
import com.ruoyi.web.vo.InviteVO;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopUserService extends ServiceImpl<TopUserMapper, TopUser> {
    public TopUser getByWallet(String walletAddress) {
        LambdaQueryWrapper<TopUser> queryWallet = Wrappers.lambdaQuery();
        queryWallet.eq(TopUser::getWallet, walletAddress);
        Optional<TopUser> topUserEntityOptional = this.getOneOpt(queryWallet);
        if(!topUserEntityOptional.isPresent()){
            log.error("user not exist,the wallet is:{}",walletAddress);
            throw new ServiceException("user not exist!");
        }
        return topUserEntityOptional.get();
    }

    public Optional<TopUser> getByWalletOptional(String walletAddress) {
        LambdaQueryWrapper<TopUser> queryWallet = Wrappers.lambdaQuery();
        queryWallet.eq(TopUser::getWallet, walletAddress);
        return this.getOneOpt(queryWallet);
    }

    public Optional<TopUser> getByInviteCode(String invitedCode) {
        LambdaQueryWrapper<TopUser> query = Wrappers.lambdaQuery();
        query.eq(TopUser::getInvitedCode, invitedCode);
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
        return baseMapper.selectInviteInfo(walletAddress);
    }

    public List<InviteVO> getInviteList(String walletAddress) {
        Optional<TopUser> optional = getByWalletOptional(walletAddress);
        if (optional.isEmpty()) {
            return new ArrayList<>();
        }
        return baseMapper.selectInviteListById(optional.get().getId());
    }
}
