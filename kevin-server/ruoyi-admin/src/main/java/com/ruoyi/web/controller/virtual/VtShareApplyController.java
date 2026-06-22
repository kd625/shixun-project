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
import com.ruoyi.system.domain.virtualdomain.VtShareApply;
import com.ruoyi.system.service.virtualservice.IVtShareApplyService;

/**
 * 共享申请Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/virtual/shareApply")
public class VtShareApplyController extends BaseController
{
    @Autowired
    private IVtShareApplyService shareApplyService;

    @PreAuthorize("@ss.hasPermi('virtual:shareApply:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtShareApply shareApply)
    {
        startPage();
        List<VtShareApply> list = shareApplyService.selectVtShareApplyList(shareApply);
        return getDataTable(list);
    }

    @Log(title = "共享申请", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:shareApply:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtShareApply shareApply)
    {
        List<VtShareApply> list = shareApplyService.selectVtShareApplyList(shareApply);
        ExcelUtil<VtShareApply> util = new ExcelUtil<VtShareApply>(VtShareApply.class);
        util.exportExcel(response, list, "共享申请数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:shareApply:query')")
    @GetMapping(value = "/{applyId}")
    public AjaxResult getInfo(@PathVariable("applyId") Long applyId)
    {
        return success(shareApplyService.selectVtShareApplyById(applyId));
    }

    @PreAuthorize("@ss.hasPermi('virtual:shareApply:add')")
    @Log(title = "共享申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VtShareApply shareApply)
    {
        shareApply.setCreateBy(getUsername());
        return toAjax(shareApplyService.insertVtShareApply(shareApply));
    }

    @PreAuthorize("@ss.hasPermi('virtual:shareApply:edit')")
    @Log(title = "共享申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VtShareApply shareApply)
    {
        shareApply.setUpdateBy(getUsername());
        return toAjax(shareApplyService.updateVtShareApply(shareApply));
    }

    @PreAuthorize("@ss.hasPermi('virtual:shareApply:remove')")
    @Log(title = "共享申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applyIds}")
    public AjaxResult remove(@PathVariable Long[] applyIds)
    {
        return toAjax(shareApplyService.deleteVtShareApplyByIds(applyIds));
    }

    @PreAuthorize("@ss.hasPermi('virtual:shareApply:audit')")
    @Log(title = "共享申请审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody VtShareApply shareApply)
    {
        shareApply.setUpdateBy(getUsername());
        return toAjax(shareApplyService.updateVtShareApply(shareApply));
    }
}
