package com.ruoyi.system.domain.virtualdomain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 过程结果对象 vt_training_record
 * 
 * @author ruoyi
 */
public class VtTrainingRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @Excel(name = "记录ID")
    private Long recordId;

    /** 计划ID */
    @Excel(name = "计划ID")
    private Long planId;

    /** 学员姓名 */
    @Excel(name = "学员姓名")
    private String studentName;

    /** 学号或编号 */
    @Excel(name = "学号或编号")
    private String studentNo;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间")
    private Date endTime;

    /** 完成状态 */
    @Excel(name = "完成状态", readConverterExp = "0=未完成,1=已完成,2=异常")
    private String completeStatus;

    /** 得分 */
    @Excel(name = "得分")
    private BigDecimal score;

    /** 用时分钟 */
    @Excel(name = "用时分钟")
    private Integer durationMinutes;

    /** 评价 */
    @Excel(name = "评价")
    private String evaluation;

    public Long getRecordId()
    {
        return recordId;
    }

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getPlanId()
    {
        return planId;
    }

    public void setPlanId(Long planId)
    {
        this.planId = planId;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
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

    public String getCompleteStatus()
    {
        return completeStatus;
    }

    public void setCompleteStatus(String completeStatus)
    {
        this.completeStatus = completeStatus;
    }

    public BigDecimal getScore()
    {
        return score;
    }

    public void setScore(BigDecimal score)
    {
        this.score = score;
    }

    public Integer getDurationMinutes()
    {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes)
    {
        this.durationMinutes = durationMinutes;
    }

    public String getEvaluation()
    {
        return evaluation;
    }

    public void setEvaluation(String evaluation)
    {
        this.evaluation = evaluation;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("planId", getPlanId())
            .append("studentName", getStudentName())
            .append("studentNo", getStudentNo())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("completeStatus", getCompleteStatus())
            .append("score", getScore())
            .append("durationMinutes", getDurationMinutes())
            .append("evaluation", getEvaluation())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
