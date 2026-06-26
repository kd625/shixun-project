# DeepSeek 流式简介生成 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为虚拟仿真实训平台的“实训资源”和“实训实验”表单增加 DeepSeek SSE 流式生成简介能力。

**Architecture:** 后端新增独立 `/virtual/ai/generateIntro/stream` SSE 接口，不改变资源/实验原 CRUD。`VtAiServiceImpl` 负责校验、prompt 组装和 `SseEmitter` 发送，`DeepSeekStreamClientImpl` 只负责 DeepSeek Chat Completions 流式 HTTP 调用。前端用 `fetch + ReadableStream` 发 POST JSON 并解析 SSE 事件，两个页面只接入按钮、状态和字段映射。

**Tech Stack:** Java 8, Spring Boot 2.5, Spring MVC `SseEmitter`, RuoYi Vue 2, Element UI, Fastjson2, Maven, Jest-free frontend build validation.

---

## File Structure

- Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtAiIntroRequest.java`
  - 前端请求 DTO，只承载生成简介所需字段。
- Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/config/DeepSeekProperties.java`
  - 绑定 `deepseek.*` 配置，提供默认 baseUrl/model/timeout。
- Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IDeepSeekStreamClient.java`
  - DeepSeek 流式客户端接口，便于 service 单测替换 fake client。
- Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IVtAiService.java`
  - AI 简介生成 service 接口。
- Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/VtAiServiceImpl.java`
  - 参数校验、prompt 构建、后台线程调用 DeepSeek、SSE 事件发送。
- Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/DeepSeekStreamClientImpl.java`
  - `HttpURLConnection` 请求 DeepSeek `/chat/completions`，解析上游 SSE。
- Create `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtAiController.java`
  - 暴露 `/virtual/ai/generateIntro/stream`。
- Modify `kevin-server/ruoyi-admin/src/main/resources/application.yml`
  - 增加 `deepseek` 配置项，不提交真实 key。
- Modify `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java`
  - 覆盖新增 Controller 类加载。
- Create `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/system/service/virtualservice/impl/VtAiServiceImplTest.java`
  - 验证 prompt、校验错误、SSE 事件行为。
- Create `kevin-web/src/api/virtual/ai.js`
  - 封装 POST SSE 流式请求、token、abort、事件解析。
- Modify `kevin-web/src/views/virtual/resource/index.vue`
  - 实训资源弹窗增加 `AI生成` 按钮和流式接收逻辑。
- Modify `kevin-web/src/views/virtual/experiment/index.vue`
  - 实训实验弹窗增加 `AI生成` 按钮和流式接收逻辑。

---

### Task 1: 后端 AI Service 骨架和单测

**Files:**
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtAiIntroRequest.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/config/DeepSeekProperties.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IDeepSeekStreamClient.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IVtAiService.java`
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/VtAiServiceImpl.java`
- Create: `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/system/service/virtualservice/impl/VtAiServiceImplTest.java`

- [ ] **Step 1: 写 service 单测**

Create `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/system/service/virtualservice/impl/VtAiServiceImplTest.java`:

```java
package com.ruoyi.system.service.virtualservice.impl;

import com.ruoyi.system.domain.virtualdomain.VtAiIntroRequest;
import com.ruoyi.system.service.virtualservice.IDeepSeekStreamClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.SyncTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VtAiServiceImplTest
{
    @Test
    public void resourcePromptShouldIncludeCoreFields()
    {
        CapturingClient client = new CapturingClient("资源简介片段");
        VtAiServiceImpl service = new VtAiServiceImpl(client, new SyncTaskExecutor());
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("resource");
        request.setResourceName("新能源汽车电池虚拟仿真资源");
        request.setResourceType("虚拟仿真");
        request.setMajorName("新能源汽车技术");
        request.setCourseName("动力电池检修");

        service.generateIntroStream(request);

        Assertions.assertTrue(client.prompt.contains("新能源汽车电池虚拟仿真资源"));
        Assertions.assertTrue(client.prompt.contains("虚拟仿真"));
        Assertions.assertTrue(client.prompt.contains("新能源汽车技术"));
        Assertions.assertTrue(client.prompt.contains("动力电池检修"));
        Assertions.assertTrue(client.prompt.contains("100到180字"));
    }

    @Test
    public void experimentPromptShouldIncludeCoreFields()
    {
        CapturingClient client = new CapturingClient("实验简介片段");
        VtAiServiceImpl service = new VtAiServiceImpl(client, new SyncTaskExecutor());
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("experiment");
        request.setExperimentName("动力电池故障诊断实验");
        request.setCourseName("动力电池检修");
        request.setResourceTitle("电池包三维拆装资源");
        request.setDifficulty("中等");
        request.setDurationMinutes(45);

        service.generateIntroStream(request);

        Assertions.assertTrue(client.prompt.contains("动力电池故障诊断实验"));
        Assertions.assertTrue(client.prompt.contains("动力电池检修"));
        Assertions.assertTrue(client.prompt.contains("电池包三维拆装资源"));
        Assertions.assertTrue(client.prompt.contains("中等"));
        Assertions.assertTrue(client.prompt.contains("45分钟"));
    }

    @Test
    public void blankResourceNameShouldReturnErrorEventWithoutCallingClient()
    {
        CapturingClient client = new CapturingClient("不会被调用");
        VtAiServiceImpl service = new VtAiServiceImpl(client, new SyncTaskExecutor());
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("resource");

        service.generateIntroStream(request);

        Assertions.assertEquals(0, client.callCount);
    }

    private static class CapturingClient implements IDeepSeekStreamClient
    {
        private final String content;
        private String prompt;
        private int callCount;
        private final List<String> chunks = new ArrayList<String>();

        CapturingClient(String content)
        {
            this.content = content;
        }

        @Override
        public void streamIntro(String prompt, Consumer<String> onChunk) throws Exception
        {
            this.callCount++;
            this.prompt = prompt;
            this.chunks.add(content);
            onChunk.accept(content);
        }
    }
}
```

- [ ] **Step 2: 运行测试，确认失败**

Run:

```bash
mvn -f kevin-server/pom.xml -pl ruoyi-admin -Dtest=VtAiServiceImplTest test
```

Expected: FAIL，提示 `VtAiIntroRequest`、`IDeepSeekStreamClient`、`VtAiServiceImpl` 不存在。

- [ ] **Step 3: 创建请求 DTO**

Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtAiIntroRequest.java`:

```java
package com.ruoyi.system.domain.virtualdomain;

public class VtAiIntroRequest
{
    private String scene;
    private String resourceName;
    private String resourceType;
    private String majorName;
    private String courseName;
    private String experimentName;
    private String difficulty;
    private Integer durationMinutes;
    private String resourceTitle;

    public String getScene() { return scene; }
    public void setScene(String scene) { this.scene = scene; }
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getMajorName() { return majorName; }
    public void setMajorName(String majorName) { this.majorName = majorName; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getExperimentName() { return experimentName; }
    public void setExperimentName(String experimentName) { this.experimentName = experimentName; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getResourceTitle() { return resourceTitle; }
    public void setResourceTitle(String resourceTitle) { this.resourceTitle = resourceTitle; }
}
```

- [ ] **Step 4: 创建 DeepSeek 配置类**

Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/config/DeepSeekProperties.java`:

```java
package com.ruoyi.system.config;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekProperties
{
    private String baseUrl = "https://api.deepseek.com";
    private String model = "deepseek-v4-flash";
    private String apiKey;
    private Integer timeoutSeconds = 60;

    public boolean isConfigured()
    {
        return StringUtils.isNotBlank(apiKey);
    }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
}
```

- [ ] **Step 5: 创建 client 和 service 接口**

Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IDeepSeekStreamClient.java`:

```java
package com.ruoyi.system.service.virtualservice;

import java.util.function.Consumer;

public interface IDeepSeekStreamClient
{
    void streamIntro(String prompt, Consumer<String> onChunk) throws Exception;
}
```

Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IVtAiService.java`:

```java
package com.ruoyi.system.service.virtualservice;

import com.ruoyi.system.domain.virtualdomain.VtAiIntroRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IVtAiService
{
    SseEmitter generateIntroStream(VtAiIntroRequest request);
}
```

- [ ] **Step 6: 实现 VtAiServiceImpl**

Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/VtAiServiceImpl.java`:

```java
package com.ruoyi.system.service.virtualservice.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.virtualdomain.VtAiIntroRequest;
import com.ruoyi.system.service.virtualservice.IDeepSeekStreamClient;
import com.ruoyi.system.service.virtualservice.IVtAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
public class VtAiServiceImpl implements IVtAiService
{
    private static final Logger log = LoggerFactory.getLogger(VtAiServiceImpl.class);
    private static final long SSE_TIMEOUT = 70000L;

    private final IDeepSeekStreamClient deepSeekStreamClient;
    private final TaskExecutor taskExecutor;

    public VtAiServiceImpl(IDeepSeekStreamClient deepSeekStreamClient,
                           @Qualifier("threadPoolTaskExecutor") TaskExecutor taskExecutor)
    {
        this.deepSeekStreamClient = deepSeekStreamClient;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public SseEmitter generateIntroStream(VtAiIntroRequest request)
    {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        taskExecutor.execute(() -> doGenerate(request, emitter));
        return emitter;
    }

    void doGenerate(VtAiIntroRequest request, SseEmitter emitter)
    {
        try
        {
            String prompt = buildPrompt(request);
            deepSeekStreamClient.streamIntro(prompt, content -> sendMessage(emitter, cleanChunk(content)));
            sendDone(emitter);
        }
        catch (IllegalArgumentException e)
        {
            sendError(emitter, e.getMessage());
        }
        catch (IllegalStateException e)
        {
            sendError(emitter, e.getMessage());
        }
        catch (Exception e)
        {
            log.warn("DeepSeek intro generation failed", e);
            sendError(emitter, "AI生成失败，请稍后重试");
        }
    }

    String buildPrompt(VtAiIntroRequest request)
    {
        if (request == null || StringUtils.isBlank(request.getScene()))
        {
            throw new IllegalArgumentException("生成场景不能为空");
        }
        if ("resource".equals(request.getScene()))
        {
            if (StringUtils.isBlank(request.getResourceName()))
            {
                throw new IllegalArgumentException("资源名称不能为空");
            }
            return "你是虚拟仿真实训教学管理平台的内容编辑。请根据以下信息生成一段100到180字的中文实训资源简介，"
                    + "突出资源用途、适用对象、教学价值和共享开放价值。不要使用标题、序号、Markdown。"
                    + "\n资源名称：" + safe(request.getResourceName())
                    + "\n资源类型：" + safe(request.getResourceType())
                    + "\n所属专业：" + safe(request.getMajorName())
                    + "\n适用课程：" + safe(request.getCourseName());
        }
        if ("experiment".equals(request.getScene()))
        {
            if (StringUtils.isBlank(request.getExperimentName()))
            {
                throw new IllegalArgumentException("实验名称不能为空");
            }
            String duration = request.getDurationMinutes() == null ? "未填写" : request.getDurationMinutes() + "分钟";
            return "你是虚拟仿真实训教学管理平台的教学设计助手。请根据以下信息生成一段100到180字的中文实训实验简介，"
                    + "突出实验目标、训练内容、能力培养和学习产出。不要使用标题、序号、Markdown。"
                    + "\n实验名称：" + safe(request.getExperimentName())
                    + "\n关联课程：" + safe(request.getCourseName())
                    + "\n关联资源：" + safe(request.getResourceTitle())
                    + "\n难度：" + safe(request.getDifficulty())
                    + "\n预计时长：" + duration;
        }
        throw new IllegalArgumentException("生成场景不支持");
    }

    private String safe(String value)
    {
        return StringUtils.isBlank(value) ? "未填写" : value.trim();
    }

    private String cleanChunk(String value)
    {
        if (value == null)
        {
            return "";
        }
        return value.replace("```", "").replace("#", "");
    }

    private void sendMessage(SseEmitter emitter, String content)
    {
        if (StringUtils.isBlank(content))
        {
            return;
        }
        try
        {
            emitter.send(SseEmitter.event().name("message").data(content));
        }
        catch (IOException e)
        {
            throw new IllegalStateException("AI生成连接已中断");
        }
    }

    private void sendDone(SseEmitter emitter)
    {
        try
        {
            emitter.send(SseEmitter.event().name("done").data(""));
        }
        catch (IOException e)
        {
            log.debug("SSE done event ignored because client disconnected", e);
        }
        finally
        {
            emitter.complete();
        }
    }

    private void sendError(SseEmitter emitter, String message)
    {
        try
        {
            emitter.send(SseEmitter.event().name("error").data(message));
        }
        catch (IOException e)
        {
            log.debug("SSE error event ignored because client disconnected", e);
        }
        finally
        {
            emitter.complete();
        }
    }
}
```

- [ ] **Step 7: 运行 service 单测**

Run:

```bash
mvn -f kevin-server/pom.xml -pl ruoyi-admin -Dtest=VtAiServiceImplTest test
```

Expected: PASS，输出包含 `Tests run: 3`，且没有 `Failures` 和 `Errors`。

- [ ] **Step 8: 提交 Task 1**

Run:

```bash
git add kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/domain/virtualdomain/VtAiIntroRequest.java \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/config/DeepSeekProperties.java \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IDeepSeekStreamClient.java \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/IVtAiService.java \
  kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/VtAiServiceImpl.java \
  kevin-server/ruoyi-admin/src/test/java/com/ruoyi/system/service/virtualservice/impl/VtAiServiceImplTest.java
git commit -m "新增AI简介生成服务骨架"
```

---

### Task 2: DeepSeek 流式客户端、Controller 和配置

**Files:**
- Create: `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/DeepSeekStreamClientImpl.java`
- Create: `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtAiController.java`
- Modify: `kevin-server/ruoyi-admin/src/main/resources/application.yml`
- Modify: `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java`

- [ ] **Step 1: 写 Controller 冒烟断言**

Modify `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java` and add this assertion inside `controllerClassesShouldLoad()`:

```java
Assertions.assertNotNull(VtAiController.class);
```

- [ ] **Step 2: 运行测试，确认失败**

Run:

```bash
mvn -f kevin-server/pom.xml -pl ruoyi-admin -Dtest=VirtualControllerSmokeTest test
```

Expected: FAIL，提示 `VtAiController` 不存在。

- [ ] **Step 3: 实现 DeepSeekStreamClientImpl**

Create `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/DeepSeekStreamClientImpl.java`:

```java
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

        HttpURLConnection connection = null;
        try
        {
            URL url = new URL(trimEnd(properties.getBaseUrl()) + "/chat/completions");
            connection = (HttpURLConnection) url.openConnection();
            int timeout = properties.getTimeoutSeconds() == null ? 60 : properties.getTimeoutSeconds();
            connection.setConnectTimeout(timeout * 1000);
            connection.setReadTimeout(timeout * 1000);
            connection.setRequestMethod("POST");
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
            if (status < 200 || status >= 300)
            {
                String errorBody = readAll(connection.getErrorStream());
                log.warn("DeepSeek API returned status {}, body {}", status, errorBody);
                throw new IllegalStateException("AI生成失败，请稍后重试");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    handleSseLine(line, onChunk);
                }
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }

    String buildRequestBody(String prompt)
    {
        JSONObject request = new JSONObject();
        request.put("model", properties.getModel());
        request.put("stream", true);
        request.put("temperature", 0.7);
        request.put("max_tokens", 512);

        JSONObject thinking = new JSONObject();
        thinking.put("type", "disabled");
        request.put("thinking", thinking);

        JSONArray messages = new JSONArray();
        JSONObject system = new JSONObject();
        system.put("role", "system");
        system.put("content", "你是虚拟仿真实训平台的中文内容生成助手，只输出可直接粘贴到简介字段的正文。");
        messages.add(system);
        JSONObject user = new JSONObject();
        user.put("role", "user");
        user.put("content", prompt);
        messages.add(user);
        request.put("messages", messages);
        return request.toJSONString();
    }

    void handleSseLine(String line, Consumer<String> onChunk)
    {
        if (StringUtils.isBlank(line) || line.startsWith(":"))
        {
            return;
        }
        if (!line.startsWith("data:"))
        {
            return;
        }
        String data = line.substring("data:".length()).trim();
        if ("[DONE]".equals(data))
        {
            return;
        }
        JSONObject chunk = JSON.parseObject(data);
        JSONArray choices = chunk.getJSONArray("choices");
        if (choices == null || choices.isEmpty())
        {
            return;
        }
        JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
        if (delta == null)
        {
            return;
        }
        String content = delta.getString("content");
        if (StringUtils.isNotEmpty(content))
        {
            onChunk.accept(content);
        }
    }

    private String trimEnd(String baseUrl)
    {
        if (StringUtils.isBlank(baseUrl))
        {
            return "https://api.deepseek.com";
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private String readAll(InputStream inputStream) throws Exception
    {
        if (inputStream == null)
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
        }
        return builder.toString();
    }
}
```

- [ ] **Step 4: 创建 VtAiController**

Create `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtAiController.java`:

```java
package com.ruoyi.web.controller.virtual;

import com.ruoyi.system.domain.virtualdomain.VtAiIntroRequest;
import com.ruoyi.system.service.virtualservice.IVtAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/virtual/ai")
public class VtAiController
{
    @Autowired
    private IVtAiService vtAiService;

    @PreAuthorize("@ss.hasAnyPermi('virtual:resource:add,virtual:resource:edit,virtual:experiment:add,virtual:experiment:edit')")
    @PostMapping(value = "/generateIntro/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateIntroStream(@RequestBody VtAiIntroRequest request)
    {
        return vtAiService.generateIntroStream(request);
    }
}
```

- [ ] **Step 5: 增加 DeepSeek 配置项**

Modify `kevin-server/ruoyi-admin/src/main/resources/application.yml` and append this block after the `swagger` block:

```yaml
# DeepSeek AI配置
deepseek:
  baseUrl: https://api.deepseek.com
  model: deepseek-v4-flash
  apiKey: ${DEEPSEEK_API_KEY:}
  timeoutSeconds: 60
```

- [ ] **Step 6: 运行后端测试**

Run:

```bash
mvn -f kevin-server/pom.xml -pl ruoyi-admin test
```

Expected: PASS，输出包含真实 `Tests run:` 计数，没有 `Tests are skipped.`。

- [ ] **Step 7: 提交 Task 2**

Run:

```bash
git add kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/service/virtualservice/impl/DeepSeekStreamClientImpl.java \
  kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/VtAiController.java \
  kevin-server/ruoyi-admin/src/main/resources/application.yml \
  kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java
git commit -m "接入DeepSeek流式生成接口"
```

---

### Task 3: 前端流式 API 封装

**Files:**
- Create: `kevin-web/src/api/virtual/ai.js`

- [ ] **Step 1: 创建流式 API 工具**

Create `kevin-web/src/api/virtual/ai.js`:

```javascript
import { getToken } from '@/utils/auth'

function parseSseBlock(block) {
  const event = { event: 'message', data: '' }
  block.split('\n').forEach(line => {
    if (line.startsWith('event:')) {
      event.event = line.slice(6).trim()
    } else if (line.startsWith('data:')) {
      event.data += line.slice(5).trim()
    }
  })
  return event
}

function getBaseUrl() {
  return process.env.VUE_APP_BASE_API || ''
}

export async function streamGenerateIntro(payload, handlers) {
  const controller = new AbortController()
  const token = getToken()
  const response = await fetch(getBaseUrl() + '/virtual/ai/generateIntro/stream', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
      'Accept': 'text/event-stream',
      'Authorization': token ? 'Bearer ' + token : ''
    },
    body: JSON.stringify(payload),
    signal: controller.signal
  })

  if (!response.ok) {
    throw new Error('AI生成接口异常：' + response.status)
  }

  const reader = response.body.getReader()
  const decoder = new TextDecoder('utf-8')
  let buffer = ''

  const read = async() => {
    const result = await reader.read()
    if (result.done) {
      if (handlers && handlers.done) {
        handlers.done()
      }
      return
    }
    buffer += decoder.decode(result.value, { stream: true })
    const blocks = buffer.split('\n\n')
    buffer = blocks.pop()
    blocks.forEach(block => {
      if (!block.trim()) {
        return
      }
      const event = parseSseBlock(block)
      if (event.event === 'error' && handlers && handlers.error) {
        handlers.error(event.data || 'AI生成失败，请稍后重试')
      } else if (event.event === 'done' && handlers && handlers.done) {
        handlers.done()
      } else if (handlers && handlers.message) {
        handlers.message(event.data)
      }
    })
    await read()
  }

  read().catch(error => {
    if (error.name === 'AbortError') {
      return
    }
    if (handlers && handlers.error) {
      handlers.error(error.message || 'AI生成失败，请稍后重试')
    }
  })

  return controller
}
```

- [ ] **Step 2: 检查语法**

Run:

```bash
npm --prefix kevin-web run build:prod
```

Expected: PASS，Webpack build 完成并生成 `kevin-web/dist`。不要提交 `dist/`。

- [ ] **Step 3: 提交 Task 3**

Run:

```bash
git add kevin-web/src/api/virtual/ai.js
git commit -m "新增AI流式生成前端接口"
```

---

### Task 4: 实训资源页面接入 AI 生成

**Files:**
- Modify: `kevin-web/src/views/virtual/resource/index.vue`

- [ ] **Step 1: 修改资源页 import**

Modify the `<script>` imports in `kevin-web/src/views/virtual/resource/index.vue`:

```javascript
import { listResource, getResource, delResource, addResource, updateResource } from '@/api/virtual/resource'
import { streamGenerateIntro } from '@/api/virtual/ai'
```

- [ ] **Step 2: 在简介表单项增加 AI 生成按钮**

Replace the existing introduction form item:

```vue
<el-form-item label="简介" prop="introduction"><el-input v-model="form.introduction" type="textarea" :rows="3" placeholder="请输入简介" /></el-form-item>
```

with:

```vue
<el-form-item label="简介" prop="introduction">
  <el-input v-model="form.introduction" type="textarea" :rows="4" placeholder="请输入简介" />
  <el-button class="ai-generate-btn" type="primary" plain size="mini" icon="el-icon-magic-stick" :loading="aiGenerating" @click="handleGenerateIntro">
    {{ aiGenerating ? '生成中' : 'AI生成' }}
  </el-button>
</el-form-item>
```

- [ ] **Step 3: 增加 data 状态**

Add these fields in `data()` return object after `open: false,`:

```javascript
aiGenerating: false,
aiAbortController: null,
```

- [ ] **Step 4: 增加资源页 AI 方法**

Add these methods inside `methods` in `kevin-web/src/views/virtual/resource/index.vue`:

```javascript
stopAiGenerate() {
  if (this.aiAbortController) {
    this.aiAbortController.abort()
    this.aiAbortController = null
  }
  this.aiGenerating = false
},
handleGenerateIntro() {
  if (!this.form.resourceName) {
    this.$modal.msgWarning('请先填写资源名称')
    return
  }
  this.stopAiGenerate()
  this.form.introduction = ''
  this.aiGenerating = true
  streamGenerateIntro({
    scene: 'resource',
    resourceName: this.form.resourceName,
    resourceType: this.getResourceTypeLabel(this.form.resourceType),
    majorName: this.form.majorName,
    courseName: this.form.courseName
  }, {
    message: content => {
      this.form.introduction = (this.form.introduction || '') + content
    },
    done: () => {
      this.aiGenerating = false
      this.aiAbortController = null
    },
    error: message => {
      this.aiGenerating = false
      this.aiAbortController = null
      this.$modal.msgError(message)
    }
  }).then(controller => {
    this.aiAbortController = controller
  }).catch(error => {
    this.aiGenerating = false
    this.aiAbortController = null
    this.$modal.msgError(error.message || 'AI生成失败，请稍后重试')
  })
},
getResourceTypeLabel(value) {
  const item = this.dict.type.vt_resource_type.find(dict => dict.value === value)
  return item ? item.label : value
},
```

- [ ] **Step 5: 在取消、重置、提交前中断生成**

Modify `cancel()`:

```javascript
cancel() {
  this.stopAiGenerate()
  this.open = false
  this.reset()
},
```

Modify `reset()` and call `this.stopAiGenerate()` before assigning `this.form`:

```javascript
reset() {
  this.stopAiGenerate()
  this.form = { resourceId: undefined, resourceName: undefined, resourceType: '0', majorName: undefined, courseName: undefined, coverUrl: undefined, fileUrl: undefined, shareStatus: '0', viewCount: 0, collectCount: 0, introduction: undefined }
  this.resetForm('form')
},
```

Add `this.stopAiGenerate()` before `this.open = false` in the success block of `submitForm()`:

```javascript
request.then(() => {
  this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
  this.stopAiGenerate()
  this.open = false
  this.getList()
})
```

- [ ] **Step 6: 增加按钮样式**

Append this style block at the end of `kevin-web/src/views/virtual/resource/index.vue`:

```vue
<style scoped>
.ai-generate-btn {
  margin-top: 8px;
}
</style>
```

- [ ] **Step 7: 构建验证**

Run:

```bash
npm --prefix kevin-web run build:prod
```

Expected: PASS，Webpack build 完成。确认 `git status --short` 中没有暂存或提交 `kevin-web/dist`。

- [ ] **Step 8: 提交 Task 4**

Run:

```bash
git add kevin-web/src/views/virtual/resource/index.vue
git commit -m "实训资源接入AI简介生成"
```

---

### Task 5: 实训实验页面接入 AI 生成

**Files:**
- Modify: `kevin-web/src/views/virtual/experiment/index.vue`

- [ ] **Step 1: 修改实验页 import**

Modify the `<script>` imports in `kevin-web/src/views/virtual/experiment/index.vue`:

```javascript
import { listExperiment, getExperiment, delExperiment, addExperiment, updateExperiment } from '@/api/virtual/experiment'
import { optionselectCourse } from '@/api/virtual/course'
import { optionselectResource } from '@/api/virtual/resource'
import { streamGenerateIntro } from '@/api/virtual/ai'
```

- [ ] **Step 2: 在实验简介表单项增加 AI 生成按钮**

Replace the existing introduction form item:

```vue
<el-form-item label="实验简介" prop="introduction"><el-input v-model="form.introduction" type="textarea" :rows="3" placeholder="请输入实验简介" /></el-form-item>
```

with:

```vue
<el-form-item label="实验简介" prop="introduction">
  <el-input v-model="form.introduction" type="textarea" :rows="4" placeholder="请输入实验简介" />
  <el-button class="ai-generate-btn" type="primary" plain size="mini" icon="el-icon-magic-stick" :loading="aiGenerating" @click="handleGenerateIntro">
    {{ aiGenerating ? '生成中' : 'AI生成' }}
  </el-button>
</el-form-item>
```

- [ ] **Step 3: 增加 data 状态**

Add these fields in `data()` return object after `open: false,`:

```javascript
aiGenerating: false,
aiAbortController: null,
```

- [ ] **Step 4: 增加实验页 AI 方法**

Add these methods inside `methods` in `kevin-web/src/views/virtual/experiment/index.vue`:

```javascript
stopAiGenerate() {
  if (this.aiAbortController) {
    this.aiAbortController.abort()
    this.aiAbortController = null
  }
  this.aiGenerating = false
},
handleGenerateIntro() {
  if (!this.form.experimentName) {
    this.$modal.msgWarning('请先填写实验名称')
    return
  }
  this.stopAiGenerate()
  this.form.introduction = ''
  this.aiGenerating = true
  streamGenerateIntro({
    scene: 'experiment',
    experimentName: this.form.experimentName,
    courseName: this.getCourseName(this.form.courseId),
    resourceTitle: this.getResourceName(this.form.resourceId),
    difficulty: this.getDifficultyLabel(this.form.difficulty),
    durationMinutes: this.form.durationMinutes
  }, {
    message: content => {
      this.form.introduction = (this.form.introduction || '') + content
    },
    done: () => {
      this.aiGenerating = false
      this.aiAbortController = null
    },
    error: message => {
      this.aiGenerating = false
      this.aiAbortController = null
      this.$modal.msgError(message)
    }
  }).then(controller => {
    this.aiAbortController = controller
  }).catch(error => {
    this.aiGenerating = false
    this.aiAbortController = null
    this.$modal.msgError(error.message || 'AI生成失败，请稍后重试')
  })
},
getCourseName(courseId) {
  const item = this.courseOptions.find(course => course.courseId === courseId)
  return item ? item.courseName : ''
},
getResourceName(resourceId) {
  const item = this.resourceOptions.find(resource => resource.resourceId === resourceId)
  return item ? item.resourceName : ''
},
getDifficultyLabel(value) {
  const item = this.dict.type.vt_difficulty.find(dict => dict.value === value)
  return item ? item.label : value
},
```

- [ ] **Step 5: 在取消、重置、提交前中断生成**

Modify `cancel()`:

```javascript
cancel() {
  this.stopAiGenerate()
  this.open = false
  this.reset()
},
```

Modify `reset()` and call `this.stopAiGenerate()` before assigning `this.form`:

```javascript
reset() {
  this.stopAiGenerate()
  this.form = { experimentId: undefined, experimentName: undefined, courseId: undefined, resourceId: undefined, difficulty: '1', durationMinutes: 45, openStatus: '0', introduction: undefined }
  this.resetForm('form')
},
```

Add `this.stopAiGenerate()` before `this.open = false` in the success block of `submitForm()`:

```javascript
request.then(() => {
  this.$modal.msgSuccess(isUpdate ? '修改成功' : '新增成功')
  this.stopAiGenerate()
  this.open = false
  this.getList()
})
```

- [ ] **Step 6: 增加按钮样式**

Append this style block at the end of `kevin-web/src/views/virtual/experiment/index.vue`:

```vue
<style scoped>
.ai-generate-btn {
  margin-top: 8px;
}
</style>
```

- [ ] **Step 7: 构建验证**

Run:

```bash
npm --prefix kevin-web run build:prod
```

Expected: PASS，Webpack build 完成。确认 `git status --short` 中没有暂存或提交 `kevin-web/dist`。

- [ ] **Step 8: 提交 Task 5**

Run:

```bash
git add kevin-web/src/views/virtual/experiment/index.vue
git commit -m "实训实验接入AI简介生成"
```

---

### Task 6: 全量验证和手动演示检查

**Files:**
- Verify only; no required source edits.

- [ ] **Step 1: 后端全量测试**

Run:

```bash
mvn -f kevin-server/pom.xml test
```

Expected: PASS，输出包含真实 `Tests run:` 计数，没有 `Tests are skipped.`。

- [ ] **Step 2: 前端生产构建**

Run:

```bash
npm --prefix kevin-web run build:prod
```

Expected: PASS，Webpack build 完成。不要提交 `kevin-web/dist`。

- [ ] **Step 3: 无 API Key 降级验证**

Run backend without `DEEPSEEK_API_KEY`, open the resource form, fill `资源名称`, click `AI生成`.

Expected:
- Button enters loading briefly.
- Page shows “未配置 DeepSeek API Key” or backend-sent error message.
- Textarea remains editable.
- Existing save flow still works when manually entering introduction.

- [ ] **Step 4: 有 API Key 流式验证**

Start backend with a real key:

```bash
export DEEPSEEK_API_KEY='真实key只放本地环境变量'
mvn -f kevin-server/pom.xml -pl ruoyi-admin spring-boot:run
```

Start frontend:

```bash
npm --prefix kevin-web run dev
```

Expected:
- 实训资源弹窗点击 `AI生成` 后简介逐步出现。
- 实训实验弹窗点击 `AI生成` 后简介逐步出现。
- 关闭弹窗会停止追加内容。
- 保存资源/实验仍走原新增或修改接口。

- [ ] **Step 5: 最终状态检查**

Run:

```bash
git status --short
```

Expected:
- 只有本功能相关源码或测试文件处于已提交状态。
- 不出现 `kevin-web/dist`、`kevin-server/*/target`、日志文件或真实密钥文件。
