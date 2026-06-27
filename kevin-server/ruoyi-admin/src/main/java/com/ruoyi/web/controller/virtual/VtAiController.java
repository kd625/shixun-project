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
