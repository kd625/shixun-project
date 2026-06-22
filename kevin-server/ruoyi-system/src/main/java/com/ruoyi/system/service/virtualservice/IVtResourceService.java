package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtResource;

/**
 * 实训资源Service接口
 * 
 * @author ruoyi
 */
public interface IVtResourceService
{
    public VtResource selectVtResourceById(Long resourceId);

    public List<VtResource> selectVtResourceList(VtResource resource);

    public int insertVtResource(VtResource resource);

    public int updateVtResource(VtResource resource);

    public int deleteVtResourceByIds(Long[] resourceIds);

    public int deleteVtResourceById(Long resourceId);
}
