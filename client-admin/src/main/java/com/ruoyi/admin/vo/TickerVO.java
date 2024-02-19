package com.ruoyi.admin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class TickerVO {

    @JsonProperty("currency_pair")
    private String currencyPair;

    @JsonProperty("last")
    private BigDecimal last;

    @JsonProperty("lowest_ask")
    private BigDecimal lowestAsk;

    @JsonProperty("highest_bid")
    private BigDecimal highestBid;

    @JsonProperty("change_percentage")
    private BigDecimal changePercentage;

    @JsonProperty("base_volume")
    private BigDecimal baseVolume;

    @JsonProperty("quote_volume")
    private BigDecimal quoteVolume;

    @JsonProperty("high_24h")
    private BigDecimal high24h;

    @JsonProperty("low_24h")
    private BigDecimal low24h;

}
