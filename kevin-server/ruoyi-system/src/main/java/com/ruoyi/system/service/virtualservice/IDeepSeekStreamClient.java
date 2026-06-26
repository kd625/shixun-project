package com.ruoyi.system.service.virtualservice;

import java.util.function.Consumer;

public interface IDeepSeekStreamClient
{
    public void streamIntro(String prompt, Consumer<String> onChunk) throws Exception;
}
