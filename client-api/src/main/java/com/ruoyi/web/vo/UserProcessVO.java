package com.ruoyi.web.vo;

import com.ruoyi.web.entity.TopPowerOrder;
import com.ruoyi.web.entity.TopPowerOrderIncome;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserProcessVO {

    private Long id;

    private String wallet;

    private String invitedCode;

    private Long invitedUserId;

    private String invitedUserCode;

    private Integer powerNumber = 0;

    private ArrayList<TopPowerOrder> powerOrders;

    private ArrayList<TopPowerOrderIncome> incomeOrders;

    private List<UserProcessVO> directChildMebList = new ArrayList<>();

}