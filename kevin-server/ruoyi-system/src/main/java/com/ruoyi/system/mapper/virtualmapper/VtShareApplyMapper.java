package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtShareApply;

/**
 * 共享申请Mapper接口
 * 
 * @author ruoyi
 */
public interface VtShareApplyMapper
{
    public VtShareApply selectVtShareApplyById(Long applyId);

    public List<VtShareApply> selectVtShareApplyList(VtShareApply shareApply);

    public int insertVtShareApply(VtShareApply shareApply);

    public int updateVtShareApply(VtShareApply shareApply);

    public int deleteVtShareApplyById(Long applyId);

    public int deleteVtShareApplyByIds(Long[] applyIds);
}
