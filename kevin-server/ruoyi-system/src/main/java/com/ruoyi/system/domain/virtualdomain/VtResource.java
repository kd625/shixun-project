package com.ruoyi.system.domain.virtualdomain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 实训资源对象 vt_resource
 * 
 * @author ruoyi
 */
public class VtResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源ID */
    @Excel(name = "资源ID")
    private Long resourceId;

    /** 资源名称 */
    @Excel(name = "资源名称")
    private String resourceName;

    /** 资源类型 */
    @Excel(name = "资源类型", readConverterExp = "0=虚拟仿真,1=视频,2=音频,3=文档")
    private String resourceType;

    /** 所属专业 */
    @Excel(name = "所属专业")
    private String majorName;

    /** 适用课程 */
    @Excel(name = "适用课程")
    private String courseName;

    /** 封面地址 */
    @Excel(name = "封面地址")
    private String coverUrl;

    /** 附件地址 */
    @Excel(name = "附件地址")
    private String fileUrl;

    /** 共享状态 */
    @Excel(name = "共享状态", readConverterExp = "0=开放,1=校内,2=停用")
    private String shareStatus;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Integer viewCount;

    /** 收藏量 */
    @Excel(name = "收藏量")
    private Integer collectCount;

    /** 简介 */
    @Excel(name = "简介")
    private String introduction;

    public Long getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(Long resourceId)
    {
        this.resourceId = resourceId;
    }

    public String getResourceName()
    {
        return resourceName;
    }

    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
    }

    public String getResourceType()
    {
        return resourceType;
    }

    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }

    public String getMajorName()
    {
        return majorName;
    }

    public void setMajorName(String majorName)
    {
        this.majorName = majorName;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getCoverUrl()
    {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl)
    {
        this.coverUrl = coverUrl;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getShareStatus()
    {
        return shareStatus;
    }

    public void setShareStatus(String shareStatus)
    {
        this.shareStatus = shareStatus;
    }

    public Integer getViewCount()
    {
        return viewCount;
    }

    public void setViewCount(Integer viewCount)
    {
        this.viewCount = viewCount;
    }

    public Integer getCollectCount()
    {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount)
    {
        this.collectCount = collectCount;
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
            .append("resourceId", getResourceId())
            .append("resourceName", getResourceName())
            .append("resourceType", getResourceType())
            .append("majorName", getMajorName())
            .append("courseName", getCourseName())
            .append("coverUrl", getCoverUrl())
            .append("fileUrl", getFileUrl())
            .append("shareStatus", getShareStatus())
            .append("viewCount", getViewCount())
            .append("collectCount", getCollectCount())
            .append("introduction", getIntroduction())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
