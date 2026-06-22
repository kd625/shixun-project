package com.ruoyi.system.domain.virtualdomain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 共享申请对象 vt_share_apply
 * 
 * @author ruoyi
 */
public class VtShareApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    @Excel(name = "申请ID")
    private Long applyId;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applicantName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 单位 */
    @Excel(name = "单位")
    private String organization;

    /** 申请类型 */
    @Excel(name = "申请类型", readConverterExp = "0=资源,1=实验室")
    private String applyType;

    /** 关联对象ID */
    @Excel(name = "关联对象ID")
    private Long targetId;

    /** 关联对象名称 */
    @Excel(name = "关联对象名称")
    private String targetName;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预约时间")
    private Date reserveTime;

    /** 使用人数 */
    @Excel(name = "使用人数")
    private Integer userCount;

    /** 申请状态 */
    @Excel(name = "申请状态", readConverterExp = "0=待审核,1=通过,2=驳回")
    private String applyStatus;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String auditOpinion;

    public Long getApplyId()
    {
        return applyId;
    }

    public void setApplyId(Long applyId)
    {
        this.applyId = applyId;
    }

    public String getApplicantName()
    {
        return applicantName;
    }

    public void setApplicantName(String applicantName)
    {
        this.applicantName = applicantName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getOrganization()
    {
        return organization;
    }

    public void setOrganization(String organization)
    {
        this.organization = organization;
    }

    public String getApplyType()
    {
        return applyType;
    }

    public void setApplyType(String applyType)
    {
        this.applyType = applyType;
    }

    public Long getTargetId()
    {
        return targetId;
    }

    public void setTargetId(Long targetId)
    {
        this.targetId = targetId;
    }

    public String getTargetName()
    {
        return targetName;
    }

    public void setTargetName(String targetName)
    {
        this.targetName = targetName;
    }

    public Date getReserveTime()
    {
        return reserveTime;
    }

    public void setReserveTime(Date reserveTime)
    {
        this.reserveTime = reserveTime;
    }

    public Integer getUserCount()
    {
        return userCount;
    }

    public void setUserCount(Integer userCount)
    {
        this.userCount = userCount;
    }

    public String getApplyStatus()
    {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus)
    {
        this.applyStatus = applyStatus;
    }

    public String getAuditOpinion()
    {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion)
    {
        this.auditOpinion = auditOpinion;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("applyId", getApplyId())
            .append("applicantName", getApplicantName())
            .append("phone", getPhone())
            .append("organization", getOrganization())
            .append("applyType", getApplyType())
            .append("targetId", getTargetId())
            .append("targetName", getTargetName())
            .append("reserveTime", getReserveTime())
            .append("userCount", getUserCount())
            .append("applyStatus", getApplyStatus())
            .append("auditOpinion", getAuditOpinion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
