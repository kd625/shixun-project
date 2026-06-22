package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtShareApply;
import com.ruoyi.system.mapper.virtualmapper.VtShareApplyMapper;
import com.ruoyi.system.service.virtualservice.IVtShareApplyService;

/**
 * 共享申请Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class VtShareApplyServiceImpl implements IVtShareApplyService
{
    @Autowired
    private VtShareApplyMapper shareApplyMapper;

    @Override
    public VtShareApply selectVtShareApplyById(Long applyId)
    {
        return shareApplyMapper.selectVtShareApplyById(applyId);
    }

    @Override
    public List<VtShareApply> selectVtShareApplyList(VtShareApply shareApply)
    {
        return shareApplyMapper.selectVtShareApplyList(shareApply);
    }

    @Override
    public int insertVtShareApply(VtShareApply shareApply)
    {
        return shareApplyMapper.insertVtShareApply(shareApply);
    }

    @Override
    public int updateVtShareApply(VtShareApply shareApply)
    {
        return shareApplyMapper.updateVtShareApply(shareApply);
    }

    @Override
    public int deleteVtShareApplyByIds(Long[] applyIds)
    {
        return shareApplyMapper.deleteVtShareApplyByIds(applyIds);
    }

    @Override
    public int deleteVtShareApplyById(Long applyId)
    {
        return shareApplyMapper.deleteVtShareApplyById(applyId);
    }
}
