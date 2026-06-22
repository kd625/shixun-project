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
import com.ruoyi.system.domain.virtualdomain.VtExperiment;
import com.ruoyi.system.service.virtualservice.IVtExperimentService;

/**
 * 实训实验Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/virtual/experiment")
public class VtExperimentController extends BaseController
{
    @Autowired
    private IVtExperimentService experimentService;

    @PreAuthorize("@ss.hasPermi('virtual:experiment:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtExperiment experiment)
    {
        startPage();
        List<VtExperiment> list = experimentService.selectVtExperimentList(experiment);
        return getDataTable(list);
    }

    @Log(title = "实训实验", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:experiment:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtExperiment experiment)
    {
        List<VtExperiment> list = experimentService.selectVtExperimentList(experiment);
        ExcelUtil<VtExperiment> util = new ExcelUtil<VtExperiment>(VtExperiment.class);
        util.exportExcel(response, list, "实训实验数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:experiment:query')")
    @GetMapping(value = "/{experimentId}")
    public AjaxResult getInfo(@PathVariable("experimentId") Long experimentId)
    {
        return success(experimentService.selectVtExperimentById(experimentId));
    }

    @PreAuthorize("@ss.hasPermi('virtual:experiment:add')")
    @Log(title = "实训实验", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VtExperiment experiment)
    {
        experiment.setCreateBy(getUsername());
        return toAjax(experimentService.insertVtExperiment(experiment));
    }

    @PreAuthorize("@ss.hasPermi('virtual:experiment:edit')")
    @Log(title = "实训实验", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VtExperiment experiment)
    {
        experiment.setUpdateBy(getUsername());
        return toAjax(experimentService.updateVtExperiment(experiment));
    }

    @PreAuthorize("@ss.hasPermi('virtual:experiment:remove')")
    @Log(title = "实训实验", businessType = BusinessType.DELETE)
    @DeleteMapping("/{experimentIds}")
    public AjaxResult remove(@PathVariable Long[] experimentIds)
    {
        return toAjax(experimentService.deleteVtExperimentByIds(experimentIds));
    }

    /**
     * 获取Experiment选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return success(experimentService.selectVtExperimentList(new VtExperiment()));
    }
}
