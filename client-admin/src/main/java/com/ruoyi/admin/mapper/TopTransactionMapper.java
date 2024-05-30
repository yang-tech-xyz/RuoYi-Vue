package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.TopTransactionDTO;
import com.ruoyi.admin.entity.TopTransaction;
import com.ruoyi.admin.vo.TopTransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TopTransactionMapper extends BaseMapper<TopTransaction> {

    @Update("update top_transaction set is_confirm = 1 and `status`='0x1' where id = #{id}")
    void updateConfirm(Long id);

    @Update("update top_transaction set is_confirm = 2, `status`='0x2' where hash = #{hash}")
    void updateFailed(String hash);

    IPage<TopTransactionVO> selectPageVOByDTO(@Param("page") IPage<TopTransactionVO> page, @Param("dto") TopTransactionDTO dto);
}
