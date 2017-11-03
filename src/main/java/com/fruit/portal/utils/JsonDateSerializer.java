/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.utils;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * 当通过Jackson将Date类型的数据转换为JSON格式时， 使用@JsonSerialize(using = JsonDateSerializer.class)注解可将Date类型数据格式化为yyyy-MM-dd格式
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-15
 * Project        : message-biz
 * File Name      : JsonDateSerializer.java
 */
public class JsonDateSerializer extends JsonSerializer<Date>
{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException
    {
        String formattedDate = dateFormat.format(date);
        jsonGenerator.writeString(formattedDate);
    }
}
