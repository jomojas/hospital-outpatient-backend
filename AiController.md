# Controller 与接口位置映射（Base Path: /api）

说明

- 本文列出每个具体 Controller 文件，以及该文件中应包含的接口方法（HTTP 方法 + 路径）与建议的方法名，便于在代码中快速定位“接口所在位置”。
- 基础包：com.ncst.hospitaloutpatient.controller.<module>.<Controller>.java
- 统一前缀：/api
- 命名规范：动词在前、资源在后，列表 list*，新增 create*，更新 update*，查询 get*/find\*，操作动词采用业务语义。

---

## 1) auth 模块

文件：com/ncst/hospitaloutpatient/controller/auth/AuthController.java

- POST /api/auth/login → login()
- POST /api/auth/logout → logout()
- POST /api/auth/password/change → changePassword()

---

## 2) hr 模块

文件：com/ncst/hospitaloutpatient/controller/hr/EmployeeController.java

- GET /api/employees → listEmployees()
- POST /api/employees → createEmployee()
- PUT /api/employees/{id} → updateEmployee(@PathVariable id)
- GET /api/employees/{id} → getEmployee(@PathVariable id)

---

## 3) reference 模块

文件：com/ncst/hospitaloutpatient/controller/reference/DictionaryController.java

- GET /api/dictionaries/settlement-categories → listSettlementCategories()
- GET /api/dictionaries/noon-sessions → listNoonSessions()
- GET /api/dictionaries/registration-types → listRegistrationTypes()
- GET /api/dictionaries/payment-methods → listPaymentMethods()

文件：com/ncst/hospitaloutpatient/controller/reference/CatalogController.java

- GET /api/catalog/exam-items?page=&size=&keyword= → listExamItems()
- GET /api/catalog/lab-items?page=&size=&keyword= → listLabItems()
- GET /api/catalog/procedure-items?page=&size=&keyword= → listProcedureItems()
- GET /api/catalog/drugs?page=&size=&keyword= → listDrugCatalog()

---

## 4) organization 模块

文件：com/ncst/hospitaloutpatient/controller/organization/DepartmentController.java

- GET /api/departments?type=OUTPATIENT|EXAM|LAB|PROCEDURE|PHARMACY|REGISTRATION → listDepartments()

文件：com/ncst/hospitaloutpatient/controller/organization/DoctorController.java

- GET /api/doctors?departmentId=... → listDoctorsByDepartment()

文件：com/ncst/hospitaloutpatient/controller/organization/AdminController.java

- GET /api/admin/employees?page=&size=&keyword= → adminListEmployees()
- POST /api/admin/employees → adminCreateEmployee()
- PUT /api/admin/employees/{id} → adminUpdateEmployee(@PathVariable id)
- PUT /api/admin/doctors/{doctorId}/quota → updateDoctorQuota(@PathVariable doctorId)
- PUT /api/admin/registration/prices → updateRegistrationPrices()

---

## 5) patient 模块

文件：com/ncst/hospitaloutpatient/controller/patient/PatientController.java

- GET /api/patients/search?name=&idCard= → searchPatient()

---

## 6) outpatient 模块

文件：com/ncst/hospitaloutpatient/controller/outpatient/RegistrationController.java

- POST /api/registrations → createRegistration()
- GET /api/registrations/{id} → getRegistration(@PathVariable id)
- GET /api/registrations?page=&size=&date=&deptId=&doctorId=&status=&keyword= → listRegistrations()
- POST /api/registrations/{id}/cancel → cancelRegistration(@PathVariable id)

---

## 7) billing 模块

文件：com/ncst/hospitaloutpatient/controller/billing/ChargeController.java

- GET /api/charges/items?name=&medicalNo=&page=&size= → listChargeItems()
- POST /api/charges/settle → settleCharges()

文件：com/ncst/hospitaloutpatient/controller/billing/RefundController.java

- GET /api/refunds/items?name=&medicalNo=&page=&size= → listRefundableItems()
- POST /api/refunds → refund()

文件：com/ncst/hospitaloutpatient/controller/billing/FeeController.java

- GET /api/fees?name=&medicalNo=&status=PAID|UNPAID&startDate=&endDate=&page=&size= → queryFees()

---

## 8) casefile 模块

文件：com/ncst/hospitaloutpatient/controller/casefile/CaseController.java

- POST /api/cases → createCase()
- POST /api/cases/{caseId}/applies → submitApplies(@PathVariable caseId)
- GET /api/cases/{caseId}/results → listCaseResults(@PathVariable caseId)
- POST /api/cases/{caseId}/prescriptions → createPrescriptions(@PathVariable caseId)
- GET /api/cases/{caseId}/fees → listCaseFees(@PathVariable caseId)

---

## 9) medicalitem 模块

文件：com/ncst/hospitaloutpatient/controller/medicalitem/ExamController.java

- GET /api/exam/applies?status=UNFINISHED&page=&size= → listExamApplies()
- POST /api/exam/applies/{applyId}/cancel → cancelExamApply(@PathVariable applyId)
- POST /api/exam/applies/{applyId}/execute → executeExam(@PathVariable applyId)
- POST /api/exam/applies/{applyId}/result → submitExamResult(@PathVariable applyId)
- GET /api/exam/departments → listExamDepartments()
- GET /api/exam/staffs → listExamStaffs()
- GET /api/exam/records?keyword=&page=&size= → listMyExamRecords()
- GET /api/exam/patients/{patientId}/records → listMyExamRecordsByPatient(@PathVariable patientId)

文件：com/ncst/hospitaloutpatient/controller/medicalitem/LabController.java

- GET /api/lab/applies?status=UNFINISHED&page=&size= → listLabApplies()
- POST /api/lab/applies/{applyId}/cancel → cancelLabApply(@PathVariable applyId)
- POST /api/lab/applies/{applyId}/execute → executeLab(@PathVariable applyId)
- POST /api/lab/applies/{applyId}/result → submitLabResult(@PathVariable applyId)
- GET /api/lab/departments → listLabDepartments()
- GET /api/lab/staffs → listLabStaffs()
- GET /api/lab/records?keyword=&page=&size= → listMyLabRecords()
- GET /api/lab/patients/{patientId}/records → listMyLabRecordsByPatient(@PathVariable patientId)

文件：com/ncst/hospitaloutpatient/controller/medicalitem/ProcedureController.java

- GET /api/procedure/applies?status=UNFINISHED&page=&size= → listProcedureApplies()
- POST /api/procedure/applies/{applyId}/cancel → cancelProcedureApply(@PathVariable applyId)
- POST /api/procedure/applies/{applyId}/execute → executeProcedure(@PathVariable applyId)
- POST /api/procedure/applies/{applyId}/result → submitProcedureResult(@PathVariable applyId)
- GET /api/procedure/departments → listProcedureDepartments()
- GET /api/procedure/staffs → listProcedureStaffs()
- GET /api/procedure/records?keyword=&page=&size= → listMyProcedureRecords()
- GET /api/procedure/patients/{patientId}/records → listMyProcedureRecordsByPatient(@PathVariable patientId)

---

## 10) pharmacy 模块

文件：com/ncst/hospitaloutpatient/controller/pharmacy/PharmacyController.java

- GET /api/pharmacy/dispense/pending?keyword=&page=&size= → listDispensePending()
- GET /api/pharmacy/prescriptions?patientMedicalNo=&status=UNFINISHED|FINISHED → listPrescriptionsByPatient()
- POST /api/pharmacy/dispense → dispenseDrugs()
- POST /api/pharmacy/return → returnDrugs()
- GET /api/pharmacy/records?keyword=&page=&size= → listMyPharmacyRecords()

---

## 11) inventory 模块

文件：com/ncst/hospitaloutpatient/controller/inventory/DrugController.java

- GET /api/drugs?page=&size=&keyword= → listDrugs()
- POST /api/drugs → createDrug()
- PUT /api/drugs/{id} → updateDrug(@PathVariable id)

---

## 12) analytics 模块

文件：com/ncst/hospitaloutpatient/controller/analytics/StatsController.java

- GET /api/stats/registrations/trend?startDate=&endDate=&granularity=auto → registrationsTrend()
- GET /api/stats/registrations/type-breakdown?startDate=&endDate= → registrationsTypeBreakdown()
- GET /api/stats/registrations/by-department?startDate=&endDate=&topN= → registrationsByDepartment()
- GET /api/stats/registrations/by-doctor?startDate=&endDate=&topN= → registrationsByDoctor()
- GET /api/stats/revenue/trend?startDate=&endDate=&granularity=auto&metric=net|gross → revenueTrend()
- GET /api/stats/revenue/by-type?startDate=&endDate= → revenueByType()
- GET /api/stats/revenue/by-payment-method?startDate=&endDate= → revenueByPaymentMethod()
- GET /api/stats/revenue/by-department?startDate=&endDate=&topN= → revenueByDepartment()
- GET /api/stats/revenue/by-doctor?startDate=&endDate=&topN= → revenueByDoctor()
- GET /api/stats/refund/trend?startDate=&endDate=&granularity=auto → refundTrend()

---

提示

- 如需在文件中快速导航，可为每个接口方法添加区域标记注释：
  // region [GET] /api/xxx
  // endregion
- 也可统一在方法上添加 @Operation(summary = "...", description = "...") 以便通过 Swagger/OpenAPI 生成可点击定位的文档。
