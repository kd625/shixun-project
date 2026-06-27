package com.ruoyi.web.controller.virtual;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 虚拟仿真业务Controller冒烟测试。
 */
public class VirtualControllerSmokeTest
{
    @Test
    public void controllerClassesShouldLoad()
    {
        Assertions.assertNotNull(VtLabController.class);
        Assertions.assertNotNull(VtDeviceController.class);
        Assertions.assertNotNull(VtResourceController.class);
        Assertions.assertNotNull(VtShareApplyController.class);
        Assertions.assertNotNull(VtCourseController.class);
        Assertions.assertNotNull(VtExperimentController.class);
        Assertions.assertNotNull(VtTeachingPlanController.class);
        Assertions.assertNotNull(VtTrainingRecordController.class);
        Assertions.assertNotNull(VtDashboardController.class);
        Assertions.assertNotNull(VtPortalController.class);
        Assertions.assertNotNull(VtAiController.class);
    }
}
