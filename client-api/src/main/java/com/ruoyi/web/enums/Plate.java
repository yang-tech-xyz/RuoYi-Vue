package com.ruoyi.web.enums;

public enum Plate {

    // https://api.gateio.ws/api/v4/spot/tickers?currency_pair=GT_USDT
    GATE_IO("GATE_IO", "https://api.gateio.ws"),
    // https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT
    BINANCE("BINANCE", "https://api.binance.com"),
    // https://www.okx.com/api/v5/market/ticker?instId=BTC-USD-SWAP
    OKX("OKX", "https://www.okx.com"),
    // https://api.huobi.pro/market/detail/merged?symbol=btcusdt
    HUO_BI("HUO_BI", "https://api.huobi.pro"),

    ;
    public final String _code;
    public final String _url;

    Plate(String _code, String _url) {
        this._code = _code;
        this._url = _url;
    }
}
