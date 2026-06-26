# DeepSeek 流式生成实训简介设计

## 背景

当前项目是虚拟仿真实训教学管理及资源共享云平台，已有实训资源、实训实验、课程、教学计划、过程结果、门户和数据大屏等模块。后台的“实训资源”和“实训实验”新增/编辑弹窗中已经有简介字段，适合补充一个轻量 AI 能力：管理员填写基础信息后，由 DeepSeek 大模型流式生成简介，再由管理员人工调整并保存。

DeepSeek 官方 API 兼容 OpenAI Chat Completions，基础地址为 `https://api.deepseek.com`，当前模型列表包含 `deepseek-v4-flash` 和 `deepseek-v4-pro`。本功能默认使用 `deepseek-v4-flash`。Chat Completions 支持 `stream: true`，会用 SSE 逐段返回增量内容，并以 `data: [DONE]` 结束；排队等待时可能返回 `: keep-alive`，后端解析时需要忽略。

参考文档：

- DeepSeek API 快速开始：https://api-docs.deepseek.com/
- DeepSeek 模型与价格：https://api-docs.deepseek.com/quick_start/pricing
- DeepSeek Chat Completion：https://api-docs.deepseek.com/api/create-chat-completion
- DeepSeek FAQ：https://api-docs.deepseek.com/faq

## 目标

1. 在“实训资源”和“实训实验”后台表单中增加“AI生成”能力。
2. 使用 DeepSeek 真实接口生成中文简介，不使用本地假数据。
3. 使用 SSE 流式返回，让演示时能看到内容逐步生成。
4. 不新增业务主表，不改变资源和实验原有新增/修改保存流程。
5. API Key 只保存在后端配置中，不进入前端、SQL 初始化脚本或数据库。

## 非目标

1. 不实现聊天窗口、多轮对话或 AI 历史记录。
2. 不为每次生成结果落库，最终是否保存仍由管理员点击原表单保存决定。
3. 不做复杂提示词模板管理后台。
4. 不引入 WebFlux 或独立 AI 服务，保持在现有 Spring MVC/RuoYi 架构内。

## 功能范围

### 实训资源简介生成

入口位于 `kevin-web/src/views/virtual/resource/index.vue` 的新增/编辑弹窗。管理员填写以下字段后点击 `AI生成`：

- 资源名称
- 资源类型
- 所属专业
- 适用课程

AI 返回 100 到 180 字中文资源简介，内容应突出资源用途、适用对象、教学价值和共享开放价值。返回内容流式追加到原有简介 textarea 中，管理员可继续编辑。

### 实训实验简介生成

入口位于 `kevin-web/src/views/virtual/experiment/index.vue` 的新增/编辑弹窗。管理员填写以下字段后点击 `AI生成`：

- 实验名称
- 关联课程
- 关联资源
- 难度
- 预计时长

AI 返回 100 到 180 字中文实验简介，内容应突出实验目标、训练内容、能力培养和学习产出。返回内容流式追加到原有简介 textarea 中，管理员可继续编辑。

## 后端设计

### Controller

新增 `VtAiController`，放在 `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/`。

接口：

- `POST /virtual/ai/generateIntro/stream`
- `Content-Type: application/json`
- `Accept: text/event-stream`
- 返回 `SseEmitter`

请求字段：

- `scene`：生成场景，取值 `resource` 或 `experiment`。
- `resourceName`、`resourceType`、`majorName`、`courseName`：资源场景字段。
- `experimentName`、`difficulty`、`durationMinutes`、`resourceTitle`：实验场景字段。

响应事件：

- `message`：正文增量片段。
- `done`：生成结束。
- `error`：生成失败，data 为用户可读错误消息。

### Service

新增 `VtAiService` 和实现类，放在 `ruoyi-system` 的 `virtualservice` 包下。

职责：

- 校验 `scene` 和核心字段。
- 根据场景组装 prompt。
- 调用 `DeepSeekClient` 开始流式生成。
- 对返回片段做轻量清洗，过滤多余标题、Markdown 标记和首尾引号。
- 将增量片段发送给 `SseEmitter`。

### DeepSeek Client

新增 `DeepSeekClient`，专门封装外部 HTTP 调用。

职责：

- 读取配置：`deepseek.apiKey`、`deepseek.baseUrl`、`deepseek.model`、`deepseek.timeoutSeconds`。
- 请求 `POST {baseUrl}/chat/completions`。
- 设置 `Authorization: Bearer ${apiKey}`。
- 请求体设置 `stream: true`、`model: deepseek-v4-flash`、`thinking: {"type": "disabled"}`、`temperature: 0.7`、`max_tokens`。
- 按行读取 SSE 响应。
- 跳过空行和 `: keep-alive`。
- 只解析 `data:` 行。
- 收到 `[DONE]` 后正常结束。
- 从 JSON chunk 中读取 `choices[0].delta.content` 并回调给 service。
- 简介生成不需要推理过程，默认禁用 thinking mode，避免把思考内容或非正文片段转发给前端。

项目当前是 Java 8 / Spring Boot / RuoYi 多模块结构，实现时优先使用 JDK `HttpURLConnection` 或项目现有可用 HTTP 工具，避免为了一个轻量功能引入较重依赖。若现有依赖中已有 OkHttp 或 Apache HttpClient，则优先复用已有依赖。

### 配置

后端配置项放在 `kevin-server/ruoyi-admin/src/main/resources/application.yml`：

```yaml
deepseek:
  baseUrl: https://api.deepseek.com
  model: deepseek-v4-flash
  apiKey: ${DEEPSEEK_API_KEY:}
  timeoutSeconds: 60
```

真实 key 通过环境变量 `DEEPSEEK_API_KEY` 注入。仓库中不提交真实 key。

## 前端设计

### API 模块

新增 `kevin-web/src/api/virtual/ai.js`，导出流式生成方法。由于浏览器原生 `EventSource` 不支持 POST JSON，本功能使用 `fetch` 读取 `ReadableStream`。

前端需要处理：

- 设置请求方法为 `POST`。
- 携带 JSON body。
- 携带 RuoYi 登录 token。
- 使用 `AbortController` 支持取消。
- 读取 `reader.read()` 返回的字节流。
- 按 SSE 事件拆分 `event:` 和 `data:`。
- `message` 事件追加到简介字段。
- `done` 事件结束 loading。
- `error` 事件弹出错误提示。

### 页面交互

在两个弹窗的简介字段旁增加 `AI生成` 按钮。

按钮状态：

- 基础字段未填时点击，前端提示先填写资源名称或实验名称。
- 生成中禁用按钮，文案显示“生成中”。
- 再次打开弹窗时重置生成状态。
- 关闭弹窗或提交保存时，若仍在生成则中断请求。

生成行为：

- 点击生成前清空原简介，避免新旧内容混在一起。
- 流式片段返回后追加到 `form.introduction`。
- 生成完成后保留文本，管理员可继续编辑。
- 生成失败时不影响手动填写和保存。

## 数据流

1. 管理员在资源或实验弹窗填写基础字段。
2. 前端点击 `AI生成`，组装 `scene` 和业务字段。
3. 前端调用 `POST /virtual/ai/generateIntro/stream`。
4. 后端校验参数和 DeepSeek 配置。
5. 后端组装 prompt，并以 `stream: true` 调用 DeepSeek Chat Completions。
6. DeepSeek 返回 SSE 增量 chunk。
7. 后端解析 `delta.content`，通过本系统 SSE 接口转发给前端。
8. 前端边接收边追加到简介 textarea。
9. DeepSeek 返回 `[DONE]` 后，后端发送 `done` 事件并关闭 `SseEmitter`。
10. 管理员确认或修改简介，再走原资源/实验保存接口。

## 异常处理

1. 未配置 API Key：后端发送 `error` 事件，提示“未配置 DeepSeek API Key”。
2. 必填字段缺失：前端先提示，后端兜底校验并返回 `error`。
3. DeepSeek 401、402、429、500、503：后端统一转换为“AI生成失败，请稍后重试”，日志记录原始状态码。
4. DeepSeek 超时：关闭外部连接，发送 `error` 事件并完成 `SseEmitter`。
5. 用户关闭弹窗或重复触发：前端通过 `AbortController` 中断请求。
6. 已生成部分内容不强制清空，用户可以保留、删除或继续手动编辑。
7. 后端日志不打印 API Key，不记录完整 Authorization header。

## 测试与验证

后端验证：

- `VtAiService` 单测覆盖资源场景 prompt、实验场景 prompt、非法 `scene`、必填字段缺失。
- Controller smoke test 覆盖未配置 API Key 或参数缺失时能返回错误事件。
- 有真实 key 时手动调用 SSE 接口，确认可以收到增量片段和 `done` 事件。

前端验证：

- `npm run build:prod` 确认新增 API 模块和页面按钮不破坏打包。
- 本地页面验证资源简介流式生成。
- 本地页面验证实验简介流式生成。
- 验证关闭弹窗时请求能被中断。

演示验证：

- 配置 `DEEPSEEK_API_KEY`。
- 启动后端和前端。
- 打开“实训资源”新增弹窗，填写资源名称等字段，点击 `AI生成`，观察简介逐步出现。
- 打开“实训实验”新增弹窗，填写实验名称等字段，点击 `AI生成`，观察简介逐步出现。

## 实施顺序

1. 增加后端配置类、请求 DTO 和 DeepSeek 流式客户端。
2. 增加 `VtAiService`，完成 prompt 组装和 SSE 转发。
3. 增加 `VtAiController`。
4. 增加前端 `src/api/virtual/ai.js`。
5. 修改资源页面弹窗，加入 `AI生成` 按钮和流式接收逻辑。
6. 修改实验页面弹窗，加入 `AI生成` 按钮和流式接收逻辑。
7. 增加后端测试。
8. 运行 Maven 测试和前端生产打包。

## 风险与边界

1. DeepSeek 外网调用依赖网络和 API Key，演示环境需要提前配置。
2. SSE 经过代理时可能受超时影响，本地 Vue dev proxy 和后端都要避免缓存流式响应。
3. 生成内容由模型决定，管理员必须保留人工编辑权。
4. 如果学校演示环境无法访问外网，需要准备“未配置/调用失败”的降级提示，不能让页面卡死。
