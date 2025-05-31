-- 图表描述表
CREATE TABLE `chart_description` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `chart_id` varchar(100) NOT NULL COMMENT '图表唯一标识',
  `chart_name` varchar(200) NOT NULL COMMENT '图表名称',
  `description` text COMMENT '图表描述内容',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_chart_id` (`chart_id`) COMMENT '图表ID唯一索引',
  KEY `idx_chart_name` (`chart_name`) COMMENT '图表名称索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图表描述信息表';

-- 插入初始化数据（月度动态图表）
INSERT INTO `chart_description` (`id`, `chart_id`, `chart_name`, `description`, `create_date`, `update_date`, `del_flag`) VALUES
('1', 'field-patrol-chart', '现场巡航统计', '', NOW(), NOW(), '0'),
('2', 'electronic-patrol-chart', '电子巡航统计', '', NOW(), NOW(), '0'),
('3', 'drone-patrol-chart', '无人机巡航统计', '', NOW(), NOW(), '0'),
('4', 'major-item-chart', '检查项目分布', '', NOW(), NOW(), '0'),
('5', 'inspection-trend-chart', '检查数量趋势', '', NOW(), NOW(), '0'),
('6', 'department-chart', '各部门检查统计', '', NOW(), NOW(), '0'),
('7', 'patrol-boat-chart', '海巡艇巡航数据对比', '', NOW(), NOW(), '0'),
('8', 'electronic-cruise-chart', '电子巡航数据统计', '', NOW(), NOW(), '0'),
('9', 'uav-data-chart', '无人机数据统计', '', NOW(), NOW(), '0'),
('10', 'task-dispatch-chart', '任务派发数据统计', '', NOW(), NOW(), '0'),
('11', 'anchor-rate-chart', '锚泊申请率统计', '', NOW(), NOW(), '0'),
('12', 'port-shipping-chart', '港航一体化数据统计', '', NOW(), NOW(), '0'),
('13', 'red-code-ship-chart', '红码船数据统计', '', NOW(), NOW(), '0'); 