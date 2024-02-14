package com.ruoyi.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TopToken;
import com.ruoyi.system.service.ITopTokenService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 币种配置Controller
 * 
 * @author ruoyi
 * @date 2024-02-13
 */
@RestController
@RequestMapping("/system/token")
public class TopTokenController extends BaseController
{
    @Autowired
    private ITopTokenService topTokenService;

    /**
     * 查询币种配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:token:list')")
    @GetMapping("/list")
    public TableDataInfo list(TopToken topToken)
    {
        startPage();
        List<TopToken> list = topTokenService.selectTopTokenList(topToken);
        return getDataTable(list);
    }

    /**
     * 导出币种配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:token:export')")
    @Log(title = "币种配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TopToken topToken)
    {
        List<TopToken> list = topTokenService.selectTopTokenList(topToken);
        ExcelUtil<TopToken> util = new ExcelUtil<TopToken>(TopToken.class);
        util.exportExcel(response, list, "币种配置数据");
    }

    /**
     * 获取币种配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:token:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(topTokenService.selectTopTokenById(id));
    }

    /**
     * 新增币种配置
     */
    @PreAuthorize("@ss.hasPermi('system:token:add')")
    @Log(title = "币种配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TopToken topToken)
    {
        return toAjax(topTokenService.insertTopToken(topToken));
    }

    /**
     * 修改币种配置
     */
    @PreAuthorize("@ss.hasPermi('system:token:edit')")
    @Log(title = "币种配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TopToken topToken)
    {
        return toAjax(topTokenService.updateTopToken(topToken));
    }

    /**
     * 删除币种配置
     */
    @PreAuthorize("@ss.hasPermi('system:token:remove')")
    @Log(title = "币种配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(topTokenService.deleteTopTokenByIds(ids));
    }
}
