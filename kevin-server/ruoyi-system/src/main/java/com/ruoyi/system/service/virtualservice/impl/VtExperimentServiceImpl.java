package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtExperiment;
import com.ruoyi.system.mapper.virtualmapper.VtExperimentMapper;
import com.ruoyi.system.service.virtualservice.IVtExperimentService;

/**
 * 实训实验Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class VtExperimentServiceImpl implements IVtExperimentService
{
    @Autowired
    private VtExperimentMapper experimentMapper;

    @Override
    public VtExperiment selectVtExperimentById(Long experimentId)
    {
        return experimentMapper.selectVtExperimentById(experimentId);
    }

    @Override
    public List<VtExperiment> selectVtExperimentList(VtExperiment experiment)
    {
        return experimentMapper.selectVtExperimentList(experiment);
    }

    @Override
    public int insertVtExperiment(VtExperiment experiment)
    {
        return experimentMapper.insertVtExperiment(experiment);
    }

    @Override
    public int updateVtExperiment(VtExperiment experiment)
    {
        return experimentMapper.updateVtExperiment(experiment);
    }

    @Override
    public int deleteVtExperimentByIds(Long[] experimentIds)
    {
        return experimentMapper.deleteVtExperimentByIds(experimentIds);
    }

    @Override
    public int deleteVtExperimentById(Long experimentId)
    {
        return experimentMapper.deleteVtExperimentById(experimentId);
    }
}
