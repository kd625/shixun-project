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
import com.ruoyi.system.domain.virtualdomain.VtTrainingRecord;
import com.ruoyi.system.service.virtualservice.IVtTrainingRecordService;

/**
 * 过程结果Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/virtual/record")
public class VtTrainingRecordController extends BaseController
{
    @Autowired
    private IVtTrainingRecordService trainingRecordService;

    @PreAuthorize("@ss.hasPermi('virtual:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtTrainingRecord trainingRecord)
    {
        startPage();
        List<VtTrainingRecord> list = trainingRecordService.selectVtTrainingRecordList(trainingRecord);
        return getDataTable(list);
    }

    @Log(title = "过程结果", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:record:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtTrainingRecord trainingRecord)
    {
        List<VtTrainingRecord> list = trainingRecordService.selectVtTrainingRecordList(trainingRecord);
        ExcelUtil<VtTrainingRecord> util = new ExcelUtil<VtTrainingRecord>(VtTrainingRecord.class);
        util.exportExcel(response, list, "过程结果数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(trainingRecordService.selectVtTrainingRecordById(recordId));
    }

    @PreAuthorize("@ss.hasPermi('virtual:record:add')")
    @Log(title = "过程结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VtTrainingRecord trainingRecord)
    {
        trainingRecord.setCreateBy(getUsername());
        return toAjax(trainingRecordService.insertVtTrainingRecord(trainingRecord));
    }

    @PreAuthorize("@ss.hasPermi('virtual:record:edit')")
    @Log(title = "过程结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VtTrainingRecord trainingRecord)
    {
        trainingRecord.setUpdateBy(getUsername());
        return toAjax(trainingRecordService.updateVtTrainingRecord(trainingRecord));
    }

    @PreAuthorize("@ss.hasPermi('virtual:record:remove')")
    @Log(title = "过程结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(trainingRecordService.deleteVtTrainingRecordByIds(recordIds));
    }
}
