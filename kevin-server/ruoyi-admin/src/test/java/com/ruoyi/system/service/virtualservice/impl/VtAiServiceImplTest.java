package com.ruoyi.system.service.virtualservice.impl;

import com.ruoyi.system.domain.virtualdomain.VtAiIntroRequest;
import com.ruoyi.system.service.virtualservice.IDeepSeekStreamClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VtAiServiceImplTest
{
    @Test
    public void resourcePromptShouldIncludeCoreFields()
    {
        CapturingClient client = new CapturingClient("资源简介片段");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        VtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("resource");
        request.setResourceName("新能源汽车电池虚拟仿真资源");
        request.setResourceType("虚拟仿真");
        request.setMajorName("新能源汽车技术");
        request.setCourseName("动力电池检修");

        SseEmitter result = service.generateIntroStream(request);

        Assertions.assertSame(emitter, result);
        Assertions.assertTrue(client.prompt.contains("新能源汽车电池虚拟仿真资源"));
        Assertions.assertTrue(client.prompt.contains("虚拟仿真"));
        Assertions.assertTrue(client.prompt.contains("新能源汽车技术"));
        Assertions.assertTrue(client.prompt.contains("动力电池检修"));
        Assertions.assertTrue(client.prompt.contains("100到180字"));
        Assertions.assertTrue(((TestableVtAiServiceImpl) service).containsEvent("message", "资源简介片段"));
        Assertions.assertTrue(((TestableVtAiServiceImpl) service).containsEvent("done", ""));
        Assertions.assertTrue(emitter.completed);
    }

    @Test
    public void experimentPromptShouldIncludeCoreFields()
    {
        CapturingClient client = new CapturingClient("实验简介片段");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        VtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
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
        Assertions.assertTrue(((TestableVtAiServiceImpl) service).containsEvent("message", "实验简介片段"));
        Assertions.assertTrue(((TestableVtAiServiceImpl) service).containsEvent("done", ""));
    }

    @Test
    public void blankResourceNameShouldReturnErrorEventWithoutCallingClient()
    {
        CapturingClient client = new CapturingClient("不会被调用");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        VtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("resource");

        service.generateIntroStream(request);

        Assertions.assertEquals(0, client.callCount);
        Assertions.assertTrue(((TestableVtAiServiceImpl) service).containsEvent("error", "资源名称不能为空"));
        Assertions.assertTrue(emitter.completed);
    }

    @Test
    public void clientExceptionShouldReturnErrorEvent()
    {
        CapturingClient client = new CapturingClient("不会返回");
        client.exception = new IllegalStateException("DeepSeek未配置");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        VtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("resource");
        request.setResourceName("新能源汽车电池虚拟仿真资源");

        service.generateIntroStream(request);

        Assertions.assertEquals(1, client.callCount);
        Assertions.assertTrue(((TestableVtAiServiceImpl) service).containsEvent("error", "DeepSeek未配置"));
        Assertions.assertTrue(emitter.completed);
    }

    @Test
    public void normalExceptionShouldReturnGenericErrorEvent()
    {
        CapturingClient client = new CapturingClient("不会返回");
        client.exception = new RuntimeException("网络异常");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        VtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("resource");
        request.setResourceName("新能源汽车电池虚拟仿真资源");

        service.generateIntroStream(request);

        Assertions.assertEquals(1, client.callCount);
        Assertions.assertTrue(((TestableVtAiServiceImpl) service).containsEvent("error", "AI生成失败，请稍后重试"));
        Assertions.assertTrue(emitter.completed);
    }

    @Test
    public void sendFailureShouldCompleteWithErrorAndStopBeforeDone()
    {
        CapturingClient client = new CapturingClient("资源简介片段");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        emitter.failOnSend = true;
        TestableVtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("resource");
        request.setResourceName("新能源汽车电池虚拟仿真资源");

        service.generateIntroStream(request);

        Assertions.assertEquals(1, client.callCount);
        Assertions.assertTrue(emitter.completedWithError);
        Assertions.assertFalse(emitter.completed);
        Assertions.assertFalse(service.containsEvent("done", ""));
    }

    @Test
    public void blankSceneShouldReturnErrorEventWithoutCallingClient()
    {
        CapturingClient client = new CapturingClient("不会被调用");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        VtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setResourceName("新能源汽车电池虚拟仿真资源");

        service.generateIntroStream(request);

        assertErrorWithoutCallingClient(client, emitter, "生成场景不能为空");
    }

    @Test
    public void unsupportedSceneShouldReturnErrorEventWithoutCallingClient()
    {
        CapturingClient client = new CapturingClient("不会被调用");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        VtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("course");
        request.setResourceName("新能源汽车电池虚拟仿真资源");

        service.generateIntroStream(request);

        assertErrorWithoutCallingClient(client, emitter, "生成场景不支持");
    }

    @Test
    public void blankExperimentNameShouldReturnErrorEventWithoutCallingClient()
    {
        CapturingClient client = new CapturingClient("不会被调用");
        CapturingSseEmitter emitter = new CapturingSseEmitter();
        VtAiServiceImpl service = new TestableVtAiServiceImpl(client, emitter);
        VtAiIntroRequest request = new VtAiIntroRequest();
        request.setScene("experiment");

        service.generateIntroStream(request);

        assertErrorWithoutCallingClient(client, emitter, "实验名称不能为空");
    }

    private void assertErrorWithoutCallingClient(CapturingClient client, CapturingSseEmitter emitter, String message)
    {
        Assertions.assertEquals(0, client.callCount);
        Assertions.assertTrue(((TestableVtAiServiceImpl) serviceForEmitter(emitter)).containsEvent("error", message));
        Assertions.assertTrue(emitter.completed);
    }

    private VtAiServiceImpl serviceForEmitter(CapturingSseEmitter emitter)
    {
        return emitter.service;
    }

    private static class CapturingClient implements IDeepSeekStreamClient
    {
        private final String content;
        private String prompt;
        private int callCount;
        private final List<String> chunks = new ArrayList<String>();
        private Exception exception;

        CapturingClient(String content)
        {
            this.content = content;
        }

        @Override
        public void streamIntro(String prompt, Consumer<String> onChunk) throws Exception
        {
            this.callCount++;
            this.prompt = prompt;
            if (exception != null)
            {
                throw exception;
            }
            this.chunks.add(content);
            onChunk.accept(content);
        }
    }

    private static class TestableVtAiServiceImpl extends VtAiServiceImpl
    {
        private final CapturingSseEmitter emitter;
        private final List<RecordedEvent> events = new ArrayList<RecordedEvent>();

        TestableVtAiServiceImpl(IDeepSeekStreamClient client, CapturingSseEmitter emitter)
        {
            super(client, new SyncTaskExecutor());
            this.emitter = emitter;
            this.emitter.service = this;
        }

        @Override
        protected SseEmitter createEmitter()
        {
            return emitter;
        }

        @Override
        protected void sendEvent(SseEmitter emitter, String eventName, String data)
        {
            events.add(new RecordedEvent(eventName, data));
            super.sendEvent(emitter, eventName, data);
        }

        private boolean containsEvent(String eventName, String data)
        {
            for (RecordedEvent event : events)
            {
                if (eventName.equals(event.eventName) && data.equals(event.data))
                {
                    return true;
                }
            }
            return false;
        }
    }

    private static class CapturingSseEmitter extends SseEmitter
    {
        private TestableVtAiServiceImpl service;
        private boolean completed;
        private boolean completedWithError;
        private boolean failOnSend;

        CapturingSseEmitter()
        {
            super(70000L);
        }

        @Override
        public void send(SseEventBuilder builder) throws IOException
        {
            if (failOnSend)
            {
                throw new IOException("SSE发送失败");
            }
        }

        @Override
        public synchronized void complete()
        {
            this.completed = true;
        }

        @Override
        public synchronized void completeWithError(Throwable ex)
        {
            this.completedWithError = true;
        }
    }

    private static class RecordedEvent
    {
        private final String eventName;
        private final String data;

        RecordedEvent(String eventName, String data)
        {
            this.eventName = eventName;
            this.data = data;
        }
    }
}
