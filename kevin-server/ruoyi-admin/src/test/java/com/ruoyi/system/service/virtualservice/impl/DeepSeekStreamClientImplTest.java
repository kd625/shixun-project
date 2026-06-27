package com.ruoyi.system.service.virtualservice.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.config.DeepSeekProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DeepSeekStreamClientImplTest
{
    @Test
    public void buildRequestBodyShouldIncludeStreamOptionsAndMessages()
    {
        DeepSeekProperties properties = new DeepSeekProperties();
        properties.setModel("deepseek-test");
        DeepSeekStreamClientImpl client = new DeepSeekStreamClientImpl(properties);

        JSONObject body = JSON.parseObject(client.buildRequestBody("生成资源简介"));

        Assertions.assertEquals("deepseek-test", body.getString("model"));
        Assertions.assertTrue(body.getBooleanValue("stream"));
        Assertions.assertEquals("disabled", body.getJSONObject("thinking").getString("type"));
        JSONArray messages = body.getJSONArray("messages");
        Assertions.assertEquals(2, messages.size());
        Assertions.assertEquals("system", messages.getJSONObject(0).getString("role"));
        Assertions.assertEquals("user", messages.getJSONObject(1).getString("role"));
        Assertions.assertEquals("生成资源简介", messages.getJSONObject(1).getString("content"));
    }

    @Test
    public void handleSseLineShouldCallbackContentChunk()
    {
        DeepSeekStreamClientImpl client = new DeepSeekStreamClientImpl(new DeepSeekProperties());
        List<String> chunks = new ArrayList<String>();

        client.handleSseLine("data: {\"choices\":[{\"delta\":{\"content\":\"简介片段\"}}]}", chunks::add);

        Assertions.assertEquals(1, chunks.size());
        Assertions.assertEquals("简介片段", chunks.get(0));
    }

    @Test
    public void handleSseLineShouldIgnoreNonContentChunks()
    {
        DeepSeekStreamClientImpl client = new DeepSeekStreamClientImpl(new DeepSeekProperties());
        List<String> chunks = new ArrayList<String>();

        Assertions.assertDoesNotThrow(() -> client.handleSseLine("data: {\"choices\":[{\"delta\":{\"role\":\"assistant\"}}]}", chunks::add));
        Assertions.assertDoesNotThrow(() -> client.handleSseLine("data: {\"choices\":[{\"delta\":{}}]}", chunks::add));
        Assertions.assertDoesNotThrow(() -> client.handleSseLine("data: [DONE]", chunks::add));
        Assertions.assertDoesNotThrow(() -> client.handleSseLine("data: not-json", chunks::add));

        Assertions.assertTrue(chunks.isEmpty());
    }

    @Test
    public void resolveTimeoutMillisShouldClampInvalidAndLargeValues()
    {
        DeepSeekStreamClientImpl client = new DeepSeekStreamClientImpl(new DeepSeekProperties());

        Assertions.assertEquals(60000, client.resolveTimeoutMillis(null));
        Assertions.assertEquals(60000, client.resolveTimeoutMillis(0));
        Assertions.assertEquals(60000, client.resolveTimeoutMillis(-1));
        Assertions.assertEquals(300000, client.resolveTimeoutMillis(301));
        Assertions.assertEquals(300000, client.resolveTimeoutMillis(Integer.MAX_VALUE));
        Assertions.assertEquals(120000, client.resolveTimeoutMillis(120));
    }

    @Test
    public void sanitizeForLogShouldMaskSecretsAndLimitLength()
    {
        DeepSeekStreamClientImpl client = new DeepSeekStreamClientImpl(new DeepSeekProperties());
        StringBuilder body = new StringBuilder();
        body.append("Authorization: Bearer sk-secret ");
        body.append("\"api_key\":\"secret-key\" ");
        body.append("Bearer another-secret ");
        for (int i = 0; i < 1300; i++)
        {
            body.append('x');
        }

        String summary = client.sanitizeForLog(body.toString());

        Assertions.assertFalse(summary.contains("sk-secret"));
        Assertions.assertFalse(summary.contains("secret-key"));
        Assertions.assertFalse(summary.contains("another-secret"));
        Assertions.assertTrue(summary.contains("Bearer ***"));
        Assertions.assertTrue(summary.length() <= 1203);
    }
}
