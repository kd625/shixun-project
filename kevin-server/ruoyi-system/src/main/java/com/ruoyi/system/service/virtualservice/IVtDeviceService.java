package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtDevice;

/**
 * 仿真设备Service接口
 * 
 * @author ruoyi
 */
public interface IVtDeviceService
{
    public VtDevice selectVtDeviceById(Long deviceId);

    public List<VtDevice> selectVtDeviceList(VtDevice device);

    public int insertVtDevice(VtDevice device);

    public int updateVtDevice(VtDevice device);

    public int deleteVtDeviceByIds(Long[] deviceIds);

    public int deleteVtDeviceById(Long deviceId);
}
