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
import com.ruoyi.system.domain.virtualdomain.VtDevice;
import com.ruoyi.system.service.virtualservice.IVtDeviceService;

/**
 * 仿真设备Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/virtual/device")
public class VtDeviceController extends BaseController
{
    @Autowired
    private IVtDeviceService deviceService;

    @PreAuthorize("@ss.hasPermi('virtual:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(VtDevice device)
    {
        startPage();
        List<VtDevice> list = deviceService.selectVtDeviceList(device);
        return getDataTable(list);
    }

    @Log(title = "仿真设备", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('virtual:device:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, VtDevice device)
    {
        List<VtDevice> list = deviceService.selectVtDeviceList(device);
        ExcelUtil<VtDevice> util = new ExcelUtil<VtDevice>(VtDevice.class);
        util.exportExcel(response, list, "仿真设备数据");
    }

    @PreAuthorize("@ss.hasPermi('virtual:device:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(deviceService.selectVtDeviceById(deviceId));
    }

    @PreAuthorize("@ss.hasPermi('virtual:device:add')")
    @Log(title = "仿真设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VtDevice device)
    {
        device.setCreateBy(getUsername());
        return toAjax(deviceService.insertVtDevice(device));
    }

    @PreAuthorize("@ss.hasPermi('virtual:device:edit')")
    @Log(title = "仿真设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VtDevice device)
    {
        device.setUpdateBy(getUsername());
        return toAjax(deviceService.updateVtDevice(device));
    }

    @PreAuthorize("@ss.hasPermi('virtual:device:remove')")
    @Log(title = "仿真设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(deviceService.deleteVtDeviceByIds(deviceIds));
    }
}
