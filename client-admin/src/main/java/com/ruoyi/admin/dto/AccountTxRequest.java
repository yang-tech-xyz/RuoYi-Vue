package com.ruoyi.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTxRequest {
    private String txNo;
    private List<AccountTxRequestDetail> details = new ArrayList<>();

    public void addDetail(AccountTxRequestDetail requestDetail) {
        this.details.add(requestDetail);
    }
}