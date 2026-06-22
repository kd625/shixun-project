package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtShareApply;

/**
 * 共享申请Service接口
 * 
 * @author ruoyi
 */
public interface IVtShareApplyService
{
    public VtShareApply selectVtShareApplyById(Long applyId);

    public List<VtShareApply> selectVtShareApplyList(VtShareApply shareApply);

    public int insertVtShareApply(VtShareApply shareApply);

    public int updateVtShareApply(VtShareApply shareApply);

    public int deleteVtShareApplyByIds(Long[] applyIds);

    public int deleteVtShareApplyById(Long applyId);
}
