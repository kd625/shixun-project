package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtLab;

/**
 * 实验室Mapper接口
 * 
 * @author ruoyi
 */
public interface VtLabMapper
{
    public VtLab selectVtLabById(Long labId);

    public List<VtLab> selectVtLabList(VtLab lab);

    public int insertVtLab(VtLab lab);

    public int updateVtLab(VtLab lab);

    public int deleteVtLabById(Long labId);

    public int deleteVtLabByIds(Long[] labIds);
}
