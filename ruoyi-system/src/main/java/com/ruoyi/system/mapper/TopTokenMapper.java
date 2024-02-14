package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TopToken;

/**
 * 币种配置Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-13
 */
public interface TopTokenMapper 
{
    /**
     * 查询币种配置
     * 
     * @param id 币种配置主键
     * @return 币种配置
     */
    public TopToken selectTopTokenById(Long id);

    /**
     * 查询币种配置列表
     * 
     * @param topToken 币种配置
     * @return 币种配置集合
     */
    public List<TopToken> selectTopTokenList(TopToken topToken);

    /**
     * 新增币种配置
     * 
     * @param topToken 币种配置
     * @return 结果
     */
    public int insertTopToken(TopToken topToken);

    /**
     * 修改币种配置
     * 
     * @param topToken 币种配置
     * @return 结果
     */
    public int updateTopToken(TopToken topToken);

    /**
     * 删除币种配置
     * 
     * @param id 币种配置主键
     * @return 结果
     */
    public int deleteTopTokenById(Long id);

    /**
     * 批量删除币种配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopTokenByIds(Long[] ids);
}
