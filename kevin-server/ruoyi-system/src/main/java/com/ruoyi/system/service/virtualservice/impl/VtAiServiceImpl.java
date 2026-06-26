package com.ruoyi.system.service.virtualservice.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.virtualdomain.VtAiIntroRequest;
import com.ruoyi.system.service.virtualservice.IDeepSeekStreamClient;
import com.ruoyi.system.service.virtualservice.IVtAiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
public class VtAiServiceImpl implements IVtAiService
{
    private static final Long SSE_TIMEOUT = 70000L;

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
        final SseEmitter emitter = createEmitter();
        taskExecutor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String prompt = buildPrompt(request);
                    deepSeekStreamClient.streamIntro(prompt, new java.util.function.Consumer<String>()
                    {
                        @Override
                        public void accept(String chunk)
                        {
                            sendEvent(emitter, "message", cleanChunk(chunk));
                        }
                    });
                    sendEvent(emitter, "done", "");
                    emitter.complete();
                }
                catch (IllegalArgumentException e)
                {
                    sendEvent(emitter, "error", e.getMessage());
                    emitter.complete();
                }
                catch (IllegalStateException e)
                {
                    sendEvent(emitter, "error", e.getMessage());
                    emitter.complete();
                }
                catch (Exception e)
                {
                    sendEvent(emitter, "error", "AI生成失败，请稍后重试");
                    emitter.complete();
                }
            }
        });
        return emitter;
    }

    protected SseEmitter createEmitter()
    {
        return new SseEmitter(SSE_TIMEOUT);
    }

    private String buildPrompt(VtAiIntroRequest request)
    {
        if (request == null)
        {
            throw new IllegalArgumentException("请求参数不能为空");
        }
        if (StringUtils.equals("experiment", request.getScene()))
        {
            return buildExperimentPrompt(request);
        }
        return buildResourcePrompt(request);
    }

    private String buildResourcePrompt(VtAiIntroRequest request)
    {
        if (StringUtils.isBlank(request.getResourceName()))
        {
            throw new IllegalArgumentException("资源名称不能为空");
        }
        StringBuilder prompt = new StringBuilder();
        prompt.append("请为虚拟仿真训练平台生成资源简介，要求语言专业、准确、适合教学资源展示，");
        prompt.append("字数控制在100到180字，不要使用Markdown格式。");
        appendField(prompt, "资源名称", request.getResourceName());
        appendField(prompt, "资源类型", request.getResourceType());
        appendField(prompt, "所属专业", request.getMajorName());
        appendField(prompt, "适用课程", request.getCourseName());
        return prompt.toString();
    }

    private String buildExperimentPrompt(VtAiIntroRequest request)
    {
        if (StringUtils.isBlank(request.getExperimentName()))
        {
            throw new IllegalArgumentException("实验名称不能为空");
        }
        StringBuilder prompt = new StringBuilder();
        prompt.append("请为虚拟仿真训练平台生成实验简介，要求突出实验目标、训练内容和学习价值，");
        prompt.append("语言专业简洁，不要使用Markdown格式。");
        appendField(prompt, "实验名称", request.getExperimentName());
        appendField(prompt, "适用课程", request.getCourseName());
        appendField(prompt, "关联资源", request.getResourceTitle());
        appendField(prompt, "难度", request.getDifficulty());
        if (request.getDurationMinutes() != null)
        {
            appendField(prompt, "实验时长", request.getDurationMinutes() + "分钟");
        }
        return prompt.toString();
    }

    private void appendField(StringBuilder prompt, String label, String value)
    {
        if (StringUtils.isNotBlank(value))
        {
            prompt.append(label).append("：").append(value).append("。");
        }
    }

    private String cleanChunk(String chunk)
    {
        if (chunk == null)
        {
            return "";
        }
        return chunk.replace("```", "").replace("#", "");
    }

    private void sendEvent(SseEmitter emitter, String eventName, String data)
    {
        try
        {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        }
        catch (IOException e)
        {
            emitter.completeWithError(e);
        }
    }
}
