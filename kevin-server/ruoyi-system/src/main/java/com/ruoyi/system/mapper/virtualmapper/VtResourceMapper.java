package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtResource;

/**
 * 实训资源Mapper接口
 * 
 * @author ruoyi
 */
public interface VtResourceMapper
{
    public VtResource selectVtResourceById(Long resourceId);

    public List<VtResource> selectVtResourceList(VtResource resource);

    public int insertVtResource(VtResource resource);

    public int updateVtResource(VtResource resource);

    public int deleteVtResourceById(Long resourceId);

    public int deleteVtResourceByIds(Long[] resourceIds);
}
