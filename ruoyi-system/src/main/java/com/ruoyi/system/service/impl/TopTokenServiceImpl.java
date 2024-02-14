package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TopTokenMapper;
import com.ruoyi.system.domain.TopToken;
import com.ruoyi.system.service.ITopTokenService;

/**
 * 币种配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-02-13
 */
@Service
public class TopTokenServiceImpl implements ITopTokenService 
{
    @Autowired
    private TopTokenMapper topTokenMapper;

    /**
     * 查询币种配置
     * 
     * @param id 币种配置主键
     * @return 币种配置
     */
    @Override
    public TopToken selectTopTokenById(Long id)
    {
        return topTokenMapper.selectTopTokenById(id);
    }

    /**
     * 查询币种配置列表
     * 
     * @param topToken 币种配置
     * @return 币种配置
     */
    @Override
    public List<TopToken> selectTopTokenList(TopToken topToken)
    {
        return topTokenMapper.selectTopTokenList(topToken);
    }

    /**
     * 新增币种配置
     * 
     * @param topToken 币种配置
     * @return 结果
     */
    @Override
    public int insertTopToken(TopToken topToken)
    {
        topToken.setCreateTime(DateUtils.getNowDate());
        return topTokenMapper.insertTopToken(topToken);
    }

    /**
     * 修改币种配置
     * 
     * @param topToken 币种配置
     * @return 结果
     */
    @Override
    public int updateTopToken(TopToken topToken)
    {
        topToken.setUpdateTime(DateUtils.getNowDate());
        return topTokenMapper.updateTopToken(topToken);
    }

    /**
     * 批量删除币种配置
     * 
     * @param ids 需要删除的币种配置主键
     * @return 结果
     */
    @Override
    public int deleteTopTokenByIds(Long[] ids)
    {
        return topTokenMapper.deleteTopTokenByIds(ids);
    }

    /**
     * 删除币种配置信息
     * 
     * @param id 币种配置主键
     * @return 结果
     */
    @Override
    public int deleteTopTokenById(Long id)
    {
        return topTokenMapper.deleteTopTokenById(id);
    }
}
