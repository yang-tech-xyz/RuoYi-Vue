package com.ruoyi.admin.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TopHomeMapper {

    BigDecimal depositBySymbolAndDate(@Param("symbol") String symbol, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    BigDecimal withdrawBySymbolAndDate(@Param("symbol") String symbol, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Integer memberCountByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
