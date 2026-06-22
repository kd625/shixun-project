package com.ruoyi.system.domain.virtualdomain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 实训实验对象 vt_experiment
 * 
 * @author ruoyi
 */
public class VtExperiment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 实验ID */
    @Excel(name = "实验ID")
    private Long experimentId;

    /** 实验名称 */
    @Excel(name = "实验名称")
    private String experimentName;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 资源ID */
    @Excel(name = "资源ID")
    private Long resourceId;

    /** 难度 */
    @Excel(name = "难度", readConverterExp = "1=初级,2=中级,3=高级")
    private String difficulty;

    /** 预计时长分钟 */
    @Excel(name = "预计时长分钟")
    private Integer durationMinutes;

    /** 开放状态 */
    @Excel(name = "开放状态", readConverterExp = "0=开放,1=关闭")
    private String openStatus;

    /** 实验简介 */
    @Excel(name = "实验简介")
    private String introduction;

    public Long getExperimentId()
    {
        return experimentId;
    }

    public void setExperimentId(Long experimentId)
    {
        this.experimentId = experimentId;
    }

    public String getExperimentName()
    {
        return experimentName;
    }

    public void setExperimentName(String experimentName)
    {
        this.experimentName = experimentName;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(Long resourceId)
    {
        this.resourceId = resourceId;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }

    public Integer getDurationMinutes()
    {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes)
    {
        this.durationMinutes = durationMinutes;
    }

    public String getOpenStatus()
    {
        return openStatus;
    }

    public void setOpenStatus(String openStatus)
    {
        this.openStatus = openStatus;
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
            .append("experimentId", getExperimentId())
            .append("experimentName", getExperimentName())
            .append("courseId", getCourseId())
            .append("resourceId", getResourceId())
            .append("difficulty", getDifficulty())
            .append("durationMinutes", getDurationMinutes())
            .append("openStatus", getOpenStatus())
            .append("introduction", getIntroduction())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
