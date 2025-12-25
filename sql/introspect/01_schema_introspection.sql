-- 使用说明：
-- 1) 先确保当前连接的数据库就是 hospital-outpatient（或你实际使用的 schema）。
-- 2) 直接整段执行；把输出结果复制给我即可。
-- 3) 本脚本只做查询，不会修改任何数据。
--
-- 兼容性：MySQL 5.7/8.0

/* ------------------------------
   0) 确认当前数据库
--------------------------------*/
SELECT DATABASE() AS current_schema;

/* ------------------------------
   1) 字段清单（最关键）
   包含：字段名/类型/是否可空/默认值/主键/自增/注释
--------------------------------*/
SELECT
  c.TABLE_NAME,
  c.ORDINAL_POSITION,
  c.COLUMN_NAME,
  c.COLUMN_TYPE,
  c.IS_NULLABLE,
  c.COLUMN_DEFAULT,
  c.COLUMN_KEY,
  c.EXTRA,
  c.CHARACTER_SET_NAME,
  c.COLLATION_NAME,
  c.COLUMN_COMMENT
FROM information_schema.COLUMNS c
WHERE c.TABLE_SCHEMA = DATABASE()
  AND c.TABLE_NAME IN (
    'patient',
    'patient_visit',
    'medical_record',
    'prescription',
    'registration',
    'medical_item_apply',
    'medical_item_operation_log',
    'drug',
    'drug_category',
    'pharmacy_record',
    'staff',
    'staff_account',
    'staff_role',
    'staff_doctor_ext',
    'doctor_quota',
    'number_type_config',
    'department',
    'department_role',
    'medical_service_item',
    'transaction',
    'payment_method',
    'settlement_type'
  )
ORDER BY c.TABLE_NAME, c.ORDINAL_POSITION;

/* ------------------------------
   2) 表信息（引擎/行数估计/注释）
--------------------------------*/
SELECT
  t.TABLE_NAME,
  t.ENGINE,
  t.TABLE_ROWS,
  t.CREATE_TIME,
  t.UPDATE_TIME,
  t.TABLE_COLLATION,
  t.TABLE_COMMENT
FROM information_schema.TABLES t
WHERE t.TABLE_SCHEMA = DATABASE()
  AND t.TABLE_NAME IN (
    'patient',
    'patient_visit',
    'medical_record',
    'prescription',
    'registration',
    'medical_item_apply',
    'medical_item_operation_log',
    'drug',
    'drug_category',
    'pharmacy_record',
    'staff',
    'staff_account',
    'staff_role',
    'staff_doctor_ext',
    'doctor_quota',
    'number_type_config',
    'department',
    'department_role',
    'medical_service_item',
    'transaction',
    'payment_method',
    'settlement_type'
  )
ORDER BY t.TABLE_NAME;

/* ------------------------------
   3) 索引信息（含唯一索引/复合索引）
--------------------------------*/
SELECT
  s.TABLE_NAME,
  s.INDEX_NAME,
  s.NON_UNIQUE,
  s.SEQ_IN_INDEX,
  s.COLUMN_NAME,
  s.COLLATION,
  s.CARDINALITY,
  s.SUB_PART,
  s.NULLABLE,
  s.INDEX_TYPE
FROM information_schema.STATISTICS s
WHERE s.TABLE_SCHEMA = DATABASE()
  AND s.TABLE_NAME IN (
    'patient',
    'patient_visit',
    'medical_record',
    'prescription',
    'registration',
    'medical_item_apply',
    'medical_item_operation_log',
    'drug',
    'drug_category',
    'pharmacy_record',
    'staff',
    'staff_account',
    'staff_role',
    'staff_doctor_ext',
    'doctor_quota',
    'number_type_config',
    'department',
    'department_role',
    'medical_service_item',
    'transaction',
    'payment_method',
    'settlement_type'
  )
ORDER BY s.TABLE_NAME, s.INDEX_NAME, s.SEQ_IN_INDEX;

/* ------------------------------
   4) 外键关系（用于排查关联字段）
--------------------------------*/
SELECT
  kcu.TABLE_NAME,
  kcu.CONSTRAINT_NAME,
  kcu.COLUMN_NAME,
  kcu.REFERENCED_TABLE_NAME,
  kcu.REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE kcu
WHERE kcu.TABLE_SCHEMA = DATABASE()
  AND kcu.REFERENCED_TABLE_NAME IS NOT NULL
  AND kcu.TABLE_NAME IN (
    'patient',
    'patient_visit',
    'medical_record',
    'prescription',
    'registration',
    'medical_item_apply',
    'medical_item_operation_log',
    'drug',
    'drug_category',
    'pharmacy_record',
    'staff',
    'staff_account',
    'staff_role',
    'staff_doctor_ext',
    'doctor_quota',
    'number_type_config',
    'department',
    'department_role',
    'medical_service_item',
    'transaction',
    'payment_method',
    'settlement_type'
  )
ORDER BY kcu.TABLE_NAME, kcu.CONSTRAINT_NAME, kcu.ORDINAL_POSITION;

/* ------------------------------
   5) （可选）每张表的 SHOW CREATE TABLE
   说明：information_schema 无法直接等价输出完整 DDL。
   如果你愿意，也请把下面这些逐条执行后的结果贴我。
--------------------------------*/
-- SHOW CREATE TABLE patient;
-- SHOW CREATE TABLE patient_visit;
-- SHOW CREATE TABLE medical_record;
-- SHOW CREATE TABLE prescription;
-- SHOW CREATE TABLE registration;
-- SHOW CREATE TABLE medical_item_apply;
-- SHOW CREATE TABLE medical_item_operation_log;
-- SHOW CREATE TABLE drug;
-- SHOW CREATE TABLE drug_category;
-- SHOW CREATE TABLE pharmacy_record;
-- SHOW CREATE TABLE staff;
-- SHOW CREATE TABLE staff_account;
-- SHOW CREATE TABLE staff_role;
-- SHOW CREATE TABLE staff_doctor_ext;
-- SHOW CREATE TABLE doctor_quota;
-- SHOW CREATE TABLE number_type_config;
-- SHOW CREATE TABLE department;
-- SHOW CREATE TABLE department_role;
-- SHOW CREATE TABLE medical_service_item;
-- SHOW CREATE TABLE `transaction`;
-- SHOW CREATE TABLE payment_method;
-- SHOW CREATE TABLE settlement_type;

