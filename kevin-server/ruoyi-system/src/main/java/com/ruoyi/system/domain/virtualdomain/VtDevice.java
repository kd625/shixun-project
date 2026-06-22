package com.ruoyi.system.domain.virtualdomain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 仿真设备对象 vt_device
 * 
 * @author ruoyi
 */
public class VtDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceCode;

    /** 所属实验室ID */
    @Excel(name = "所属实验室ID")
    private Long labId;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceType;

    /** 运行状态 */
    @Excel(name = "运行状态", readConverterExp = "0=正常,1=维护,2=故障")
    private String runStatus;

    /** 在线状态 */
    @Excel(name = "在线状态", readConverterExp = "0=在线,1=离线")
    private String onlineStatus;

    /** 最近检测时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最近检测时间")
    private Date lastCheckTime;

    public Long getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceCode()
    {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode)
    {
        this.deviceCode = deviceCode;
    }

    public Long getLabId()
    {
        return labId;
    }

    public void setLabId(Long labId)
    {
        this.labId = labId;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getRunStatus()
    {
        return runStatus;
    }

    public void setRunStatus(String runStatus)
    {
        this.runStatus = runStatus;
    }

    public String getOnlineStatus()
    {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus)
    {
        this.onlineStatus = onlineStatus;
    }

    public Date getLastCheckTime()
    {
        return lastCheckTime;
    }

    public void setLastCheckTime(Date lastCheckTime)
    {
        this.lastCheckTime = lastCheckTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("deviceCode", getDeviceCode())
            .append("labId", getLabId())
            .append("deviceType", getDeviceType())
            .append("runStatus", getRunStatus())
            .append("onlineStatus", getOnlineStatus())
            .append("lastCheckTime", getLastCheckTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
