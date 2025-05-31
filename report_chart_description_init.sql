-- 船舶监督图表描述表初始化数据
-- 确保所有船舶监督图表的记录都存在

-- 删除可能存在的旧记录
DELETE FROM chart_description WHERE chart_id IN (
    'department-sea-ship-chart',
    'department-river-ship-chart',
    'department-psc-chart',
    'department-onsite-chart',
    'department-sea-ship-defect-rate-chart',
    'department-river-ship-defect-rate-chart',
    'department-psc-defect-rate-chart',
    'department-onsite-abnormal-rate-chart',
    'sea-ship-defect-type-chart',
    'river-ship-defect-type-chart',
    'onsite-content-abnormal-rate-chart'
);

-- 插入所有船舶监督图表记录
INSERT INTO chart_description (id, chart_id, chart_name, description, create_date, update_date, del_flag) VALUES
(UUID(), 'department-sea-ship-chart', '各部门海船安检情况', '展示各海事机构海船安检数量统计，反映各部门在海船安全检查方面的工作完成情况和执法活动水平。', NOW(), NOW(), '0'),
(UUID(), 'department-river-ship-chart', '各部门内河船安检情况', '统计各海事机构内河船舶安检数量，体现各部门在内河船舶安全监管中的检查频次和覆盖范围。', NOW(), NOW(), '0'),
(UUID(), 'department-psc-chart', '各部门PSC检查情况', '分析各海事机构港口国监督检查执行情况，反映对外国船舶的安全监管力度和国际履约水平。', NOW(), NOW(), '0'),
(UUID(), 'department-onsite-chart', '各部门船舶现场监督情况', '展示各海事机构船舶现场监督检查数量，体现对船舶日常运营安全的现场监管执法情况。', NOW(), NOW(), '0'),
(UUID(), 'department-sea-ship-defect-rate-chart', '各部门海船安检单船缺陷数量', '统计各海事机构海船安检中发现的平均单船缺陷数量，反映检查质量和船舶安全状况。', NOW(), NOW(), '0'),
(UUID(), 'department-river-ship-defect-rate-chart', '各部门内河船安检单船缺陷数量', '分析各海事机构内河船安检中的平均单船缺陷发现情况，体现内河船舶安全管理水平。', NOW(), NOW(), '0'),
(UUID(), 'department-psc-defect-rate-chart', '各部门PSC检查单船缺陷数量', '展示各海事机构PSC检查中发现的平均单船缺陷数量，反映外国船舶的安全合规状况。', NOW(), NOW(), '0'),
(UUID(), 'department-onsite-abnormal-rate-chart', '各部门现场监督检查异常发现率', '统计各海事机构现场监督检查中的异常发现比率，体现现场监督检查的有效性和针对性。', NOW(), NOW(), '0'),
(UUID(), 'sea-ship-defect-type-chart', '海船安检常见缺陷类型分布', '分析海船安检中发现的各类缺陷类型分布情况，识别船舶安全管理的薄弱环节和重点监管领域。', NOW(), NOW(), '0'),
(UUID(), 'river-ship-defect-type-chart', '内河船安检常见缺陷类型分布', '统计内河船安检中的缺陷类型分布特点，为针对性安全监管和船舶管理提供数据支撑。', NOW(), NOW(), '0'),
(UUID(), 'onsite-content-abnormal-rate-chart', '现场监督内容异常率分析', '展示不同现场监督检查内容的异常发现率排序，识别高风险监督项目和安全管理重点。', NOW(), NOW(), '0');

-- 查询插入结果
SELECT chart_id, chart_name FROM chart_description WHERE del_flag = '0' AND chart_id LIKE '%-chart' ORDER BY id; 