# API 设计

- Base Path: /api/
- 认证：JWT（Authorization: Bearer <token>）
- 统一响应：{ code, message, data, meta }
- 分页参数：page(default=1), size(default=20)
- 时间筛选：startDate, endDate（yyyy-MM-dd HH:mm:ss，统计接口可只到日）
- 统计粒度：granularity=auto|day|week|month|year（auto 规则：≤90 天=day；≤2 年=month；>2 年=year）

# 约定

- 关键状态枚举（示例）：
  - MedicalItemApply.status: PENDING_PAYMENT -> UNFINISHED -> FINISHED -> RETURNED -> CANCELLED

---

## 认证与员工

- POST /auth/login  
  功能：校验用户名与密码，签发访问令牌，记录本次登录时间，返回员工基本信息与令牌。

- POST /auth/logout  
  功能：注销当前会话（令牌失效/加入黑名单），记录登出审计。

- POST /auth/password/change  
  功能：已登录用户修改密码，校验旧密码并更新新密码哈希，记录操作审计。

- GET /employees  
  功能：分页查询员工列表，支持关键字（姓名/编号/职位）筛选，返回基础档案信息。

- POST /employees  
  功能：新增员工档案，设置职位、所属科室、初始角色/权限等。

- PUT /employees/{id}  
  功能：更新员工信息（姓名、职位、科室、状态、角色等），记录变更审计。

- GET /employees/{id}  
  功能：按 ID 获取员工详情（基本信息、所属科室、角色等）。

---

## 字典/主数据

- GET /dictionaries/settlement-categories  
  功能：获取结算类别字典（自费、医保、商保等）用于前端选择。

- GET /dictionaries/noon-sessions  
  功能：获取午别字典（上午/下午/晚间等）用于挂号与排班。

- GET /dictionaries/registration-types  
  功能：获取号别字典（普通号/专家号等）及其配置。

- GET /dictionaries/payment-methods  
  功能：获取收费支付方式字典（现金、银行卡、微信、支付宝、医保等）。

- GET /departments?type=OUTPATIENT|EXAM|LAB|PROCEDURE|PHARMACY|REGISTRATION  
  功能：按科室类型查询科室列表，用于挂号、医技执行、药房等模块选择。

- GET /doctors?departmentId=...  
  功能：按科室查询医生列表（可扩展支持按出诊状态/号源过滤）。

- GET /catalog/exam-items?page=&size=&keyword=  
  功能：分页检索检查项目目录（名称、价格、编码、单位等）。

- GET /catalog/lab-items?page=&size=&keyword=  
  功能：分页检索检验项目目录。

- GET /catalog/procedure-items?page=&size=&keyword=  
  功能：分页检索处置项目目录。

- GET /catalog/drugs?page=&size=&keyword=  
  功能：分页检索药品目录（名称、规格、单位、价格、库存摘要等）。

---

## 挂号收费员 - 挂号

- GET /patients/search?name=&idCard=  
  功能：按姓名与身份证查询患者主档，有则返回档案信息，无则返回空结果。

- POST /registrations  
  功能：创建挂号单。若患者存在则更新档案，不存在则新增；生成挂号记录、主流程记录与交易流水（挂号费），返回挂号信息。

- GET /registrations/{id}  
  功能：获取单条挂号详情（患者信息、科室医生、号别、费用、状态流转等）。

- GET /registrations?page=&size=&date=&deptId=&doctorId=&status=&keyword=  
  功能：分页查询挂号记录，支持按日期、科室、医生、状态与关键字（姓名/病历号）筛选。

- POST /registrations/{id}/cancel  
  功能：退号。校验状态后更新主流程为取消/退号，生成退号交易流水并回退相关计数/号源。

---

## 挂号收费员 - 收费

- GET /charges/items?name=&medicalNo=&page=&size=  
  功能：查询患者待支付的项目与处方明细（状态为 PENDING_PAYMENT），返回待收费用清单与合计。

- POST /charges/settle  
  功能：对选中未支付项目/处方执行收费结算，生成收费交易流水，更新明细状态为 UNFINISHED（待执行），返回结算结果与票据信息（如有）。

---

## 挂号收费员 - 退费

- GET /refunds/items?name=&medicalNo=&page=&size=  
  功能：查询已收费且未退费的项目/处方明细（如 UNFINISHED/可退口径），用于选择退费对象。

- POST /refunds  
  功能：对选中明细执行退费（支持部分/多项），生成退费交易流水，并将对应明细状态更新为 CANCELLED（或按规则置 RETURNED/取消执行）。

---

## 患者费用信息（综合查询）

- GET /fees?name=&medicalNo=&status=PAID|UNPAID&startDate=&endDate=&page=&size=  
  功能：按姓名/病历号/费用状态与时间范围综合检索患者各类费用（检查、检验、处置、药品等），返回明细与汇总。

---

## 门诊医生 - 候诊与看诊记录

- GET /doctor/waiting-patients?keyword=&page=&size=  
  功能：获取当前医生名下待初诊队列与已初诊数量统计，并列出待初诊患者（默认按挂号时间降序），支持姓名/病历号关键词过滤。

- GET /doctor/visit-records?status=&keyword=&page=&size=  
  功能：查询当前医生历史/在诊患者记录，按当前就诊状态与关键词筛选，默认按病案时间降序，用于复诊管理。

---

## 门诊医生 - 病案与申请/处方

- POST /cases  
  功能：创建病案首页，保存主诉、现病史、既往史等诊疗基础信息，进入诊疗流程。

- POST /cases/{caseId}/applies  
  功能：为病案提交检查/检验/处置申请（可多项批量），生成 medical_item_apply 记录，进入待执行队列。

- GET /cases/{caseId}/results  
  功能：查询该病案已完成的检查/检验/处置结果汇总，便于临床决策。

- POST /cases/{caseId}/prescriptions  
  功能：为病案开立处方（多药品明细），生成处方记录，进入收费及发药流程。

- GET /cases/{caseId}/fees  
  功能：查看病案关联费用（项目与处方）的支付状态与金额，支持就地对照。

---

## 检查员（EXAM）

- GET /exam/applies?status=UNFINISHED&page=&size=  
  功能：查看待执行的检查申请列表，支持分页。

- POST /exam/applies/{applyId}/cancel  
  功能：取消指定检查申请，将申请状态更新为 RETURNED，并记录操作日志。

- POST /exam/applies/{applyId}/execute  
  功能：开始执行检查，生成一条 operation_log（EXECUTE），如主流程非“检查中”则置为“检查中”。

- POST /exam/applies/{applyId}/result  
  功能：录入检查结果，生成 operation_log（INPUT_RESULT），保存结果并将申请状态置为 FINISHED。

- GET /exam/departments  
  功能：获取检查科室列表（用于执行与结果录入时选择）。

- GET /exam/staffs  
  功能：获取检查工作人员列表（执行与录入人选择）。

- GET /exam/records?keyword=&page=&size=  
  功能：查询当前检查员经手（执行或录入）的检查记录，支持姓名/病历号关键词过滤。

- GET /exam/patients/{patientId}/records  
  功能：查看当前检查员经手的指定患者的检查明细（项目、结果、时间、操作类型）。

---

## 检验员（LAB）

- GET /lab/applies?status=UNFINISHED&page=&size=  
  功能：查看待执行的检验申请列表。

- POST /lab/applies/{applyId}/cancel  
  功能：取消检验申请，状态置 RETURNED，记录操作日志。

- POST /lab/applies/{applyId}/execute  
  功能：开始执行检验，生成 operation_log（EXECUTE），如主流程非“检验中”则置为“检验中”。

- POST /lab/applies/{applyId}/result  
  功能：录入检验结果，生成 operation_log（INPUT_RESULT），保存结果并将申请状态置为 FINISHED。

- GET /lab/departments  
  功能：获取检验科室列表。

- GET /lab/staffs  
  功能：获取检验工作人员列表。

- GET /lab/records?keyword=&page=&size=  
  功能：查询当前检验员经手的检验记录。

- GET /lab/patients/{patientId}/records  
  功能：查看当前检验员经手的指定患者检验明细。

---

## 处置员（PROCEDURE）

- GET /procedure/applies?status=UNFINISHED&page=&size=  
  功能：查看待执行的处置申请列表。

- POST /procedure/applies/{applyId}/cancel  
  功能：取消处置申请，状态置 RETURNED，记录操作日志。

- POST /procedure/applies/{applyId}/execute  
  功能：开始执行处置，生成 operation_log（EXECUTE），如主流程非“处置中”则置为“处置中”。

- POST /procedure/applies/{applyId}/result  
  功能：录入处置结果，生成 operation_log（INPUT_RESULT），保存结果并将申请状态置为 FINISHED。

- GET /procedure/departments  
  功能：获取处置科室列表。

- GET /procedure/staffs  
  功能：获取处置工作人员列表。

- GET /procedure/records?keyword=&page=&size=  
  功能：查询当前处置员经手的处置记录。

- GET /procedure/patients/{patientId}/records  
  功能：查看当前处置员经手的指定患者处置明细。

---

## 药房管理员 - 发药/退药/记录

- GET /pharmacy/dispense/pending?keyword=&page=&size=  
  功能：查询待发药患者与处方摘要（仅已缴费的处方项可发药），用于出药队列。

- GET /pharmacy/prescriptions?patientMedicalNo=&status=UNFINISHED|FINISHED  
  功能：按患者病历号查看处方明细，支持按状态过滤（未发药/已发药）。

- POST /pharmacy/dispense  
  功能：对选定处方明细执行发药，生成发药记录（pharmacy_record），扣减库存，并更新处方明细状态为 FINISHED。

- POST /pharmacy/return  
  功能：对选定处方明细执行退药，生成退药记录（pharmacy_record），回冲库存，并更新处方明细状态为 RETURNED。

- GET /pharmacy/records?keyword=&page=&size=  
  功能：查询当前药房管理员的发药/退药操作记录（含患者与药品信息）。

---

## 药库管理

- GET /drugs?page=&size=&keyword=  
  功能：分页查询药品信息（名称、规格、单位、库存数量、价格等），支持关键字筛选。

- POST /drugs  
  功能：新增药品记录，录入基础信息与初始库存（如需）。

- PUT /drugs/{id}  
  功能：更新药品信息（规格、价格、状态、库存调整入口可另设）。

---

## 系统管理员

- GET /admin/employees?page=&size=&keyword=  
  功能：管理员视角分页查询员工（可含更多敏感字段/过滤条件）。

- POST /admin/employees  
  功能：管理员新增员工并配置角色权限、所属科室与初始登录设置。

- PUT /admin/employees/{id}  
  功能：管理员更新员工信息与角色权限，记录审计。

- PUT /admin/doctors/{doctorId}/quota  
  功能：设置或调整医生当日/当前时段号额（含加号/限号），影响挂号放号策略。

- PUT /admin/registration/prices  
  功能：配置普通号/专家号的挂号金额（建议支持生效时间与版本管理）。

---

## 统计图表

- 统一时间筛选：最近 1 月（按日）、最近 1 季度（按日）、最近 1 年（按月）、全部（按年）、自定义（自动粒度）  
  自动粒度：跨度 ≤90 天用日；≤2 年用月；>2 年用年。

- GET /stats/registrations/trend?startDate=&endDate=&granularity=auto  
  功能：返回挂号量的时间序列（按日/周/月/年聚合），用于趋势分析。

- GET /stats/registrations/type-breakdown?startDate=&endDate=  
  功能：统计时间段内普通号/专家号挂号数量与占比，用于结构对比（饼图/条形）。

- GET /stats/registrations/by-department?startDate=&endDate=&topN=  
  功能：统计时间段内各门诊科室的挂号人数，支持 Top N，用于科室负载对比。

- GET /stats/registrations/by-doctor?startDate=&endDate=&topN=  
  功能：统计时间段内各医生的挂号量，支持 Top N，用于医生工作量对比。

- GET /stats/revenue/trend?startDate=&endDate=&granularity=auto&metric=net|gross  
  功能：返回收入趋势（净收/应收），按时间粒度聚合，用于收入变化与季节性观察。

- GET /stats/revenue/by-type?startDate=&endDate=  
  功能：按业务类型（挂号/检查/检验/处置/药品）统计收入结构与占比。

- GET /stats/revenue/by-payment-method?startDate=&endDate=  
  功能：按支付方式统计收入金额/笔数占比（医保/自费/微信/支付宝等）。

- GET /stats/revenue/by-department?startDate=&endDate=&topN=  
  功能：按科室统计收入金额（支持 Top N），用于识别高收入科室。

- GET /stats/revenue/by-doctor?startDate=&endDate=&topN=  
  功能：按医生统计收入金额（支持 Top N），用于医生产出分析与绩效。

- GET /stats/refund/trend?startDate=&endDate=&granularity=auto  
  功能：返回退费金额与退费率的时间序列（可双轴），用于监控异常与流程改进。

---

## 响应示例（统一）

{
"code": 200,
"message": "OK",
"data": {...},
"meta": { "page": 1, "size": 20, "total": 135, "totalPages": 7 }
}

## 错误码建议

- 200 OK
- 400xx 参数/校验错误
- 401xx 认证失败/过期
- 403xx 无权限
- 404xx 资源不存在
- 409xx 并发/状态冲突（如重复结算、已执行不可退）
- 422xx 业务校验失败（医保/库存不足等）
- 500xx 系统内部异常
