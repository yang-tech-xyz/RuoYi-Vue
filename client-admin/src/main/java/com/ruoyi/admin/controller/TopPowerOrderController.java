package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.PowerOrderPageDTO;
import com.ruoyi.admin.service.TopPowerOrderService;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.PowerOrderVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 充值
 */
@Tag(description = "算力订单", name = "算力订单")
@RestController
@RequestMapping("powerOrder")
public class TopPowerOrderController {

    @Autowired
    private TopPowerOrderService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<PowerOrderVO>> getPage(@ModelAttribute PowerOrderPageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }

}
