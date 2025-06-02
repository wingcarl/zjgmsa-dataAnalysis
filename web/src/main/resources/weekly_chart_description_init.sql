-- 周报图表描述初始化数据
-- 插入 weeklyReportChart.html 页面中各个图表的描述信息

INSERT INTO js_chart_description (chart_id, chart_name, description, create_date, update_date) VALUES 
('weekly-department-sea-ship-chart', '各部门海船安检情况', '展示各部门海船安检数据的对比分析，包括检查数量、缺陷数量和滞留数量的本期与上期对比，帮助识别各部门的检查工作效果和异常情况。', NOW(), NOW()),
('weekly-department-river-ship-chart', '各部门内河船安检情况', '展示各部门内河船安检数据的统计分析，通过本期与上期数据对比，反映内河船舶安全检查的执行情况和发现问题的趋势。', NOW(), NOW()),
('weekly-department-onsite-chart', '各部门船舶现场监督情况', '统计各部门船舶现场监督工作的执行情况，包括监督数量和异常发现数量，反映现场监督工作的覆盖面和发现问题的能力。', NOW(), NOW()),
('weekly-department-sea-ship-defect-rate-chart', '各部门海船安检单船缺陷数量', '计算并展示各部门海船安检中单船平均缺陷数量，通过横向对比识别缺陷发现率较高的部门，为质量控制提供参考。', NOW(), NOW()),
('weekly-department-river-ship-defect-rate-chart', '各部门内河船安检单船缺陷数量', '分析各部门内河船安检中单船平均缺陷数量，通过数据排序展示各部门检查质量和缺陷发现效率的差异。', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
chart_name = VALUES(chart_name),
description = VALUES(description),
update_date = NOW(); 