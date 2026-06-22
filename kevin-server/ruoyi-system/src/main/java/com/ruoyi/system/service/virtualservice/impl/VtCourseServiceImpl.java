package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtCourse;
import com.ruoyi.system.mapper.virtualmapper.VtCourseMapper;
import com.ruoyi.system.service.virtualservice.IVtCourseService;

/**
 * 课程Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class VtCourseServiceImpl implements IVtCourseService
{
    @Autowired
    private VtCourseMapper courseMapper;

    @Override
    public VtCourse selectVtCourseById(Long courseId)
    {
        return courseMapper.selectVtCourseById(courseId);
    }

    @Override
    public List<VtCourse> selectVtCourseList(VtCourse course)
    {
        return courseMapper.selectVtCourseList(course);
    }

    @Override
    public int insertVtCourse(VtCourse course)
    {
        return courseMapper.insertVtCourse(course);
    }

    @Override
    public int updateVtCourse(VtCourse course)
    {
        return courseMapper.updateVtCourse(course);
    }

    @Override
    public int deleteVtCourseByIds(Long[] courseIds)
    {
        return courseMapper.deleteVtCourseByIds(courseIds);
    }

    @Override
    public int deleteVtCourseById(Long courseId)
    {
        return courseMapper.deleteVtCourseById(courseId);
    }
}
