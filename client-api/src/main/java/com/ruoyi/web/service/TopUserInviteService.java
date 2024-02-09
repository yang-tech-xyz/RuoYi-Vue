package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.entity.TopUserInvite;
import com.ruoyi.web.mapper.TopUserInviteMapper;
import com.ruoyi.web.mapper.TopUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopUserInviteService extends ServiceImpl<TopUserInviteMapper, TopUserInvite> {

    @Autowired
    private TopUserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public void process(Long userId, Long parentId) {
        List<Long> parents = getSpreadParent(parentId);
        for (int i = 0; i < parents.size(); i++) {
            Optional<TopUserInvite> optional = Optional.ofNullable(baseMapper.selectInvite(parents.get(i), userId));
            if (optional.isEmpty()) {
                TopUserInvite referral = new TopUserInvite();
                referral.setUserId(parents.get(i));
                referral.setInviteUserId(userId);
                referral.setLevel(i + 1);
                referral.setCreatedDate(LocalDateTime.now());
                referral.setCreatedBy(userId.toString());
                referral.setUpdatedDate(LocalDateTime.now());
                referral.setUpdatedBy(userId.toString());
                baseMapper.insert(referral);
            }
        }
    }

    public List<Long> getSpreadParent(Long parentId) {
        List<Long> parentList = new ArrayList<>();
        TopUserEntity parent = userMapper.selectParent(parentId);
        if (null == parent) {
            return parentList;
        }
        parentList.add(parent.getId());
        parentList.addAll(getSpreadParent(parent.getInvitedUserId()));
        return parentList;
    }
}
