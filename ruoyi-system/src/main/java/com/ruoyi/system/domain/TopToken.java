package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 币种配置对象 top_token
 * 
 * @author ruoyi
 * @date 2024-02-13
 */
public class TopToken extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** token 名称 */
    @Excel(name = "token 名称")
    private String symbol;

    /** 小数位 */
    @Excel(name = "小数位")
    private Long decimals;

    /** 是否上线: 0,上线,1下线 */
    @Excel(name = "是否上线: 0,上线,1下线")
    private Long online;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSymbol(String symbol) 
    {
        this.symbol = symbol;
    }

    public String getSymbol() 
    {
        return symbol;
    }
    public void setDecimals(Long decimals) 
    {
        this.decimals = decimals;
    }

    public Long getDecimals() 
    {
        return decimals;
    }
    public void setOnline(Long online) 
    {
        this.online = online;
    }

    public Long getOnline() 
    {
        return online;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("symbol", getSymbol())
            .append("decimals", getDecimals())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("online", getOnline())
            .toString();
    }
}
