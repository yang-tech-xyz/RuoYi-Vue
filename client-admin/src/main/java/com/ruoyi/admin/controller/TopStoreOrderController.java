package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.AccountTxPageDTO;
import com.ruoyi.admin.dto.StoreOrderPageDTO;
import com.ruoyi.admin.service.TopStoreOrderService;
import com.ruoyi.admin.vo.AccountTxVO;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.StoreOrderVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "存币生息订单", name = "存币生息订单")
@RestController
@RequestMapping("topStoreOrder")
public class TopStoreOrderController {

    @Autowired
    private TopStoreOrderService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<StoreOrderVO>> getPage(@ModelAttribute StoreOrderPageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }

}
