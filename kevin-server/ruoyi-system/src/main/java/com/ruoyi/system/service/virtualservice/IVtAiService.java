package com.ruoyi.system.service.virtualservice;

import com.ruoyi.system.domain.virtualdomain.VtAiIntroRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IVtAiService
{
    public SseEmitter generateIntroStream(VtAiIntroRequest request);
}
