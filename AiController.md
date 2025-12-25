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
- GET /api/employees/accountName?roleId= → getAccountName()
- PUT /api/employees/{id} → updateEmployee(@PathVariable id)
- DELETE /api/employees/{id} → deleteEmployee(@PathVariable id)
- PUT /api/employees/{id}/restore → restoreEmployee(@PathVariable id)
- GET /api/employees/{id} → getEmployee(@PathVariable id)
- PUT /api/employees/{id}/reset-password → resetPassword(@PathVariable id)
- GET /api/employees/roles → listRoles()

---

## 3) reference 模块

文件：com/ncst/hospitaloutpatient/controller/reference/DictionaryController.java

- GET /api/dictionaries/settlement-categories → listSettlementCategories()
- GET /api/dictionaries/noon-sessions → listNoonSessions()
- GET /api/dictionaries/number-types → listNumberTypes()
- GET /api/dictionaries/payment-methods → listPaymentMethods()
- GET /api/dictionaries/drug-categories → listDrugCategories()
- GET /api/dictionaries/item-types → listItemTypes()
- GET /api/dictionaries/drug-units → listDrugUnits()

文件：com/ncst/hospitaloutpatient/controller/reference/CatalogController.java

- GET /api/catalog/exam-items?page=&pageSize=&keyword= → listExamItems()
- GET /api/catalog/lab-items?page=&pageSize=&keyword= → listLabItems()
- GET /api/catalog/disposal-items?page=&pageSize=&keyword= → listDisposalItems()
- GET /api/catalog/drugs?page=&pageSize=&keyword=&categoryId= → listDrugs()

---

## 4) organization 模块

文件：com/ncst/hospitaloutpatient/controller/organization/DepartmentController.java

- GET /api/departments/types → listDepartmentTypes()
- GET /api/departments?type=OUTPATIENT|EXAM|LAB|PROCEDURE|PHARMACY|REGISTRATION → listDepartments()
- GET /api/departments/all?type=... → listAllDepartments()
- GET /api/departments/{departmentId}/roles → listDepartmentRoles(@PathVariable departmentId)
- POST /api/departments → createDepartment()
- PUT /api/departments/{id} → updateDepartment(@PathVariable id)
- DELETE /api/departments/{id} → deleteDepartment(@PathVariable id)

文件：com/ncst/hospitaloutpatient/controller/organization/DoctorController.java

- GET /api/doctors?departmentId=... → listDoctorsByDepartment()
- GET /api/doctors/all → listDoctors()

文件：com/ncst/hospitaloutpatient/controller/organization/AdminController.java

- GET /api/admin/registration/levels → listRegistrationLevels()
- POST /api/admin/registration/levels → createRegistrationLevel()
- PATCH /api/admin/registration/levels/{code}/status?status=0|1 → updateRegistrationLevelStatus()
- PUT /api/admin/registration/prices → updateRegistrationPrices()

文件：com/ncst/hospitaloutpatient/controller/organization/ScheduleController.java

- GET /api/schedules → listSchedules()
- PUT /api/schedules/quota → setQuota()
- POST /api/schedules/quota/batch → batchSetQuota()
- POST /api/schedules/copy → copySchedule()

---

## 5) patient 模块

文件：com/ncst/hospitaloutpatient/controller/patient/PatientController.java

- GET /api/patients/search?name=&idCard= → searchPatient()
- POST /api/patients → createPatient(@RequestBody PatientRequest request)

---

## 6) outpatient 模块

文件：com/ncst/hospitaloutpatient/controller/outpatient/RegistrationController.java

- POST /api/registrations → createRegistration()
- GET /api/registrations/{id} → getRegistration(@PathVariable id)
- GET /api/registrations/generate-patient-no → generatePatientNo()
- GET /api/registrations?page=&pageSize=&date=&deptId=&doctorId=&status=&keyword= → listRegistrations()
- POST /api/registrations/{id}/cancel → cancelRegistration(@PathVariable id)

---

## 7) billing 模块

文件：com/ncst/hospitaloutpatient/controller/billing/ChargeController.java

- GET /api/charges/items?type=&keyword=&itemType=&drugCategory=&page=&pageSize=&sortBy=&order= → listChargeItems()
- POST /api/charges/settle → settleCharges()

文件：com/ncst/hospitaloutpatient/controller/billing/RefundController.java

- GET /api/refunds/items?type=&keyword=&itemType=&drugCategory=&page=&pageSize=&sortBy=&order= → listRefundableItems()
- POST /api/refunds → refund()

文件：com/ncst/hospitaloutpatient/controller/billing/FeeController.java

- GET /api/fees?name=&status=PAID|REFUND&startTime=&endTime=&page=&pageSize=&sortBy=&order= → queryFees()

---

## 8) casefile 模块

文件：com/ncst/hospitaloutpatient/controller/casefile/CaseController.java

- POST /api/cases → createCase()
- GET /api/cases/registration/{registrationId}/record-id → getRecordIdByRegistrationId()
- GET /api/cases/{caseId} → getCaseDetail()
- PUT /api/cases/{caseId} → updateCase(@PathVariable caseId, @RequestBody CaseRequestDTO request)
- PUT /api/cases/{caseId}/diagnosis → confirmCase(@PathVariable caseId)
- POST /api/cases/{caseId}/applies → submitApplies(@PathVariable caseId)
- GET /api/cases/{caseId}/items → getCaseItemsHistory(@PathVariable caseId)
- POST /api/cases/applies/{applyId}/revoke → revokeMedicalItemApply(@PathVariable applyId)
- GET /api/cases/{caseId}/results → listCaseResults(@PathVariable caseId)
- GET /api/cases/{caseId}/prescriptions → listCasePrescriptions(@PathVariable caseId)
- POST /api/cases/{caseId}/prescriptions → createPrescriptions(@PathVariable caseId)
- POST /api/cases/prescriptions/{prescriptionId}/revoke → revokePrescription(@PathVariable prescriptionId)
- GET /api/cases/{caseId}/fees → listCaseFees(@PathVariable caseId)
- GET /api/cases/registrations/patients?page=&pageSize=&keyword=&status= → listDoctorRegisteredPatients()
- GET /api/cases/registrations/patients/status-count → countPatientsByFrontendStatus()
- GET /api/cases/registrations/patients/{medicalNo}/detail → getPatientDetailByMedicalNo()
- GET /api/cases/registrations/{registrationId}/context → getClinicContext()
- POST /api/cases/registrations/{registrationId}/finish → finishVisit()

---

## 9) medicalitem 模块

文件：com/ncst/hospitaloutpatient/controller/medicalitem/MedicalItemController.java

- GET /api/items?keyword=&status=&departmentId=&page=&pageSize= → listItems()
- POST /api/items → createItem()
- PUT /api/items/{itemId} → updateItem(@PathVariable itemId)
- PATCH /api/items/{itemId}/toggle → toggleItemStatus(@PathVariable itemId)

文件：com/ncst/hospitaloutpatient/controller/medicalitem/ExamController.java

- GET /api/exam/applies?keyword=&page=&pageSize=&sortBy=&order= → listExamApplies()
- POST /api/exam/applies/{applyId}/execute → executeExam(@PathVariable applyId)
- POST /api/exam/applies/{applyId}/result → submitExamResult(@PathVariable applyId)
- GET /api/exam/departments → listExamDepartments()
- GET /api/exam/staffs/{departmentId} → listExamStaffs()
- GET /api/exam/records?keyword=&operateType=&page=&pageSize=&sortBy=&order= → listExamRecords()

文件：com/ncst/hospitaloutpatient/controller/medicalitem/LabController.java

- GET /api/lab/applies?keyword=&page=&pageSize=&sortBy=&order= → listLabApplies()
- POST /api/lab/applies/{applyId}/execute → executeLab(@PathVariable applyId)
- POST /api/lab/applies/{applyId}/result → submitLabResult(@PathVariable applyId)
- GET /api/lab/departments → listLabDepartments()
- GET /api/lab/staffs/{departmentId} → listLabStaffs()
- GET /api/lab/records?keyword=&operateType=&page=&pageSize=&sortBy=&order= → listLabRecords()

文件：com/ncst/hospitaloutpatient/controller/medicalitem/DisposalController.java

- GET /api/disposal/applies?keyword=&page=&pageSize=&sortBy=&order= → listDisposalApplies()
- POST /api/disposal/applies/{applyId}/execute → executeDisposal(@PathVariable applyId)
- POST /api/disposal/applies/{applyId}/result → submitDisposalResult(@PathVariable applyId)
- GET /api/disposal/departments → listDisposalDepartments()
- GET /api/disposal/staffs/{departmentId} → listDisposalStaffs()
- GET /api/disposal/records?keyword=&operateType=&page=&pageSize=&sortBy=&order= → listDisposalRecords()

---

## 10) pharmacy 模块

文件：com/ncst/hospitaloutpatient/controller/pharmacy/PharmacyController.java

- GET /api/pharmacy/dispense/pending?keyword=&sortBy=&order=&page=&pageSize= → listDispensePending()
- POST /api/pharmacy/dispense → dispenseDrugs()
- GET /api/pharmacy/records?keyword=&type=&sortBy=&order=&page=&pageSize= → listPharmacyRecords()

---

## 11) inventory 模块

文件：com/ncst/hospitaloutpatient/controller/inventory/DrugController.java

- POST /api/drugs → createDrug()
- PUT /api/drugs/{id} → updateDrug(@PathVariable id)
- PATCH /api/drugs/{id}/toggle → toggleDrugStatus(@PathVariable id)

---

## 12) analytics 模块

文件：com/ncst/hospitaloutpatient/controller/analytics/StatsController.java

- GET /api/stats/registrations/trend?period=month|season|year|all|auto&startDate=&endDate= → registrationsTrend()
- GET /api/stats/registrations/type-breakdown?period=...&startDate=&endDate= → registrationsTypeBreakdown()
- GET /api/stats/registrations/by-department?period=...&startDate=&endDate= → registrationsByDepartment()
- GET /api/stats/registrations/by-doctor?period=...&startDate=&endDate= → registrationsByDoctor()
- GET /api/stats/revenue/trend?period=...&startDate=&endDate= → revenueTrend()
- GET /api/stats/revenue/by-type?period=...&startDate=&endDate= → revenueByType()
- GET /api/stats/revenue/by-department?period=...&startDate=&endDate= → revenueByDepartment()
- GET /api/stats/refund/trend?period=...&startDate=&endDate= → refundTrend()

---

提示

- 如需在文件中快速导航，可为每个接口方法添加区域标记注释：
  // region [GET] /api/xxx
  // endregion
- 也可统一在方法上添加 @Operation(summary = "...", description = "...") 以便通过 Swagger/OpenAPI 生成可点击定位的文档。

- 200 OK
- 400xx 参数/校验错误 40001 参数错误 40002 参数缺失 40003 参数格式错误 40004 参数值超出允许范围
- 401xx 认证失败/过期 40101 未登录 40102 登录过期 40103 账号或密码错误
- 403xx 无权限       40301 访问被拒绝
- 404xx 资源不存在    40401 表中无此记录
- 409xx 并发/状态冲突（如重复结算、已执行不可退）
- 422xx 业务校验失败（医保/库存不足等）
- 500xx 系统内部异常
