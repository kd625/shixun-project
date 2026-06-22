package com.ruoyi.system.service.virtualservice.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.virtualdomain.VtTrainingRecord;
import com.ruoyi.system.mapper.virtualmapper.VtTrainingRecordMapper;
import com.ruoyi.system.service.virtualservice.IVtTrainingRecordService;

/**
 * 过程结果Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class VtTrainingRecordServiceImpl implements IVtTrainingRecordService
{
    @Autowired
    private VtTrainingRecordMapper trainingRecordMapper;

    @Override
    public VtTrainingRecord selectVtTrainingRecordById(Long recordId)
    {
        return trainingRecordMapper.selectVtTrainingRecordById(recordId);
    }

    @Override
    public List<VtTrainingRecord> selectVtTrainingRecordList(VtTrainingRecord trainingRecord)
    {
        return trainingRecordMapper.selectVtTrainingRecordList(trainingRecord);
    }

    @Override
    public int insertVtTrainingRecord(VtTrainingRecord trainingRecord)
    {
        return trainingRecordMapper.insertVtTrainingRecord(trainingRecord);
    }

    @Override
    public int updateVtTrainingRecord(VtTrainingRecord trainingRecord)
    {
        return trainingRecordMapper.updateVtTrainingRecord(trainingRecord);
    }

    @Override
    public int deleteVtTrainingRecordByIds(Long[] recordIds)
    {
        return trainingRecordMapper.deleteVtTrainingRecordByIds(recordIds);
    }

    @Override
    public int deleteVtTrainingRecordById(Long recordId)
    {
        return trainingRecordMapper.deleteVtTrainingRecordById(recordId);
    }
}
