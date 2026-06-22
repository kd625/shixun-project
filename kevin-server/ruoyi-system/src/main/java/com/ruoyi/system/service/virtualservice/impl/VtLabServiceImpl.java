package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtLab;
import com.ruoyi.system.mapper.virtualmapper.VtLabMapper;
import com.ruoyi.system.service.virtualservice.IVtLabService;

/**
 * 实验室Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class VtLabServiceImpl implements IVtLabService
{
    @Autowired
    private VtLabMapper labMapper;

    @Override
    public VtLab selectVtLabById(Long labId)
    {
        return labMapper.selectVtLabById(labId);
    }

    @Override
    public List<VtLab> selectVtLabList(VtLab lab)
    {
        return labMapper.selectVtLabList(lab);
    }

    @Override
    public int insertVtLab(VtLab lab)
    {
        return labMapper.insertVtLab(lab);
    }

    @Override
    public int updateVtLab(VtLab lab)
    {
        return labMapper.updateVtLab(lab);
    }

    @Override
    public int deleteVtLabByIds(Long[] labIds)
    {
        return labMapper.deleteVtLabByIds(labIds);
    }

    @Override
    public int deleteVtLabById(Long labId)
    {
        return labMapper.deleteVtLabById(labId);
    }
}
