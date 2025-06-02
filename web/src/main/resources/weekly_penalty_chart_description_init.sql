-- 周报处罚图表描述初始化SQL
-- 插入周报处罚图表的描述信息

-- 各机构处罚决定数量图表
INSERT INTO chart_description (chart_id, chart_name, description, create_time, update_time) 
VALUES ('weekly-punish-agency-chart', '各机构处罚决定数量', 
'本图表展示了各执法机构在统计周期内的处罚决定数量对比情况。通过柱状图形式直观呈现不同海事处的执法工作量，有助于了解各机构的执法力度和工作效率。数据包括张家港锦丰海事处、张家港港区海事处、海巡执法支队等主要执法单位的处罚决定统计。', 
NOW(), NOW()) 
ON DUPLICATE KEY UPDATE 
chart_name = VALUES(chart_name), 
description = VALUES(description), 
update_time = NOW();

-- 各机构处罚金额图表
INSERT INTO chart_description (chart_id, chart_name, description, create_time, update_time) 
VALUES ('weekly-penalty-amount-chart', '各机构处罚金额', 
'本图表展示了各执法机构在统计周期内的处罚金额分布情况。通过柱状图形式展现不同海事处的处罚金额总额，反映了各机构执法的经济影响力和案件严重程度。金额统计包括所有类型的行政处罚，为执法效果评估提供重要参考依据。', 
NOW(), NOW()) 
ON DUPLICATE KEY UPDATE 
chart_name = VALUES(chart_name), 
description = VALUES(description), 
update_time = NOW();

-- 处罚案由对比图表
INSERT INTO chart_description (chart_id, chart_name, description, create_time, update_time) 
VALUES ('weekly-case-reason-chart', '处罚案由对比', 
'本图表展示了不同违法案由的处罚情况分布。通过柱状图形式对比各类违法行为的处罚频次，帮助识别当前海事监管中的主要问题类型和高发违法行为。案由分类涵盖了船舶航行安全、环境保护、港口作业等各个监管领域，为有针对性地开展执法工作提供数据支撑。', 
NOW(), NOW()) 
ON DUPLICATE KEY UPDATE 
chart_name = VALUES(chart_name), 
description = VALUES(description), 
update_time = NOW();

-- 处罚类别对比图表
INSERT INTO chart_description (chart_id, chart_name, description, create_time, update_time) 
VALUES ('weekly-management-category-chart', '处罚类别对比', 
'本图表采用饼图形式展示了不同业务类型的处罚案件分布比例。通过可视化的扇形图展现各业务领域的执法情况，包括船舶监督、港口监管、海洋环境保护等主要业务类别。这有助于了解执法工作的重点领域和资源配置情况，为执法策略调整提供参考。', 
NOW(), NOW()) 
ON DUPLICATE KEY UPDATE 
chart_name = VALUES(chart_name), 
description = VALUES(description), 
update_time = NOW(); 