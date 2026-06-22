package com.ruoyi.web.controller.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.virtualservice.IVtDashboardService;

/**
 * 虚拟仿真数据概览Controller。
 */
@RestController
@RequestMapping("/virtual/dashboard")
public class VtDashboardController extends BaseController
{
    @Autowired
    private IVtDashboardService vtDashboardService;

    @PreAuthorize("@ss.hasPermi('virtual:dashboard:list')")
    @GetMapping("/summary")
    public AjaxResult summary()
    {
        return success(vtDashboardService.selectSummary());
    }
}
