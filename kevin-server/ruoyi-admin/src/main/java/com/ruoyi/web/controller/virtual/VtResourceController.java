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
import com.ruoyi.system.domain.virtualdomain.VtResource;
import com.ruoyi.system.service.virtualservice.IVtResourceService;

/**
 * 实训资源Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/virtual/resource")
public class VtResourceController extends BaseController
{
    @Autowired
    private IVtResourceService resourceService;

    @PreAuthorize("@ss.hasPermi('virtual:resource:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtResource resource)
    {
        startPage();
        List<VtResource> list = resourceService.selectVtResourceList(resource);
        return getDataTable(list);
    }

    @Log(title = "实训资源", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:resource:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtResource resource)
    {
        List<VtResource> list = resourceService.selectVtResourceList(resource);
        ExcelUtil<VtResource> util = new ExcelUtil<VtResource>(VtResource.class);
        util.exportExcel(response, list, "实训资源数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:resource:query')")
    @GetMapping(value = "/{resourceId}")
    public AjaxResult getInfo(@PathVariable("resourceId") Long resourceId)
    {
        return success(resourceService.selectVtResourceById(resourceId));
    }

    @PreAuthorize("@ss.hasPermi('virtual:resource:add')")
    @Log(title = "实训资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VtResource resource)
    {
        resource.setCreateBy(getUsername());
        return toAjax(resourceService.insertVtResource(resource));
    }

    @PreAuthorize("@ss.hasPermi('virtual:resource:edit')")
    @Log(title = "实训资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VtResource resource)
    {
        resource.setUpdateBy(getUsername());
        return toAjax(resourceService.updateVtResource(resource));
    }

    @PreAuthorize("@ss.hasPermi('virtual:resource:remove')")
    @Log(title = "实训资源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{resourceIds}")
    public AjaxResult remove(@PathVariable Long[] resourceIds)
    {
        return toAjax(resourceService.deleteVtResourceByIds(resourceIds));
    }

    /**
     * 获取Resource选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return success(resourceService.selectVtResourceList(new VtResource()));
    }
}
