package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.entity.TopTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TopTransactionMapper extends BaseMapper<TopTransaction> {

    @Update("update top_transaction set is_confirm = 1 and `status`='0x1' where id = #{id}")
    void updateConfirm(Long id);

    @Update("update top_transaction set is_confirm = 2, `status`='0x2' where hash = #{hash}")
    void updateFailed(String hash);
}
