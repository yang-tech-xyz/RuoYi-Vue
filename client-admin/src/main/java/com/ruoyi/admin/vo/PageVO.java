package com.ruoyi.admin.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageVO<T> {
    private int pageNum = 1;
    private int pageSize = 20;

    @JsonSerialize()
    private long total;
    private List<T> list;


    public PageVO() {
    }

    public PageVO(Integer pageNo, Integer pageSize) {
        this.list = new ArrayList<>();
        this.total = 0;
        if (pageNo != null) {
            this.pageNum = pageNo;
        }
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
    }

    public PageVO(List<T> list, long total, int pageNum, int pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return this.total == 0 ? 1 : (int) Math.ceil((double) this.total / (double) this.getPageSize());
    }
}