package com.ruoyi;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.status.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志配置测试。
 */
public class LogbackConfigurationTest
{
    @TempDir
    public Path logPath;

    @Test
    public void logPathShouldBeOverridableForLocalStartup() throws Exception
    {
        String previousLogPath = System.getProperty("LOG_PATH");
        System.setProperty("LOG_PATH", logPath.toString());

        LoggerContext context = new LoggerContext();
        try
        {
            URL config = Thread.currentThread().getContextClassLoader().getResource("logback.xml");
            Assertions.assertNotNull(config);

            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            configurator.doConfigure(config);

            List<Status> errors = new ArrayList<Status>();
            for (Status status : context.getStatusManager().getCopyOfStatusList())
            {
                if (status.getLevel() >= Status.ERROR)
                {
                    errors.add(status);
                }
            }

            Assertions.assertTrue(errors.isEmpty(), errors.toString());
            Assertions.assertTrue(Files.exists(logPath.resolve("sys-info.log")));
            Assertions.assertTrue(Files.exists(logPath.resolve("sys-error.log")));
            Assertions.assertTrue(Files.exists(logPath.resolve("sys-user.log")));
        }
        finally
        {
            context.stop();
            if (previousLogPath == null)
            {
                System.clearProperty("LOG_PATH");
            }
            else
            {
                System.setProperty("LOG_PATH", previousLogPath);
            }
        }
    }
}
