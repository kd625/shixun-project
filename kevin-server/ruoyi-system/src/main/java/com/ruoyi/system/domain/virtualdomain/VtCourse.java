package com.ruoyi.system.domain.virtualdomain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 课程对象 vt_course
 * 
 * @author ruoyi
 */
public class VtCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 专业方向 */
    @Excel(name = "专业方向")
    private String majorDirection;

    /** 课时 */
    @Excel(name = "课时")
    private Integer classHours;

    /** 授课教师 */
    @Excel(name = "授课教师")
    private String teacherName;

    /** 课程状态 */
    @Excel(name = "课程状态", readConverterExp = "0=启用,1=停用")
    private String courseStatus;

    /** 课程简介 */
    @Excel(name = "课程简介")
    private String introduction;

    public Long getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getMajorDirection()
    {
        return majorDirection;
    }

    public void setMajorDirection(String majorDirection)
    {
        this.majorDirection = majorDirection;
    }

    public Integer getClassHours()
    {
        return classHours;
    }

    public void setClassHours(Integer classHours)
    {
        this.classHours = classHours;
    }

    public String getTeacherName()
    {
        return teacherName;
    }

    public void setTeacherName(String teacherName)
    {
        this.teacherName = teacherName;
    }

    public String getCourseStatus()
    {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus)
    {
        this.courseStatus = courseStatus;
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
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("majorDirection", getMajorDirection())
            .append("classHours", getClassHours())
            .append("teacherName", getTeacherName())
            .append("courseStatus", getCourseStatus())
            .append("introduction", getIntroduction())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
