package com.ruoyi.system.service.virtualservice.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.config.DeepSeekProperties;
import com.ruoyi.system.service.virtualservice.IDeepSeekStreamClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Component
public class DeepSeekStreamClientImpl implements IDeepSeekStreamClient
{
    private static final Logger log = LoggerFactory.getLogger(DeepSeekStreamClientImpl.class);

    private static final int DEFAULT_TIMEOUT_SECONDS = 60;

    private static final int MAX_TIMEOUT_SECONDS = 300;

    private static final int ERROR_BODY_SUMMARY_LIMIT = 1200;

    private final DeepSeekProperties properties;

    public DeepSeekStreamClientImpl(DeepSeekProperties properties)
    {
        this.properties = properties;
    }

    @Override
    public void streamIntro(String prompt, Consumer<String> onChunk) throws Exception
    {
        if (!properties.isConfigured())
        {
            throw new IllegalStateException("未配置 DeepSeek API Key");
        }

        String requestUrl = trimEnd(properties.getBaseUrl()) + "/chat/completions";
        HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
        try
        {
            int timeoutMillis = resolveTimeoutMillis(properties.getTimeoutSeconds());
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(timeoutMillis);
            connection.setReadTimeout(timeoutMillis);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + properties.getApiKey());
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "text/event-stream");

            byte[] body = buildRequestBody(prompt).getBytes(StandardCharsets.UTF_8);
            try (OutputStream outputStream = connection.getOutputStream())
            {
                outputStream.write(body);
            }

            int status = connection.getResponseCode();
            if (status < HttpURLConnection.HTTP_OK || status >= HttpURLConnection.HTTP_MULT_CHOICE)
            {
                String errorBody = readAll(connection.getErrorStream());
                log.error("DeepSeek stream request failed, status={}, body={}", status, sanitizeForLog(errorBody));
                throw new IllegalStateException("AI生成失败，请稍后重试");
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (StringUtils.isBlank(line) || line.startsWith(":"))
                    {
                        continue;
                    }
                    if (line.startsWith("data:") && StringUtils.equals("[DONE]", line.substring(5).trim()))
                    {
                        return;
                    }
                    handleSseLine(line, onChunk);
                }
            }
        }
        finally
        {
            connection.disconnect();
        }
    }

    String buildRequestBody(String prompt)
    {
        JSONObject body = new JSONObject();
        body.put("model", properties.getModel());
        body.put("stream", true);
        body.put("temperature", 0.7);
        body.put("max_tokens", 512);

        JSONObject thinking = new JSONObject();
        thinking.put("type", "disabled");
        body.put("thinking", thinking);

        JSONArray messages = new JSONArray();
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是虚拟仿真训练平台的教学内容助手，请生成专业、准确、适合平台展示的简介。");
        messages.add(systemMessage);

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        body.put("messages", messages);

        return JSON.toJSONString(body);
    }

    void handleSseLine(String line, Consumer<String> onChunk)
    {
        if (!line.startsWith("data:"))
        {
            return;
        }

        String data = line.substring(5).trim();
        if (StringUtils.isBlank(data) || StringUtils.equals("[DONE]", data))
        {
            return;
        }

        String content;
        try
        {
            JSONObject payload = JSON.parseObject(data);
            JSONArray choices = payload.getJSONArray("choices");
            if (choices == null || choices.isEmpty())
            {
                return;
            }

            JSONObject choice = choices.getJSONObject(0);
            if (choice == null)
            {
                return;
            }

            JSONObject delta = choice.getJSONObject("delta");
            if (delta == null)
            {
                return;
            }

            content = delta.getString("content");
        }
        catch (RuntimeException e)
        {
            log.debug("Ignore invalid DeepSeek SSE data, error={}, data={}", e.getClass().getSimpleName(),
                    sanitizeForLog(data));
            return;
        }

        if (StringUtils.isNotBlank(content))
        {
            onChunk.accept(content);
        }
    }

    int resolveTimeoutMillis(Integer timeoutSeconds)
    {
        int seconds = timeoutSeconds == null || timeoutSeconds <= 0 ? DEFAULT_TIMEOUT_SECONDS : timeoutSeconds;
        seconds = Math.min(seconds, MAX_TIMEOUT_SECONDS);
        long timeoutMillis = seconds * 1000L;
        return (int) timeoutMillis;
    }

    String sanitizeForLog(String content)
    {
        if (content == null)
        {
            return "";
        }
        String summary = content;
        if (summary.length() > ERROR_BODY_SUMMARY_LIMIT)
        {
            summary = summary.substring(0, ERROR_BODY_SUMMARY_LIMIT) + "...";
        }
        summary = summary.replaceAll("(?i)Bearer\\s+[^\\s,;\"'}]+", "Bearer ***");
        summary = summary.replaceAll("(?i)(Authorization\\s*[:=]\\s*)([^\\s,;\"'}]+)", "$1***");
        summary = summary.replaceAll("(?i)(api[_-]?key[\"']?\\s*[:=]\\s*[\"']?)([^\\s,\"'}]+)", "$1***");
        return summary;
    }

    private String trimEnd(String baseUrl)
    {
        if (StringUtils.isBlank(baseUrl))
        {
            return "https://api.deepseek.com";
        }
        String result = baseUrl.trim();
        while (result.endsWith("/"))
        {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String readAll(InputStream inputStream) throws Exception
    {
        if (inputStream == null)
        {
            return "";
        }
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                content.append(line);
            }
        }
        return content.toString();
    }
}
