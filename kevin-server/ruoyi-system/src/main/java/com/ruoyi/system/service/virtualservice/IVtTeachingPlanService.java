package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtTeachingPlan;

/**
 * 教学计划Service接口
 * 
 * @author ruoyi
 */
public interface IVtTeachingPlanService
{
    public VtTeachingPlan selectVtTeachingPlanById(Long planId);

    public List<VtTeachingPlan> selectVtTeachingPlanList(VtTeachingPlan teachingPlan);

    public int insertVtTeachingPlan(VtTeachingPlan teachingPlan);

    public int updateVtTeachingPlan(VtTeachingPlan teachingPlan);

    public int deleteVtTeachingPlanByIds(Long[] planIds);

    public int deleteVtTeachingPlanById(Long planId);
}
