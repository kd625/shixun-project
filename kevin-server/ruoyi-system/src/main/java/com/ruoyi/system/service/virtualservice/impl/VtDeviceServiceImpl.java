package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtDevice;
import com.ruoyi.system.mapper.virtualmapper.VtDeviceMapper;
import com.ruoyi.system.service.virtualservice.IVtDeviceService;

/**
 * 仿真设备Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class VtDeviceServiceImpl implements IVtDeviceService
{
    @Autowired
    private VtDeviceMapper deviceMapper;

    @Override
    public VtDevice selectVtDeviceById(Long deviceId)
    {
        return deviceMapper.selectVtDeviceById(deviceId);
    }

    @Override
    public List<VtDevice> selectVtDeviceList(VtDevice device)
    {
        return deviceMapper.selectVtDeviceList(device);
    }

    @Override
    public int insertVtDevice(VtDevice device)
    {
        return deviceMapper.insertVtDevice(device);
    }

    @Override
    public int updateVtDevice(VtDevice device)
    {
        return deviceMapper.updateVtDevice(device);
    }

    @Override
    public int deleteVtDeviceByIds(Long[] deviceIds)
    {
        return deviceMapper.deleteVtDeviceByIds(deviceIds);
    }

    @Override
    public int deleteVtDeviceById(Long deviceId)
    {
        return deviceMapper.deleteVtDeviceById(deviceId);
    }
}
