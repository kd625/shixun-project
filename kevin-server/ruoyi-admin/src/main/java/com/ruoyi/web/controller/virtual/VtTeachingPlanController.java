package com.ruoyi.web.controller.virtual;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.virtualdomain.VtTeachingPlan;
import com.ruoyi.system.service.virtualservice.IVtTeachingPlanService;

/**
 * 教学计划Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/virtual/plan")
public class VtTeachingPlanController extends BaseController
{
    @Autowired
    private IVtTeachingPlanService teachingPlanService;

    @PreAuthorize("@ss.hasPermi('virtual:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtTeachingPlan teachingPlan)
    {
        startPage();
        List<VtTeachingPlan> list = teachingPlanService.selectVtTeachingPlanList(teachingPlan);
        return getDataTable(list);
    }

    @Log(title = "教学计划", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:plan:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtTeachingPlan teachingPlan)
    {
        List<VtTeachingPlan> list = teachingPlanService.selectVtTeachingPlanList(teachingPlan);
        ExcelUtil<VtTeachingPlan> util = new ExcelUtil<VtTeachingPlan>(VtTeachingPlan.class);
        util.exportExcel(response, list, "教学计划数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:plan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId)
    {
        return success(teachingPlanService.selectVtTeachingPlanById(planId));
    }

    @PreAuthorize("@ss.hasPermi('virtual:plan:add')")
    @Log(title = "教学计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VtTeachingPlan teachingPlan)
    {
        teachingPlan.setCreateBy(getUsername());
        return toAjax(teachingPlanService.insertVtTeachingPlan(teachingPlan));
    }

    @PreAuthorize("@ss.hasPermi('virtual:plan:edit')")
    @Log(title = "教学计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VtTeachingPlan teachingPlan)
    {
        teachingPlan.setUpdateBy(getUsername());
        return toAjax(teachingPlanService.updateVtTeachingPlan(teachingPlan));
    }

    @PreAuthorize("@ss.hasPermi('virtual:plan:remove')")
    @Log(title = "教学计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds)
    {
        return toAjax(teachingPlanService.deleteVtTeachingPlanByIds(planIds));
    }
}
