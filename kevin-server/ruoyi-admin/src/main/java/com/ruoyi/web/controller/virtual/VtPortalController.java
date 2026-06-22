package com.ruoyi.web.controller.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.virtualdomain.VtExperiment;
import com.ruoyi.system.domain.virtualdomain.VtLab;
import com.ruoyi.system.domain.virtualdomain.VtResource;
import com.ruoyi.system.domain.virtualdomain.VtShareApply;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.system.service.virtualservice.IVtDashboardService;
import com.ruoyi.system.service.virtualservice.IVtExperimentService;
import com.ruoyi.system.service.virtualservice.IVtLabService;
import com.ruoyi.system.service.virtualservice.IVtResourceService;
import com.ruoyi.system.service.virtualservice.IVtShareApplyService;

/**
 * 虚拟仿真门户公开接口。
 */
@Anonymous
@RestController
@RequestMapping("/portal")
public class VtPortalController extends BaseController
{
    @Autowired
    private IVtDashboardService vtDashboardService;

    @Autowired
    private IVtResourceService vtResourceService;

    @Autowired
    private IVtLabService vtLabService;

    @Autowired
    private IVtExperimentService vtExperimentService;

    @Autowired
    private IVtShareApplyService vtShareApplyService;

    @Autowired
    private ISysNoticeService noticeService;

    @GetMapping("/home")
    public AjaxResult home()
    {
        return success(vtDashboardService.selectSummary());
    }

    @GetMapping("/news")
    public AjaxResult news(SysNotice query)
    {
        query.setStatus("0");
        return success(noticeService.selectNoticeList(query));
    }

    @GetMapping("/resources")
    public AjaxResult resources(VtResource query)
    {
        return success(vtResourceService.selectVtResourceList(query));
    }

    @GetMapping("/labs")
    public AjaxResult labs(VtLab query)
    {
        return success(vtLabService.selectVtLabList(query));
    }

    @GetMapping("/experiments")
    public AjaxResult experiments(VtExperiment query)
    {
        return success(vtExperimentService.selectVtExperimentList(query));
    }

    @GetMapping("/screen")
    public AjaxResult screen()
    {
        return success(vtDashboardService.selectSummary());
    }

    @PostMapping("/share")
    public AjaxResult share(@RequestBody VtShareApply apply)
    {
        apply.setApplyStatus("0");
        apply.setCreateBy("portal");
        return toAjax(vtShareApplyService.insertVtShareApply(apply));
    }
}
