package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtResource;
import com.ruoyi.system.mapper.virtualmapper.VtResourceMapper;
import com.ruoyi.system.service.virtualservice.IVtResourceService;

/**
 * 实训资源Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class VtResourceServiceImpl implements IVtResourceService
{
    @Autowired
    private VtResourceMapper resourceMapper;

    @Override
    public VtResource selectVtResourceById(Long resourceId)
    {
        return resourceMapper.selectVtResourceById(resourceId);
    }

    @Override
    public List<VtResource> selectVtResourceList(VtResource resource)
    {
        return resourceMapper.selectVtResourceList(resource);
    }

    @Override
    public int insertVtResource(VtResource resource)
    {
        return resourceMapper.insertVtResource(resource);
    }

    @Override
    public int updateVtResource(VtResource resource)
    {
        return resourceMapper.updateVtResource(resource);
    }

    @Override
    public int deleteVtResourceByIds(Long[] resourceIds)
    {
        return resourceMapper.deleteVtResourceByIds(resourceIds);
    }

    @Override
    public int deleteVtResourceById(Long resourceId)
    {
        return resourceMapper.deleteVtResourceById(resourceId);
    }
}
