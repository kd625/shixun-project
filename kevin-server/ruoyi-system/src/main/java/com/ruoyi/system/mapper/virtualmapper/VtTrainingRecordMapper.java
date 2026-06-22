package com.ruoyi.system.mapper.virtualmapper;

import java.util.List;
import com.ruoyi.system.domain.virtualdomain.VtTrainingRecord;

/**
 * 过程结果Mapper接口
 * 
 * @author ruoyi
 */
public interface VtTrainingRecordMapper
{
    public VtTrainingRecord selectVtTrainingRecordById(Long recordId);

    public List<VtTrainingRecord> selectVtTrainingRecordList(VtTrainingRecord trainingRecord);

    public int insertVtTrainingRecord(VtTrainingRecord trainingRecord);

    public int updateVtTrainingRecord(VtTrainingRecord trainingRecord);

    public int deleteVtTrainingRecordById(Long recordId);

    public int deleteVtTrainingRecordByIds(Long[] recordIds);
}
