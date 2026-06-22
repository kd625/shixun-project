package com.ruoyi.system.domain.virtualdomain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 实验室对象 vt_lab
 * 
 * @author ruoyi
 */
public class VtLab extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 实验室ID */
    @Excel(name = "实验室ID")
    private Long labId;

    /** 实验室名称 */
    @Excel(name = "实验室名称")
    private String labName;

    /** 所属院校或院系 */
    @Excel(name = "所属院校或院系")
    private String collegeName;

    /** 地点 */
    @Excel(name = "地点")
    private String location;

    /** 容量 */
    @Excel(name = "容量")
    private Integer capacity;

    /** 开放状态 */
    @Excel(name = "开放状态", readConverterExp = "0=开放,1=关闭,2=维护")
    private String openStatus;

    /** 负责人 */
    @Excel(name = "负责人")
    private String managerName;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String contactPhone;

    /** 简介 */
    @Excel(name = "简介")
    private String introduction;

    public Long getLabId()
    {
        return labId;
    }

    public void setLabId(Long labId)
    {
        this.labId = labId;
    }

    public String getLabName()
    {
        return labName;
    }

    public void setLabName(String labName)
    {
        this.labName = labName;
    }

    public String getCollegeName()
    {
        return collegeName;
    }

    public void setCollegeName(String collegeName)
    {
        this.collegeName = collegeName;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Integer getCapacity()
    {
        return capacity;
    }

    public void setCapacity(Integer capacity)
    {
        this.capacity = capacity;
    }

    public String getOpenStatus()
    {
        return openStatus;
    }

    public void setOpenStatus(String openStatus)
    {
        this.openStatus = openStatus;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public String getIntroduction()
    {
        return introduction;
    }

    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("labId", getLabId())
            .append("labName", getLabName())
            .append("collegeName", getCollegeName())
            .append("location", getLocation())
            .append("capacity", getCapacity())
            .append("openStatus", getOpenStatus())
            .append("managerName", getManagerName())
            .append("contactPhone", getContactPhone())
            .append("introduction", getIntroduction())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
