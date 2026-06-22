package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtTeachingPlan;

/**
 * 教学计划Mapper接口
 * 
 * @author ruoyi
 */
public interface VtTeachingPlanMapper
{
    public VtTeachingPlan selectVtTeachingPlanById(Long planId);

    public List<VtTeachingPlan> selectVtTeachingPlanList(VtTeachingPlan teachingPlan);

    public int insertVtTeachingPlan(VtTeachingPlan teachingPlan);

    public int updateVtTeachingPlan(VtTeachingPlan teachingPlan);

    public int deleteVtTeachingPlanById(Long planId);

    public int deleteVtTeachingPlanByIds(Long[] planIds);
}
