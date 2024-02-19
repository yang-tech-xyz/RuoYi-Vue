package com.ruoyi.admin.config;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * BigDecimal序列化类：为了解决数据在前端小数位不统一的问题
 */
public class ToBigDecimalSerializer extends StdSerializer<BigDecimal> {

    public final static ToBigDecimalSerializer instance = new ToBigDecimalSerializer();

    public ToBigDecimalSerializer() {
        super(BigDecimal.class);
    }

    protected ToBigDecimalSerializer(Class<BigDecimal> t) {
        super(t);
    }

    protected ToBigDecimalSerializer(JavaType type) {
        super(type);
    }

    protected ToBigDecimalSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected ToBigDecimalSerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.stripTrailingZeros().toPlainString());
    }
}
