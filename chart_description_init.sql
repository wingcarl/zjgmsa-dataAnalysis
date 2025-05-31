-- 图表描述表初始化数据
-- 确保所有图表的记录都存在

-- 删除可能存在的旧记录
DELETE FROM chart_description WHERE chart_id IN (
    'field-patrol-chart',
    'electronic-patrol-chart', 
    'drone-patrol-chart',
    'major-item-chart',
    'inspection-trend-chart',
    'department-chart',
    'patrol-boat-chart',
    'electronic-cruise-chart',
    'uav-data-chart',
    'task-dispatch-chart',
    'anchor-rate-chart',
    'port-shipping-chart',
    'red-code-ship-chart'
);

-- 插入所有图表记录
INSERT INTO chart_description (id, chart_id, chart_name, description, create_date, update_date, del_flag) VALUES
(UUID(), 'field-patrol-chart', '现场巡航统计', '现场巡航数据统计，包括发现异常数量和立案调查数量的对比分析。', NOW(), NOW(), '0'),
(UUID(), 'electronic-patrol-chart', '电子巡航统计', '电子巡航数据统计，显示各部门电子巡航发现异常和立案调查的情况。', NOW(), NOW(), '0'),
(UUID(), 'drone-patrol-chart', '无人机巡航统计', '无人机巡航数据统计，展示无人机巡航发现异常和立案调查的数据分析。', NOW(), NOW(), '0'),
(UUID(), 'major-item-chart', '检查项目分布', '检查项目分布图，按检查项目统计各类型检查的数量分布情况。', NOW(), NOW(), '0'),
(UUID(), 'inspection-trend-chart', '检查数量趋势', '检查数量趋势图，显示按日、周、月、季度的检查数量变化趋势。', NOW(), NOW(), '0'),
(UUID(), 'department-chart', '各部门检查统计', '各部门检查统计饼图，展示各部门的检查数量占比分布。', NOW(), NOW(), '0'),
(UUID(), 'patrol-boat-chart', '海巡艇巡航数据对比', '海巡艇巡航数据对比图，包括巡航时间和巡航艘次的统计对比。', NOW(), NOW(), '0'),
(UUID(), 'electronic-cruise-chart', '电子巡航数据统计', '电子巡航数据统计图，显示各组织单位的电子巡航次数统计。', NOW(), NOW(), '0'),
(UUID(), 'uav-data-chart', '无人机数据统计', '无人机数据统计图，包括单兵飞行航次、无人机库飞行航次和处罚数量。', NOW(), NOW(), '0'),
(UUID(), 'task-dispatch-chart', '任务派发数据统计', '任务派发数据统计图，显示派发任务次数和巡航任务派发次数。', NOW(), NOW(), '0'),
(UUID(), 'anchor-rate-chart', '锚泊申请率统计', '锚泊申请率统计图，展示各组织的锚泊申请率数据。', NOW(), NOW(), '0'),
(UUID(), 'port-shipping-chart', '港航一体化数据统计', '港航一体化数据统计图，包括闭环率和到港作业率的统计分析。', NOW(), NOW(), '0'),
(UUID(), 'red-code-ship-chart', '红码船数据统计', '红码船数据统计图，显示红码船处置率、登轮检查率和相关数量统计。', NOW(), NOW(), '0');

-- 查询验证插入结果
SELECT chart_id, chart_name, description FROM chart_description WHERE del_flag = '0' ORDER BY chart_id; 