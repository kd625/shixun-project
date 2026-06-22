package com.ruoyi.system.mapper.virtualmapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 虚拟仿真数据概览Mapper接口。
 */
public interface VtDashboardMapper
{
    public Integer countLabs();

    public Integer countResources();

    public Integer countDevices();

    public Integer countParticipants();

    public BigDecimal selectCompletionRate();

    public BigDecimal selectAverageScore();

    public List<Map<String, Object>> selectResourceTypeStats();

    public List<Map<String, Object>> selectDeviceStatusStats();

    public List<Map<String, Object>> selectApplyStatusStats();

    public List<Map<String, Object>> selectTrainingTrend();
}
