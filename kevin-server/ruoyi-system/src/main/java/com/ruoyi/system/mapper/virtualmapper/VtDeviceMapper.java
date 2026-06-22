package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtDevice;

/**
 * 仿真设备Mapper接口
 * 
 * @author ruoyi
 */
public interface VtDeviceMapper
{
    public VtDevice selectVtDeviceById(Long deviceId);

    public List<VtDevice> selectVtDeviceList(VtDevice device);

    public int insertVtDevice(VtDevice device);

    public int updateVtDevice(VtDevice device);

    public int deleteVtDeviceById(Long deviceId);

    public int deleteVtDeviceByIds(Long[] deviceIds);
}
