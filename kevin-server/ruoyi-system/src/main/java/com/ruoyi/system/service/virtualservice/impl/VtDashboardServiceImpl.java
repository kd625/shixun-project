package com.ruoyi.system.service.virtualservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtDashboardSummary;
import com.ruoyi.system.mapper.virtualmapper.VtDashboardMapper;
import com.ruoyi.system.service.virtualservice.IVtDashboardService;

/**
 * 虚拟仿真数据概览Service业务层处理。
 */
@Service
public class VtDashboardServiceImpl implements IVtDashboardService
{
    @Autowired
    private VtDashboardMapper vtDashboardMapper;

    @Override
    public VtDashboardSummary selectSummary()
    {
        VtDashboardSummary summary = new VtDashboardSummary();
        summary.setLabCount(vtDashboardMapper.countLabs());
        summary.setResourceCount(vtDashboardMapper.countResources());
        summary.setDeviceCount(vtDashboardMapper.countDevices());
        summary.setParticipantCount(vtDashboardMapper.countParticipants());
        summary.setCompletionRate(vtDashboardMapper.selectCompletionRate());
        summary.setAverageScore(vtDashboardMapper.selectAverageScore());
        summary.setResourceTypeStats(vtDashboardMapper.selectResourceTypeStats());
        summary.setDeviceStatusStats(vtDashboardMapper.selectDeviceStatusStats());
        summary.setApplyStatusStats(vtDashboardMapper.selectApplyStatusStats());
        summary.setTrainingTrend(vtDashboardMapper.selectTrainingTrend());
        return summary;
    }
}
