-- ...existing code...

-- 使用 drug_id 的确定性顺序为前 24 条记录依次填充厂家信息（MySQL 8+）
UPDATE drug d
JOIN (
  SELECT drug_id, rn
  FROM (
    SELECT
      drug_id,
      ROW_NUMBER() OVER (ORDER BY drug_id) AS rn
    FROM drug
  ) t
  WHERE rn <= 24
) AS ranked ON ranked.drug_id = d.drug_id
JOIN (
  SELECT 1 AS rn, '上海新科技制药有限公司' AS manufacturer UNION ALL
  SELECT 2, '江苏恒瑞医药股份有限公司' UNION ALL
  SELECT 3, '北京益民制药厂' UNION ALL
  SELECT 4, '湖南华康药业有限公司' UNION ALL
  SELECT 5, '广州同仁医药有限公司' UNION ALL
  SELECT 6, '湖北天元制药有限公司' UNION ALL
  SELECT 7, '浙江京新药业股份有限公司' UNION ALL
  SELECT 8, '四川科伦药业股份有限公司' UNION ALL
  SELECT 9, '深圳迈瑞生物医疗有限公司' UNION ALL
  SELECT 10, '重庆康华制药有限公司' UNION ALL
  SELECT 11, '山东齐鲁制药有限公司' UNION ALL
  SELECT 12, '哈尔滨三联药业有限公司' UNION ALL
  SELECT 13, '上海医药集团股份有限公司' UNION ALL
  SELECT 14, '扬子江药业集团有限公司' UNION ALL
  SELECT 15, '天津力生制药股份有限公司' UNION ALL
  SELECT 16, '江西青峰药业有限公司' UNION ALL
  SELECT 17, '西安杨森制药有限公司' UNION ALL
  SELECT 18, '武汉人福医药集团股份有限公司' UNION ALL
  SELECT 19, '石家庄以岭药业股份有限公司' UNION ALL
  SELECT 20, '云南白药集团股份有限公司' UNION ALL
  SELECT 21, '浙江华海药业股份有限公司' UNION ALL
  SELECT 22, '成都倍特药业股份有限公司' UNION ALL
  SELECT 23, '上海复星医药（集团）股份有限公司' UNION ALL
  SELECT 24, '广州白云山制药股份有限公司'
) AS m ON m.rn = ranked.rn
SET d.manufacturer = m.manufacturer;

-- 将仍为 NULL 的 manufacturer 设置为“未知”
UPDATE drug
SET manufacturer = COALESCE(manufacturer, '未知');

-- ...existing code...
