package com.ruoyi.system.domain.virtualdomain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 虚拟仿真数据概览。
 */
public class VtDashboardSummary implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer labCount;
    private Integer resourceCount;
    private Integer deviceCount;
    private Integer participantCount;
    private BigDecimal completionRate;
    private BigDecimal averageScore;
    private List<Map<String, Object>> resourceTypeStats;
    private List<Map<String, Object>> deviceStatusStats;
    private List<Map<String, Object>> applyStatusStats;
    private List<Map<String, Object>> trainingTrend;

    public Integer getLabCount()
    {
        return labCount;
    }

    public void setLabCount(Integer labCount)
    {
        this.labCount = labCount;
    }

    public Integer getResourceCount()
    {
        return resourceCount;
    }

    public void setResourceCount(Integer resourceCount)
    {
        this.resourceCount = resourceCount;
    }

    public Integer getDeviceCount()
    {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount)
    {
        this.deviceCount = deviceCount;
    }

    public Integer getParticipantCount()
    {
        return participantCount;
    }

    public void setParticipantCount(Integer participantCount)
    {
        this.participantCount = participantCount;
    }

    public BigDecimal getCompletionRate()
    {
        return completionRate;
    }

    public void setCompletionRate(BigDecimal completionRate)
    {
        this.completionRate = completionRate;
    }

    public BigDecimal getAverageScore()
    {
        return averageScore;
    }

    public void setAverageScore(BigDecimal averageScore)
    {
        this.averageScore = averageScore;
    }

    public List<Map<String, Object>> getResourceTypeStats()
    {
        return resourceTypeStats;
    }

    public void setResourceTypeStats(List<Map<String, Object>> resourceTypeStats)
    {
        this.resourceTypeStats = resourceTypeStats;
    }

    public List<Map<String, Object>> getDeviceStatusStats()
    {
        return deviceStatusStats;
    }

    public void setDeviceStatusStats(List<Map<String, Object>> deviceStatusStats)
    {
        this.deviceStatusStats = deviceStatusStats;
    }

    public List<Map<String, Object>> getApplyStatusStats()
    {
        return applyStatusStats;
    }

    public void setApplyStatusStats(List<Map<String, Object>> applyStatusStats)
    {
        this.applyStatusStats = applyStatusStats;
    }

    public List<Map<String, Object>> getTrainingTrend()
    {
        return trainingTrend;
    }

    public void setTrainingTrend(List<Map<String, Object>> trainingTrend)
    {
        this.trainingTrend = trainingTrend;
    }
}
