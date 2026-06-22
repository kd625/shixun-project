package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtCourse;

/**
 * 课程Mapper接口
 * 
 * @author ruoyi
 */
public interface VtCourseMapper
{
    public VtCourse selectVtCourseById(Long courseId);

    public List<VtCourse> selectVtCourseList(VtCourse course);

    public int insertVtCourse(VtCourse course);

    public int updateVtCourse(VtCourse course);

    public int deleteVtCourseById(Long courseId);

    public int deleteVtCourseByIds(Long[] courseIds);
}
