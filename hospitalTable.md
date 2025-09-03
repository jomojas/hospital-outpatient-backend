# 数据库表

### 员工科室信息

- 员工信息表：

```sql
CREATE TABLE staff (
    staff_id      INT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(50) NOT NULL,
    phone         VARCHAR(20),
    id_card       VARCHAR(20),
    department_id INT,
    description   VARCHAR(100),
    role_id       INT NOT NULL,
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES staff_role(role_id),
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);
```

- 员工账号表：

```sql
CREATE TABLE staff_account (
    account_id    INT PRIMARY KEY AUTO_INCREMENT,
    staff_id      INT NOT NULL,
    account_name  VARCHAR(50) UNIQUE NOT NULL,
    password      VARCHAR(100) NOT NULL,
    status        TINYINT DEFAULT 0,   -- 0=正常 1=禁用/离职
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login    DATETIME,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);
```

- 员工角色表：

```sql
CREATE TABLE staff_role (
    role_id      INT PRIMARY KEY AUTO_INCREMENT,
    role_name    VARCHAR(50) UNIQUE NOT NULL,   -- doctor, cashier, pharmacist, inspector, etc.
    description  VARCHAR(100)
);
```

- 医生扩展表：

```sql
CREATE TABLE staff_doctor_ext (
    staff_id      INT PRIMARY KEY,
    is_expert     TINYINT DEFAULT 0,          -- 0=普通，1=专家
    -- schedule_id   INT,                        -- 排班ID
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);
```

- 科室表：

```sql
CREATE TABLE department (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(50) NOT NULL, -- 科室名称
    type VARCHAR(50) NOT NULL -- 科室类型（如 门诊, 检查, 检验, 处置）【后端枚举类】
);
```

- 后端科室枚举类:

```java
public enum DepartmentType {
  OUTPATIENT,   // 门诊
  EXAM,         // 检查
  LAB,          // 检验
  PROCEDURE,    // 处置
  PHARMACY,     // 药房
  EMERGENCY     // 急诊
}
```

### 患者挂号信息

-患者信息表：

```sql
CREATE TABLE patient (
    patient_id INT PRIMARY KEY AUTO_INCREMENT, -- 自增主键
    patient_no VARCHAR(20) UNIQUE NOT NULL, -- 病历号，唯一
    name VARCHAR(50) NOT NULL, -- 姓名
    gender VARCHAR(10) NOT NULL, -- 性别
    birthday DATE, -- 出生日期
    id_card VARCHAR(20), -- 身份证号
    address VARCHAR(100) -- 家庭住址
);
```

- 挂号信息表：

```sql
CREATE TABLE registration (
    registration_id INT PRIMARY KEY AUTO_INCREMENT, -- 挂号记录 ID
    patient_id INT NOT NULL, -- 患者 ID，外键
    department_id INT NOT NULL, -- 科室 ID
    doctor_id INT NOT NULL, -- 医生 ID（staff表的外键）
    visit_date DATE NOT NULL, -- 看诊日期
    period VARCHAR(10) NOT NULL, -- 午别（枚举：上午/下午）
    number_type VARCHAR(20) NOT NULL, -- 号别（枚举：普通号/专家号等）
    init_quota INT NOT NULL, -- 初始号额
    used_quota INT NOT NULL, -- 已用号额
    settlement_type_id INT NOT NULL, -- 结算类别 ID，外键
    payment_method_id INT NOT NULL, -- 收费方式 ID，外键
    payable_amount DECIMAL(10,2) NOT NULL, -- 应收金额
    medical_record_book TINYINT DEFAULT 0, -- 是否购买病历本（0=否，1=是）
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (department_id) REFERENCES department(department_id),
    FOREIGN KEY (doctor_id) REFERENCES staff(staff_id), -- 这里关联 staff 主键
    FOREIGN KEY (settlement_type_id) REFERENCES settlement_type(settlement_type_id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id)
);
```

- 结算类型:

```sql
CREATE TABLE settlement_type (
    settlement_type_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL, -- 结算类别名（如医保、自费等）
    description VARCHAR(100) -- 描述
);
```

- 收费方式：

```sql
CREATE TABLE payment_method (
    payment_method_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL, -- 收费方式名（如医保卡、现金等）
    description VARCHAR(100) -- 描述
);
```

- 午别（上午/下午）号别（普通号/专家号）使用后端枚举类，
- 午别：

```java
public enum Period {
    MORNING,    // 上午
    AFTERNOON   // 下午
}
```

- 号别：

```java
public enum NumberType {
    GENERAL,    // 普通号
    SPECIALIST  // 专家号
}
```

- 后端枚举状态

```java
public enum VisitStatus {
    WAITING_FOR_CONSULTATION,        // 待看诊
    INITIAL_CONSULTATION_DONE,       // 已初诊
    WAITING_FOR_PROJECT_PAYMENT,     // 待项目缴费（医生开具检查/检验/处置后，患者未缴费）
    WAITING_FOR_CHECKUP,             // 待检查（已缴费但未做检查/检验/处置）
    CHECKING,                        // 检查中
    WAITING_FOR_REVISIT,             // 待复诊
    WAITING_FOR_PRESCRIPTION_PAYMENT,// 待处方缴费（医生开具处方后，患者未缴费）
    WAITING_FOR_MEDICINE,            // 待取药（已缴费但未取药）
    MEDICINE_TAKEN,                  // 已取药
    MEDICINE_RETURNED,               // 已退药
    FINISHED                         // 诊疗结束
}
```

- 状态转换操作：
  挂号完成（待看诊） → 初诊（已初诊） → 开检查（待项目缴费）→ 待检查 → 检查开始（检查中） → 检查完成（待复诊） → 已复诊 → 待处方缴费 → 待取药 → 已取药 → 已退药 → 诊疗结束

- 主流程表：

```sql
CREATE TABLE patient_visit (
    visit_id INT PRIMARY KEY AUTO_INCREMENT, -- 主流程 ID
    patient_id INT NOT NULL, -- 患者 ID，外键
    registration_id INT NOT NULL, -- 挂号信息 ID，外键
    department_id INT NOT NULL, -- 科室 ID
    doctor_id INT NOT NULL, -- 主诊医生 ID
    visit_date DATE NOT NULL, -- 就诊日期
    current_status VARCHAR(40) NOT NULL, -- 当前状态（枚举：待看诊、已初诊、待检查、待复诊、待付费、待取药、已取药、已退药、诊疗结束）
    status_changed_at DATETIME NOT NULL, -- 状态变更时间
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,-- 创建时间
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间

    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (registration_id) REFERENCES registration(registration_id),
    FOREIGN KEY (department_id) REFERENCES department(department_id),
    FOREIGN KEY (doctor_id) REFERENCES staff(staff_id)
);
```

### 患者交易信息

- 交易表：

```sql
CREATE TABLE transaction (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    business_type VARCHAR(20) NOT NULL, -- REGISTRATION | DRUG | ITEM
    registration_id INT NOT NULL, -- 挂号 ID，外键
    patient_id INT NOT NULL, -- 患者 ID，外键
    amount DECIMAL(10,2) NOT NULL, -- 金额
    transaction_type VARCHAR(10) NOT NULL, -- PAY / REFUND
    payment_method_id INT NOT NULL, -- 收费方式 ID，外键
    transaction_time DATETIME NOT NULL, -- 交易时间
    cashier_id INT NOT NULL, -- 收费员 ID，外键
    remark VARCHAR(100),

    -- 外键约束
    FOREIGN KEY (registration_id) REFERENCES registration(registration_id),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id),
    FOREIGN KEY (cashier_id) REFERENCES staff(staff_id)
);
```

- 缴费类型枚举：

```java
public enum BusinessType {
    REGISTRATION,   // 挂号
    DRUG,           // 药品
    ITEM            // 诊疗项目（检查/检验/处置合并）
}
```

- 交易类型枚举：

```java
public enum TransactionType {
    PAY,        // 缴费
    REFUND      // 退费
}
```

### 项目药品信息

- 药品信息表：

```sql
CREATE TABLE drug (
    drug_id INT PRIMARY KEY AUTO_INCREMENT, -- 药品主键 ID
    drug_code VARCHAR(50) NOT NULL, -- 药品编号/编码
    drug_name VARCHAR(100) NOT NULL, -- 药品名称
    category_id INT NOT NULL, -- 药品类别 ID，外键
    production_date DATE, -- 生产日期
    shelf_life VARCHAR(20), -- 保质期
    stock_quantity DECIMAL(12,2) DEFAULT 0, -- 库存数量
    specification VARCHAR(50), -- 规格（如 200g）(字符串，添加药品时手动添加)
    unit VARCHAR(10), -- 单位（如 BOX/BOTTLE/PIECE/TUBE 等）
    retail_price DECIMAL(10,2), -- 零售价
    description VARCHAR(255), -- 备注
    FOREIGN KEY (category_id) REFERENCES drug_category(category_id)
);
```

- 药品类别表：

```sql
CREATE TABLE drug_category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(100)
);
```

- 发退药记录表：

```sql
CREATE TABLE pharmacy_record (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    prescription_id INT NOT NULL,        -- 关联处方ID
    patient_no VARCHAR(20) NOT NULL,     -- 病历号
    patient_name VARCHAR(50) NOT NULL,   -- 姓名（为冗余方便查询）
    drug_id INT NOT NULL,                -- 药品ID
    drug_name VARCHAR(100) NOT NULL,     -- 药品名称（为冗余方便查询）
    quantity DECIMAL(10,2) NOT NULL,     -- 发药/退药数量
    amount DECIMAL(10,2) NOT NULL,       -- 金额
    operate_type VARCHAR(10) NOT NULL,   -- 操作类型（'DISPENSE'发药/'RETURN'退药）
    operator_id INT NOT NULL,            -- 操作员（药房人员ID）
    operate_time DATETIME NOT NULL,      -- 操作时间
    remark VARCHAR(255),                 -- 备注或退药原因
    FOREIGN KEY (prescription_id) REFERENCES prescription(prescription_id),
    FOREIGN KEY (drug_id) REFERENCES drug(drug_id),
    FOREIGN KEY (operator_id) REFERENCES staff(staff_id)
);
```

- 药品单位枚举类：

```java
public enum DrugUnit {
    BOX,    // 盒
    BOTTLE, // 瓶
    PIECE,  // 片
    TUBE,   // 支
    GRAM,   // 克
    MILLIGRAM, // 毫克
    MILLILITER // 毫升
}
```

- 医疗项目表：

```sql
CREATE TABLE medical_service_item (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    item_code VARCHAR(50) NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    item_type VARCHAR(20) NOT NULL, -- 'EXAM'/'LAB'/'DISPOSAL'
    price DECIMAL(10,2),
    description VARCHAR(255),
    status VARCHAR(10) -- 'ENABLED'/'DISABLED'
);
```

- 项目类别后端枚举：

```java
public enum MedicalItemType {
    EXAM,      // 检查
    LAB,       // 检验
    DISPOSAL  // 处置
```

- 医疗项目申请表：

```sql
CREATE TABLE medical_item_apply (
    apply_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT NOT NULL,                                   -- 新增，病案ID外键
    registration_id INT NOT NULL,
    item_id INT NOT NULL,
    apply_type VARCHAR(20) NOT NULL, -- 'EXAM'/'LAB'/'DISPOSAL'
    apply_purpose VARCHAR(100),
    apply_site VARCHAR(50),
    apply_time DATETIME,
    performer_id INT, -- 执行人（staff表的外键）
    result_recorder_id INT,
    perform_time DATETIME,
    result VARCHAR(255),
    status VARCHAR(20), -- 'UNFINISHED'/'FINISHED'/'CANCELLED'/'PENDING_PAYMENT'
    remark VARCHAR(255),
    FOREIGN KEY (record_id) REFERENCES medical_record(record_id),      -- 新增外键
    FOREIGN KEY (performer_id) REFERENCES staff(staff_id),
    FOREIGN KEY (registration_id) REFERENCES registration(registration_id),
    FOREIGN KEY (item_id) REFERENCES medical_service_item(item_id)
);
```

- 项目操作记录：

```sql
CREATE TABLE medical_item_operation_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    apply_id INT NOT NULL,                  -- 关联medical_item_apply表主键
    operator_id INT NOT NULL,               -- 操作员ID（staff表外键）
    operate_time DATETIME NOT NULL,         -- 操作时间
    item_id INT NOT NULL,                   -- 项目ID（medical_service_item表外键）
    item_name VARCHAR(100) NOT NULL,        -- 项目名称（冗余）
    item_type VARCHAR(20) NOT NULL, -- 'EXAM'/'LAB'/'DISPOSAL' -- 项目类型（冗余）
    patient_no VARCHAR(20) NOT NULL,        -- 病历号
    patient_name VARCHAR(50) NOT NULL,      -- 患者姓名（冗余）
    result VARCHAR(255),                    -- 操作结果（可选）
    remark VARCHAR(255),                    -- 备注
    FOREIGN KEY (apply_id) REFERENCES medical_item_apply(apply_id),
    FOREIGN KEY (operator_id) REFERENCES staff(staff_id),
    FOREIGN KEY (item_id) REFERENCES medical_service_item(item_id)
);
```

- 申请状态枚举：

```java
public enum MedicalItemApplyStatus {
    PENDING_PAYMENT, // 待缴费
    UNFINISHED,      // 已缴费待完成
    FINISHED,        // 已完成
    CANCELLED        // 已作废/撤销
}
```

- 门诊医生开具处方表：

```sql
CREATE TABLE prescription (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,       -- 处方主键
    record_id INT NOT NULL,                               -- 新增，病案ID外键
    registration_id INT NOT NULL,                         -- 挂号ID（外键）
    drug_id INT NOT NULL,                                 -- 药品ID（外键）
    dosage VARCHAR(50) NOT NULL,                          -- 用法用量频次（如：口服 1片 每日3次）
    quantity DECIMAL(10,2) NOT NULL,                      -- 药品数量
    prescribe_time DATETIME NOT NULL,                     -- 开立时间
    status VARCHAR(20) NOT NULL,                          -- 处方状态：如 'UNFINISHED'（未发药）、'FINISHED'（已发药）、'CANCELLED'（已作废）,'PENDING_PAYMENT'（待缴费）
    remark VARCHAR(255),                                   -- 备注
    FOREIGN KEY (record_id) REFERENCES medical_record(record_id),      -- 新增外键
    FOREIGN KEY (registration_id) REFERENCES registration(registration_id),
    FOREIGN KEY (drug_id) REFERENCES drug(drug_id)
);
```

- 处方状态枚举：

```java
public enum PrescriptionStatus {
    PENDING_PAYMENT, // 待缴费
    UNFINISHED,      // 已缴费、待发药
    FINISHED,        // 已发药
    CANCELLED        // 已退药/作废
}
```

- 病案表：

```sql
CREATE TABLE medical_record (
    record_id INT PRIMARY KEY AUTO_INCREMENT,                 -- 病案主键
    patient_no VARCHAR(20) NOT NULL,                          -- 外键，病历号
    registration_id INT NOT NULL,                             -- 外键，挂号记录ID
    chief_complaint VARCHAR(500),                             -- 主诉
    present_history VARCHAR(1000),                            -- 现病史
    physical_exam VARCHAR(1000),                              -- 体格检查
    diagnosis VARCHAR(1000),                                  -- 诊断结果
    treatment_plan VARCHAR(1000),                             -- 处理意见
    doctor_id INT,                                            -- 经治医生ID（staff表的外键）
    record_time DATETIME DEFAULT CURRENT_TIMESTAMP,            -- 病案生成时间
    FOREIGN KEY (patient_no) REFERENCES patient(patient_no),
    FOREIGN KEY (registration_id) REFERENCES registration(registration_id),
    FOREIGN KEY (doctor_id) REFERENCES staff(staff_id)
);
```
