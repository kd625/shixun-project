package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtTeachingPlan;
import com.ruoyi.system.mapper.virtualmapper.VtTeachingPlanMapper;
import com.ruoyi.system.service.virtualservice.IVtTeachingPlanService;

/**
 * 教学计划Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class VtTeachingPlanServiceImpl implements IVtTeachingPlanService
{
    @Autowired
    private VtTeachingPlanMapper teachingPlanMapper;

    @Override
    public VtTeachingPlan selectVtTeachingPlanById(Long planId)
    {
        return teachingPlanMapper.selectVtTeachingPlanById(planId);
    }

    @Override
    public List<VtTeachingPlan> selectVtTeachingPlanList(VtTeachingPlan teachingPlan)
    {
        return teachingPlanMapper.selectVtTeachingPlanList(teachingPlan);
    }

    @Override
    public int insertVtTeachingPlan(VtTeachingPlan teachingPlan)
    {
        return teachingPlanMapper.insertVtTeachingPlan(teachingPlan);
    }

    @Override
    public int updateVtTeachingPlan(VtTeachingPlan teachingPlan)
    {
        return teachingPlanMapper.updateVtTeachingPlan(teachingPlan);
    }

    @Override
    public int deleteVtTeachingPlanByIds(Long[] planIds)
    {
        return teachingPlanMapper.deleteVtTeachingPlanByIds(planIds);
    }

    @Override
    public int deleteVtTeachingPlanById(Long planId)
    {
        return teachingPlanMapper.deleteVtTeachingPlanById(planId);
    }
}
