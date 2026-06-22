package com.ruoyi.system.service.virtualservice;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtTrainingRecord;

/**
 * 过程结果Service接口
 * 
 * @author ruoyi
 */
public interface IVtTrainingRecordService
{
    public VtTrainingRecord selectVtTrainingRecordById(Long recordId);

    public List<VtTrainingRecord> selectVtTrainingRecordList(VtTrainingRecord trainingRecord);

    public int insertVtTrainingRecord(VtTrainingRecord trainingRecord);

    public int updateVtTrainingRecord(VtTrainingRecord trainingRecord);

    public int deleteVtTrainingRecordByIds(Long[] recordIds);

    public int deleteVtTrainingRecordById(Long recordId);
}
