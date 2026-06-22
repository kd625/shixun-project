package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtExperiment;

/**
 * 实训实验Service接口
 * 
 * @author ruoyi
 */
public interface IVtExperimentService
{
    public VtExperiment selectVtExperimentById(Long experimentId);

    public List<VtExperiment> selectVtExperimentList(VtExperiment experiment);

    public int insertVtExperiment(VtExperiment experiment);

    public int updateVtExperiment(VtExperiment experiment);

    public int deleteVtExperimentByIds(Long[] experimentIds);

    public int deleteVtExperimentById(Long experimentId);
}
