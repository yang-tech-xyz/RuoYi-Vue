package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.InvitePageDTO;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopUserMapper;
import com.ruoyi.web.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        if (!topUserEntityOptional.isPresent()) {
            log.error("user not exist,the wallet is:{}", walletAddress);
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

    public TopUser lockById(Long id) {
        return baseMapper.lockById(id);
    }

    /**
     * 算力推广
     * 1:限制十层用户
     */
    public PowerInviteInfoVO getPowerInviteInfo(String wallet) {
        return baseMapper.selectPowerInviteInfo(wallet);
    }

    /**
     * 算力推广
     * 1:限制十层用户
     */
    public PageVO<PowerInviteVO> getPowerInvitePage(String wallet, InvitePageDTO dto) {
        TopUser user = baseMapper.selectByWallet(wallet);
        IPage<PowerInviteVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPowerPageVO(iPage, user.getId());
        PageVO<PowerInviteVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }

    /**
     * 理财推广
     * 1:限制一层用户
     */
    public StoreInviteInfoVO getStoreInviteInfo(String wallet) {
        return baseMapper.selectStoreInviteInfo(wallet);
    }

    /**
     * 理财推广
     * 1:限制一层用户
     */
    public PageVO<StoreInviteVO> getStoreInvitePage(String wallet, InvitePageDTO dto) {
        TopUser user = baseMapper.selectByWallet(wallet);
        IPage<StoreInviteVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectStorePageVO(iPage, user.getId());
        PageVO<StoreInviteVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
