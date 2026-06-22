package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtExperiment;

/**
 * 实训实验Mapper接口
 * 
 * @author ruoyi
 */
public interface VtExperimentMapper
{
    public VtExperiment selectVtExperimentById(Long experimentId);

    public List<VtExperiment> selectVtExperimentList(VtExperiment experiment);

    public int insertVtExperiment(VtExperiment experiment);

    public int updateVtExperiment(VtExperiment experiment);

    public int deleteVtExperimentById(Long experimentId);

    public int deleteVtExperimentByIds(Long[] experimentIds);
}
