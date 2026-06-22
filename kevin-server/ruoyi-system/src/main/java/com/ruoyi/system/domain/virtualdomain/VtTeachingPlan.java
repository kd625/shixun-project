package com.ruoyi.system.domain.virtualdomain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 教学计划对象 vt_teaching_plan
 * 
 * @author ruoyi
 */
public class VtTeachingPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 计划ID */
    @Excel(name = "计划ID")
    private Long planId;

    /** 计划名称 */
    @Excel(name = "计划名称")
    private String planName;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 实验ID */
    @Excel(name = "实验ID")
    private Long experimentId;

    /** 班级或对象 */
    @Excel(name = "班级或对象")
    private String classTarget;

    /** 计划开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计划开始时间")
    private Date startTime;

    /** 计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计划结束时间")
    private Date endTime;

    /** 计划状态 */
    @Excel(name = "计划状态", readConverterExp = "0=未开始,1=进行中,2=已结束")
    private String planStatus;

    /** 负责人 */
    @Excel(name = "负责人")
    private String managerName;

    public Long getPlanId()
    {
        return planId;
    }

    public void setPlanId(Long planId)
    {
        this.planId = planId;
    }

    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getExperimentId()
    {
        return experimentId;
    }

    public void setExperimentId(Long experimentId)
    {
        this.experimentId = experimentId;
    }

    public String getClassTarget()
    {
        return classTarget;
    }

    public void setClassTarget(String classTarget)
    {
        this.classTarget = classTarget;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getPlanStatus()
    {
        return planStatus;
    }

    public void setPlanStatus(String planStatus)
    {
        this.planStatus = planStatus;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("planId", getPlanId())
            .append("planName", getPlanName())
            .append("courseId", getCourseId())
            .append("experimentId", getExperimentId())
            .append("classTarget", getClassTarget())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("planStatus", getPlanStatus())
            .append("managerName", getManagerName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
