package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtLab;

/**
 * 实验室Service接口
 * 
 * @author ruoyi
 */
public interface IVtLabService
{
    public VtLab selectVtLabById(Long labId);

    public List<VtLab> selectVtLabList(VtLab lab);

    public int insertVtLab(VtLab lab);

    public int updateVtLab(VtLab lab);

    public int deleteVtLabByIds(Long[] labIds);

    public int deleteVtLabById(Long labId);
}
