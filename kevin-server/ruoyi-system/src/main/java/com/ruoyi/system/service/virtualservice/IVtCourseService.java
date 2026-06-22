package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtCourse;

/**
 * 课程Service接口
 * 
 * @author ruoyi
 */
public interface IVtCourseService
{
    public VtCourse selectVtCourseById(Long courseId);

    public List<VtCourse> selectVtCourseList(VtCourse course);

    public int insertVtCourse(VtCourse course);

    public int updateVtCourse(VtCourse course);

    public int deleteVtCourseByIds(Long[] courseIds);

    public int deleteVtCourseById(Long courseId);
}
