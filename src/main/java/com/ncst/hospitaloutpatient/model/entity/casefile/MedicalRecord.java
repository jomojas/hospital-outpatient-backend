package com.ncst.hospitaloutpatient.model.entity.casefile;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalRecord {
    private Integer recordId;           // 主键
    private String patientNo;           // 患者编号
    private Integer registrationId;     // 挂号ID
    private String chiefComplaint;      // 主诉
    private String presentHistory;      // 现病史
    private String physicalExam;        // 体格检查
    private String diagnosis;           // 诊断
    private String treatmentPlan;       // 治疗方案
    private Integer doctorId;           // 医生ID
    private LocalDateTime recordTime;   // 记录时间
}