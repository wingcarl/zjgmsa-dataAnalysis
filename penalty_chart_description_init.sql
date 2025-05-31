-- 处罚图表描述表初始化数据
-- 确保所有处罚图表的记录都存在

-- 删除可能存在的旧记录
DELETE FROM chart_description WHERE chart_id IN (
    'punish-agency-chart',
    'penalty-amount-chart', 
    'average-penalty-amount-chart',
    'key-violation-ratio-chart',
    'case-reason-chart',
    'management-category-chart',
    'violation-circumstances-chart',
    'party-type-chart',
    'port-registry-chart'
);

-- 插入所有处罚图表记录
INSERT INTO chart_description (id, chart_id, chart_name, description, create_date, update_date, del_flag) VALUES
(UUID(), 'punish-agency-chart', '各部门处罚决定数量', '展示各海事机构处罚决定数量的统计对比，反映各部门的执法活动水平和违法案件处理情况。', NOW(), NOW(), '0'),
(UUID(), 'penalty-amount-chart', '各部门处罚金额', '统计各海事机构的处罚金额总计，体现各部门在海事执法中的处罚力度和涉案金额规模。', NOW(), NOW(), '0'),
(UUID(), 'average-penalty-amount-chart', '各部门平均处罚金额', '分析各海事机构的平均处罚金额水平，反映不同部门在案件处理中的处罚标准和违法行为严重程度。', NOW(), NOW(), '0'),
(UUID(), 'key-violation-ratio-chart', '各部门重点违法行为占比', '展示各海事机构重点违法行为案件在总案件中的占比情况，体现重点违法行为的分布特点。', NOW(), NOW(), '0'),
(UUID(), 'case-reason-chart', '处罚案由对比', '统计不同案由类型的处罚案件数量分布，分析海事违法行为的主要类型和发生频率。', NOW(), NOW(), '0'),
(UUID(), 'management-category-chart', '处罚类别对比', '按照业务管理类别分类统计处罚案件，反映不同业务领域的违法行为发生情况和监管重点。', NOW(), NOW(), '0'),
(UUID(), 'violation-circumstances-chart', '违法情节统计', '统计不同违法情节类型的案件分布，分析违法行为的严重程度和处罚适用情况。', NOW(), NOW(), '0'),
(UUID(), 'party-type-chart', '当事人类型统计', '按照当事人类型分类统计处罚案件，了解不同类型主体的违法行为特点和监管需求。', NOW(), NOW(), '0'),
(UUID(), 'port-registry-chart', '船籍港处罚数量统计', '统计不同船籍港船舶的处罚案件数量，分析船舶来源地与违法行为的关联性。', NOW(), NOW(), '0');

-- 查询插入结果
SELECT chart_id, chart_name FROM chart_description WHERE del_flag = '0' AND chart_id LIKE '%-chart' ORDER BY id; 