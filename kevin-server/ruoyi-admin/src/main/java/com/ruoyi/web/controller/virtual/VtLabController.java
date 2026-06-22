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
import com.ruoyi.system.domain.virtualdomain.VtLab;
import com.ruoyi.system.service.virtualservice.IVtLabService;

/**
 * 实验室Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/virtual/lab")
public class VtLabController extends BaseController
{
    @Autowired
    private IVtLabService labService;

    @PreAuthorize("@ss.hasPermi('virtual:lab:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtLab lab)
    {
        startPage();
        List<VtLab> list = labService.selectVtLabList(lab);
        return getDataTable(list);
    }

    @Log(title = "实验室", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:lab:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtLab lab)
    {
        List<VtLab> list = labService.selectVtLabList(lab);
        ExcelUtil<VtLab> util = new ExcelUtil<VtLab>(VtLab.class);
        util.exportExcel(response, list, "实验室数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:lab:query')")
    @GetMapping(value = "/{labId}")
    public AjaxResult getInfo(@PathVariable("labId") Long labId)
    {
        return success(labService.selectVtLabById(labId));
    }

    @PreAuthorize("@ss.hasPermi('virtual:lab:add')")
    @Log(title = "实验室", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VtLab lab)
    {
        lab.setCreateBy(getUsername());
        return toAjax(labService.insertVtLab(lab));
    }

    @PreAuthorize("@ss.hasPermi('virtual:lab:edit')")
    @Log(title = "实验室", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VtLab lab)
    {
        lab.setUpdateBy(getUsername());
        return toAjax(labService.updateVtLab(lab));
    }

    @PreAuthorize("@ss.hasPermi('virtual:lab:remove')")
    @Log(title = "实验室", businessType = BusinessType.DELETE)
    @DeleteMapping("/{labIds}")
    public AjaxResult remove(@PathVariable Long[] labIds)
    {
        return toAjax(labService.deleteVtLabByIds(labIds));
    }

    /**
     * 获取Lab选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return success(labService.selectVtLabList(new VtLab()));
    }
}
