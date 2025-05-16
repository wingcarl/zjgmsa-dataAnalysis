/*
Navicat Premium Data Transfer

Source Server         : 123
Source Server Type    : MySQL
Source Server Version : 50711
Source Host           : localhost:3306
Source Schema         : data_analysis

Target Server Type    : MySQL
Target Server Version : 50711
File Encoding         : 65001

Date: 11/04/2025 10:32:50
*/
基于上述数据结构，帮忙编写一个大型受限船舶的数据分析展示页面。

上面有一个时间选择，类似相关前端页面的做法。时间以周为最小单位，每一周均为上周五到本周四。但是可以选取多个周的数据。

根据选择的时间，系统自动计算该时间段内的数据。
包括以下数据卡片：该卡片需要计算环比数据。根据当前选择的时间段，去取环比的相应的时间端。
1.大型受限船舶数量（按照时间字段筛选）
2.大型受限船舶进港数量（进/出字段包含 “进”关键字）
3.大型受限船舶出港数量（进/出字段包含 “出”关键字）
4.CAPE船舶进港数量（进/出字段包含 “进”关键字，船长字段>=250)
5.CAPE船舶出港数量（进/出字段包含 “出”关键字，船长字段>=250）

下面是图表：
1.受限船舶趋势图，根据选择的时间段，展示该时间段受限船舶审批的数量趋势图，该趋势图可以修改展示的单位，默认以天计，用户可以切换为以周计算（上周五到本周四），以月计算、以年计算。
该趋势图可以根据船舶长度（范围），靠泊码头或泊位名称（模糊查询），货物名称（模糊查询），代理名称，船籍港进行组合筛选。
2.船舶长度分布图。根据船舶长度 <200,200-250,250+，分别统计相关数量。

在下方继续增加图表。
两个图表并列在一行。
第一个图表是按照代理名称分类统计每个代理的受限船舶数量。
第二个图标是按照代理名称分类统计每个代理 船舶最大淡水吃水的范围，最小吃水和最大吃水之间的范围图。
请给出需要对应增加的前端代码和后台Controller代码

在下方继续增加三行图表，为码头数据分析。

第一个图表是按照靠泊码头或泊位名称分类统计每个靠泊码头或泊位名称的受限船舶数量。
第二个图表是按照靠泊码头或泊位名称分类统计每个靠泊码头或泊位名称船舶最大淡水吃水的范围，最小吃水和最大吃水之间的范围图，考虑使用箱线图。
第三个图表是按照靠泊码头或泊位名称分类统计每个靠泊码头或泊位名称船舶长度的分布，考虑使用箱线图。
每个码头或泊位名称，数据库查询后需要进行处理，只保留中文，把符号和数字都去掉。
请给出需要对应增加的前端代码和后台Controller代码
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for large_restricted_vessels
-- ----------------------------
DROP TABLE IF EXISTS `large_restricted_vessels`;
CREATE TABLE `large_restricted_vessels`  (
`id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
`no` int(11) NULL DEFAULT NULL COMMENT '序号',
`operation_date` date NULL DEFAULT NULL COMMENT '日期',
`chinese_ship_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中文船名',
`english_ship_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文船名',
`mmsi` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '海上移动识别码（MMSI）',
`port_of_registry` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '船籍港',
`owner_or_operator` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有人或经营人',
`agent_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理名称',
`agent_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理联系电话',
`ship_length` decimal(10, 2) NULL DEFAULT NULL COMMENT '船长（米）',
`ship_width` decimal(10, 2) NULL DEFAULT NULL COMMENT '船宽（米）',
`max_height_above_water` decimal(10, 2) NULL DEFAULT NULL COMMENT '船舶水面以上最大高度（米）',
`max_freshwater_draft` decimal(10, 2) NULL DEFAULT NULL COMMENT '船舶最大淡水吃水（米）',
`gross_tonnage` decimal(10, 2) NULL DEFAULT NULL COMMENT '总吨',
`deadweight_tonnage` decimal(10, 2) NULL DEFAULT NULL COMMENT '核定载重吨',
`actual_cargo_status` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实际载货情况',
`cargo_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '货物名称',
`berth_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '靠泊码头或泊位名称',
`berth_tonnage_grade` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '靠泊码头或泊位等级（吨级）',
`entry_exit_direction` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进/出',
`expected_entry_exit_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预计进/出港日期',
`safety_measures` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '安全措施',
`create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
`create_date` datetime NOT NULL COMMENT '创建时间',
`update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
`update_date` datetime NOT NULL COMMENT '更新时间',
`remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '大型受限船舶表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of large_restricted_vessels
-- ----------------------------
INSERT INTO `large_restricted_vessels` VALUES ('1910520430440767488', 366, '2025-04-02', '万国顺达', 'WAN GUO SHUN DA', '413452530', '芜湖', '安徽万国航运有限公司', '江苏嵘海国际物流有限公司', '51258319595', 162.80, 25.60, 23.00, 10.70, 17167.00, 27279.00, '27000', '煤炭', '港务集团8#', '7万', '进', '2025-04-05', '福南水道落实一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520430574985216', 367, '2025-04-02', '利电1', 'LI DIAN 1', '413982000', '江阴', '江苏利电航运有限公司', '张家港海僡国际船务代理有限公司', '18951136779', 189.99, 32.26, 32.00, 11.50, 32895.00, 57000.00, '46500', '煤炭', '港务集团9#', '7万', '进', '2025-04-07', '福南水道落实一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520430642094080', 368, '2025-04-02', '神华511', 'SHEN HUA 511', '413261340', '上海', '国能远海航运有限公司', '中国张家港外轮代理有限公司', '0512-58335210', 189.99, 32.26, 31.75, 11.20, 29153.00, 48951.00, '46000', '煤炭', '港务集团8#', '7万', '进', '2025-04-07', '福南水道落实一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520430734368768', 369, '2025-04-02', '安茂山', 'AN MAO SHAN', '413186000', '上海', '中远海运散货运输有限公司', '中国张家港外轮代理有限公司', '0512-58335210', 199.99, 32.26, 37.10, 11.80, 33511.00, 57683.00, '50000', '玉米', '港务集团9#', '7万', '进', '2025-04-06', '福南水道落实一艘警戒艇、一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520430780506112', 370, '2025-04-02', '康斯特', 'CONSTITUTION', '311000379', '巴哈马', 'DORIAN ULSAN LPG TRANSPORT LLC', '中国张家港外轮代理有限公司', '58332535', 225.07, 36.60, 41.80, 9.50, 47379.00, 54577.00, '24080.70', '丙烷/丁烷', '东华能源', '5万', '进/出', '2025年4月9日/2025年4月11日', '进江安排一艘警戒艇全维，进福南水道安排两艘护航拖轮维护，出福南水道安排一艘护航拖轮，一艘警戒艇维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520430839226368', 371, '2025-04-02', '夜吻', 'NIGHTKISS', '311000125', '巴哈马', 'NIGHTKISS MARINE LTD', '张家港明洋船务代理有限公司', '51235027305', 292.00, 45.00, 55.00, 11.50, 93511.00, 179354.10, '85000', '铁矿', '港务集团5#', '10万', '进/出', '2025年4月3日/2025年4月5日', '一艘警戒艇、一艘护航拖轮进出江全维，过苏通大桥水域加派一艘拖轮，进福姜沙加派一艘拖轮维护，拖轮四靠三离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520430910529536', 372, '2025-04-02', '麒麟松', 'QI LIN SONG', '563215800', '新加坡', 'COSCO Shipping Specialized Carriers Co., Ltd', '中国张家港外轮代理有限公司', '15962367989', 179.50, 27.20, 38.45, 10.80, 20619.00, 27307.40, '19606.67', '袋装锂辉石', '永恒码头4#', '1万', '进', '2025-04-02', '大新水道落实一艘拖轮和一艘警戒艇维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520430977638400', 373, '2025-04-02', '米拉格罗', 'MILAGRO', '215737000', '马耳他', 'SING OCEAN', '张家港海僡国际船务代理有限公司', '13616241033', 225.00, 32.26, 44.00, 11.50, 40298.00, 75085.00, '47265', '大豆', '江海粮油2#', '7万', '进/出', '2025年4月4日/2025年4月7日', '进福南水道落实一艘警戒艇、两艘护航拖轮，出福南水道落实一艘护航拖轮维护，过苏通大桥安排一艘拖轮维护，拖轮三靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431032164352', 374, '2025-04-02', '银河钓鱼岛', 'YIN HE DIAO YU DAO', '414254000', '南京', '南京银河海运有限公司', '江苏嵘海国际物流有限公司', '51258319595', 225.00, 32.20, 41.00, 11.36, 38835.00, 73856.00, '55000', '煤炭', '港务集团8#', '7万', '进/出', '2025年4月4日/2025年4月5日', '福南水道落实一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431111856128', 375, '2025-04-02', '黄玉', 'HAFNIA TOPAZ', '538006762', '马绍尔群岛', 'HAFNIA MIDDLE EAST DMCC', '张家港船务代理有限公司', '0512-58332515', 182.90, 32.20, 38.40, 11.55, 29492.00, 49560.80, '37517.98', '乙二醇/二甘醇等', '长江国际2#', '5万', '进', '2025-04-04', '福南水道落实一艘警戒艇、一艘护航拖轮维护，护航拖轮需在尾部带缆，拖轮三靠三离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431187353600', 376, '2025-04-02', '恒荣兴海', 'HENG RONG XING HAI', '414216000', '宁波', '宁波恒荣世纪海运有限公司', '张家港保税区瑞创国际物流有限公司', '18015528096', 199.99, 32.26, 32.00, 11.00, 31429.00, 51106.00, '50000', '矿砂', '奔辉码头', '5万', '进', '2025-04-05', '靠码头划江前拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431241879552', 377, '2025-04-02', '阿尔法', 'ALPHA PROGRESS', '241208000', '希腊', 'CONTRALTO SHIPHOLDING LTD', '张家港保税区瑞创国际物流有限公司', '0512-58638505', 229.00, 32.25, 44.88, 11.30, 43721.00, 81251.00, '47462', '铁矿', '海力码头4#', '7万', '进/出', '2025年4月6日/2025年4月8日', '进出江落实一艘警戒艇、一艘护航拖轮全维，进出江过苏通大桥派一艘拖轮维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '先靠海力6#，再靠海力4#');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431321571328', 378, '2025-04-03', '中海通26', 'ZHONG HAI TONG 26', '413360050', '南京', '深圳市中海通供应链管理有限公司', '张家港保税区瑞创国际物流有限公司', '18015528096', 162.80, 25.60, 32.00, 10.80, 17057.00, 27350.00, '26000', '矿砂', '海力码头4#', '7万', '进', '2025-04-09', '靠码头划江前拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431384485888', 379, '2025-04-03', '恒荣安海', 'HENG RONG AN HAI', '414217000', '宁波', '宁波恒荣世纪海运有限公司', '张家港保税区瑞创国际物流有限公司', '18015528096', 199.99, 32.26, 34.80, 11.00, 31429.00, 51106.00, '50000', '矿砂', '奔辉码头', '5万', '进', '2025-04-09', '靠码头划江前拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431439011840', 380, '2025-04-03', '宏泰858', 'HONG TAI 858', '414569000', '上海', '上海港禄航运有限公司', '张家港保税区瑞创国际物流有限公司', '18015528096', 199.96, 32.26, 35.00, 12.00, 31774.00, 55050.00, '54000', '矿砂', '奔辉码头', '5万', '进', '2025-04-07', '靠码头划江前拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431489343488', 381, '2025-04-03', '北部湾荣耀', 'BBG HONOR', '477477300', '中国香港', 'TIANJIN YUYUE6 LEASING LIMITED', '张家港船务代理有限公司', '0512-58332515', 228.99, 32.26, 43.60, 11.80, 43007.00, 81917.00, '57671.44', '大豆', '江海粮油2#', '7万', '进/出', '2025年4月9日/2025年4月12日', '进福南水道落实一艘警戒艇、两艘护航拖轮，出福南水道落实一艘护航拖轮维护，过苏通大桥安排一艘拖轮维护，拖轮三靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431539675136', 382, '2025-04-03', '安盛山', 'AN SHENG SHAN', '413184000', '广州', '中远海运散货运输有限公司', '中国张家港外轮代理有限公司', '0512-58335210', 199.99, 32.26, 38.14, 11.36, 33511.00, 57678.00, '47500', '煤炭', '江海粮油3#', '7万', '进', '2025-04-05', '福南水道落实一艘护航拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431585812480', 383, '2025-04-03', '斯坦福', 'NICHOLAS STANFORD', '636020296', '利比里亚', 'CHIJIN SHIPPING S.A.', '张家港金泰国际船舶代理有限公司', '13962213172', 199.90, 32.26, 38.00, 11.50, 35607.00, 63523.00, '42150', '铜精矿', '港务集团1#', '5万', '进', '2025-04-06', '福南水道一艘警戒艇、一艘拖轮维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431636144128', 384, '2025-04-03', '威美亚', 'WAIMEA', '36017496', '利比里亚', 'LS－KBR 8 CO.,LTD.', '张家港金泰国际船舶代理有限公司', '58398881', 187.88, 32.26, 38.00, 11.50, 32795.00, 55395.00, '45000', 'PTA', '港务集团1#', '5万', '出', '2025-04-10', '福南水道一艘警戒艇、一艘拖轮维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431682281472', 385, '2025-04-07', '九济3', 'JIU JI 3', '414355000', '上海', '上海九济航运有限公司', '江苏嵘海国际物流有限公司', '0512-58319595', 224.97, 32.25, 44.00, 11.36, 40605.00, 75230.00, '55000', '煤炭', '港务集团8#', '7万', '进/出', '2025年4月7日/2025年4月8日', '福南水道落实一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '2001.07');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431732613120', 386, '2025-04-07', '米拉格罗', 'MILAGRO', '215737000', '马耳他', 'SING OCEAN', '张家港海僡国际船务代理有限公司', '13616241033', 225.00, 32.26, 44.00, 11.50, 40298.00, 75085.00, '47265', '大豆', '江海粮油2#', '7万', '进/出', '2025年4月10日/2025年4月13日', '进福南水道落实一艘警戒艇、两艘护航拖轮，出福南水道落实一艘护航拖轮维护，过苏通大桥安排一艘拖轮维护，拖轮三靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431778750464', 387, '2025-04-07', '长航威海', 'CHANG HANG WEI HAI', '413568280', '上海', '长航货运有限公司上海分公司', '张家港保税区瑞创国际物流有限公司', '18015528096', 159.95, 27.60, 32.00, 11.90, 21833.00, 35688.00, '35000', '矿砂', '海力码头6#', '12万', '进', '2025-04-08', '靠码头划江前警戒艇、拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431829082112', 388, '2025-04-07', '恒荣兴海', 'HENG RONG XING HAI', '414216000', '宁波', '宁波恒荣世纪海运有限公司', '张家港保税区瑞创国际物流有限公司', '18015528096', 199.99, 32.26, 32.00, 11.00, 31429.00, 51106.00, '50000', '矿砂', '奔辉码头', '5万', '进', '2025-04-10', '靠码头划江前拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431875219456', 389, '2025-04-07', '鹏泰', 'PENG TAI', '414156000', '深圳', '中远海运散货运输有限公司', '中国张家港外轮代理有限公司', '0512-58335210', 215.00, 32.26, 35.50, 10.70, 31893.00, 49836.00, '48000', '煤炭', '沙电一期煤炭码头', '5万', '进/出', '2025年4月11日/2025年4月12日', '靠码头划江前警戒艇、拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431933939712', 390, '2025-04-07', '浙海165', 'ZHE HAI 165', '413348990', '杭州', '浙江省海运集团有限公司', '江苏嵘海国际物流有限公司', '51258319595', 189.98, 32.26, 33.00, 11.80, 28746.00, 48336.00, '45000', '煤炭', '沙电二期煤炭码头', '10万', '进', '2025-04-09', '靠码头划江前拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520431988465664', 391, '2025-04-07', '好运', 'CS FORTUNE', '372918000', '巴拿马', 'CS FORTUNE LTD', '张家港明洋船务代理有限公司', '51235027305', 182.90, 28.39, 40.00, 11.07, 22998.00, 34005.50, '31600', '铁矿', '宏泰码头1#', '7万', '进', '2025-04-09', '进永钢水道一艘警戒艇、一艘护航拖轮维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432051380224', 392, '2025-04-07', '弗里', 'FRIEDRICH OLDENDORFF', '374052000', '巴拿马', 'SUN LANES SHIPPING SA', '张家港明洋船务代理有限公司', '0512-35027305', 299.99, 50.00, 50.00, 11.80, 107450.00, 208822.00, '98000', '铁矿', '盛泰码头3#', '5万', '进/出', '2025年4月11日/2025年4月13日', '进江安排一艘拖轮、一艘警戒艇全程维护；出江安排一艘拖轮、一艘警戒艇全程维护。过永钢航道和苏通大桥安排一条拖轮维护；拖轮四靠四离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432126877696', 393, '2025-04-09', '汉堡', 'TD HAMBURG', '636017645', '利比里亚', 'TRI-DO TWO SHIPPING LIMITED', '张家港金泰国际船舶代理有限公司', '13962213172', 199.90, 32.26, 40.00, 11.20, 36347.00, 63463.00, '43902', '铜精矿', '港务集团8#', '7万', '进', '2025-04-10', '福南水道落实一艘警戒艇、一艘拖轮维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432173015040', 394, '2025-04-09', '国家能源601', 'GUOJIANENGYUAN 601', '414897000', '天津', '国家能源集团航运有限公司', '中国张家港外轮代理有限公司', '0512-58335210', 209.95, 34.80, 35.40, 11.50, 39443.00, 64871.00, '60500', '煤炭', '港务集团9#', '7万', '进/出', '2025年4月11日/2025年4月12日', '福南水道一艘警戒艇、一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432214958080', 395, '2025-04-09', '夜曲', 'NOCTURNE', '538008724', '马绍尔群岛', 'MOL CHEMICAL TANKERS PTE.LTD.', '中国张家港外轮代理有限公司', '13812852060', 180.15, 28.20, 30.00, 11.20, 22988.00, 37245.00, '33355', '乙二醇/二甘醇等', '长江国际2#', '5万', '进', '2025-04-11', '福南水道落实一艘警戒艇、一艘护航拖轮维护，护航拖轮需在尾部带缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432282066944', 396, '2025-04-09', '联合皇冠', 'UNITED CROWN', '353297000', '巴拿马', 'FARWAY SHIPPING SA', '张家港保税区瑞创国际物流有限公司', '0512-58638505', 291.98, 45.00, 52.00, 12.00, 92752.00, 181381.00, '98000', '铁矿', '奔辉码头', '5万', '进/出', '2025年4月11日/2025年4月13日', '进出江落实一艘警戒艇、一艘护航拖轮全维，进出江过苏通大桥派一艘拖轮维护，在港期间安排一艘拖轮值守，拖轮四靠三离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432332398592', 397, '2025-04-09', '联合', 'SG UNITED', '431812000', '日本', 'WODEN MARITIME SA', '张家港保税区瑞创国际物流有限公司', '0512-58638505', 291.98, 45.00, 50.91, 12.00, 92752.00, 181415.00, '97139', '铁矿', '海力码头6#', '12万', '进/出', '2025年4月11日/2025年4月13日', '进出江落实一艘警戒艇、一艘护航拖轮全维，进出江过苏通大桥派一艘拖轮维护，在港期间安排一艘拖轮值守，拖轮四靠三离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432370147328', 398, '2025-04-09', '新一海18', 'XIN YI HAI 18', '414542000', '舟山', '浙江新一海海运有限公司', '江苏嵘海国际物流有限公司', '0512-58319595', 199.90, 34.20, 36.00, 11.00, 31660.00, 50385.00, '50000', '铜精矿', '港务集团8#', '7万', '进', '2025-04-09', '福南水道落实一艘警戒艇、一艘拖轮维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432420478976', 399, '2025-04-09', '银雪', 'YIN XUE', '413386920', '上海', 'HONG GLORY', '张家港海僡国际船务代理有限公司', '13616241033', 189.98, 32.26, 35.00, 10.90, 29644.00, 44945.40, '44000', '钢材', '盛泰码头2#', '5万', '出', '2025-04-11', '永钢水道安排一艘拖轮、一艘警戒艇维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:01', 'system', '2025-04-11 10:29:01', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432470810624', 400, '2025-04-09', '新昌海', 'XIN CHANG HAI', '477103100', '中国香港', 'COSCO', '张家港海僡国际船务代理有限公司', '13616241033', 292.00, 45.00, 50.00, 12.00, 94035.00, 178360.00, '95200', '铁矿', '港务集团5#', '10万', '进/出', '2025年4月17日/2025年4月19日', '一艘警戒艇、一艘护航拖轮进出江全维，过苏通大桥水域加派一艘拖轮，进出江福姜沙加派一艘拖轮维护，拖轮四靠三离，在港期间安排一艘拖轮基地值守。', 'system', '2025-04-11 10:29:02', 'system', '2025-04-11 10:29:02', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432516947968', 401, '2025-04-09', '太平洋日照', 'PACIFIC RIZHAO', '477810400', '中国香港', 'INTERNATIONAL SHIPPING AGENCIES LIMITED.', '中国张家港外轮代理有限公司', '58332535', 226.05, 36.60, 42.00, 10.50, 48419.00, 53999.00, '26896.10', '丙烷', '东华能源', '5万', '进/出', '2025年4月12日/2025年4月14日', '进江安排一艘警戒艇全维，进福南水道安排两艘护航拖轮维护，出福南水道安排一艘护航拖轮，一艘警戒艇维护，拖轮三靠两离。', 'system', '2025-04-11 10:29:02', 'system', '2025-04-11 10:29:02', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432567279616', 402, '2025-04-09', '意和欣88', 'YI HE XIN 88', '413443570', '台州', '浙江意和欣海运有限公司', '江苏嵘海国际物流有限公司', '0512-58319595', 189.90, 27.80, 32.00, 10.70, 21515.00, 35542.00, '35000', '煤炭', '港务集团8#', '7万', '进', '2025-04-11', '福南水道落实一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:02', 'system', '2025-04-11 10:29:02', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432617611264', 403, '2025-04-09', '邦德', 'NGM BOND', '636019157', '利比里亚', 'MAIN SEAWAYS LTD', '张家港明洋船务代理有限公司', '51235027305', 292.00, 45.00, 50.00, 12.00, 93286.00, 182641.00, '93000', '铁矿', '港务集团5#', '10万', '进/出', '2025年4月15日/2025年4月17日', '一艘警戒艇、一艘护航拖轮进出江全维，过苏通大桥水域加派一艘拖轮，进出江福姜沙加派一艘拖轮维护，拖轮四靠三离，在港期间安排一艘拖轮基地值守。', 'system', '2025-04-11 10:29:02', 'system', '2025-04-11 10:29:02', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432684720128', 404, '2025-04-09', '宏泰 588', 'HONG TAI 588', '414545000', '上海', '上海港禄航运有限公司', '上海港禄航运有限公司', '18962296329', 199.96, 32.26, 43.16, 11.36, 31774.00, 55036.00, '51500', '铁矿', '宏泰码头1#', '7万', '进', '2025-04-11', '进永钢水道一艘警戒艇、一艘护航拖轮维护，拖轮两靠两离。', 'system', '2025-04-11 10:29:02', 'system', '2025-04-11 10:29:02', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432756023296', 405, '2025-04-09', '中海昌运2', 'ZHONG HAI CHANG YUN 2', '413692000', '上海', '中远海运散货运输有限公司', '中国张家港外轮代理有限公司', '0512-58335210', 199.99, 32.26, 37.58, 11.80, 33511.00, 57680.00, '50500', '煤炭', '港务集团9#', '7万', '进', '2025-04-13', '福南水道落实一艘警戒艇、一艘拖轮维护，另一艘拖轮船艉中间带拖缆，拖轮两靠两离。', 'system', '2025-04-11 10:29:02', 'system', '2025-04-11 10:29:02', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432818937856', 406, '2025-04-09', '宏泰858', 'HONG TAI 858', '414569000', '上海', '上海港禄航运有限公司', '张家港保税区瑞创国际物流有限公司', '18015528096', 199.96, 32.26, 35.00, 12.00, 31774.00, 55050.00, '54000', '矿砂', '奔辉码头', '5万', '进', '2025-04-12', '靠码头划江前警戒艇、拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:02', 'system', '2025-04-11 10:29:02', '');
INSERT INTO `large_restricted_vessels` VALUES ('1910520432877658112', 407, '2025-04-09', '北仑19', 'BEI LUN 19', '414656000', '宁波', '浦银金融租赁股份有限公司', '张家港保税区瑞创国际物流有限公司', '18015528096', 199.99, 34.50, 35.00, 11.75, 38158.00, 62472.00, '57400', '矿砂', '海力码头5#', '10万', '进', '2025-04-11', '靠码头划江前拖轮落实到位，拖轮两靠两离。', 'system', '2025-04-11 10:29:02', 'system', '2025-04-11 10:29:02', '');

SET FOREIGN_KEY_CHECKS = 1;


package com.jeesite.modules.shipreport.entity;

import javax.validation.constraints.Size;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;

/**
* 船舶报告表Entity
* @author 王浩宇
* @version 2024-06-21
  */
  @Table(name="ship_report", alias="a", label="船舶报告表信息", columns={
  @Column(name="id", attrName="id", label="编号", isPK=true),
  @Column(name="report_id", attrName="reportId", label="报告编号", isQuery=false),
  @Column(name="ship_name_cn", attrName="shipNameCn", label="中文船名", queryType=QueryType.LIKE),
  @Column(name="ship_identification_number", attrName="shipIdentificationNumber", label="船舶识别号", queryType=QueryType.LIKE),
  @Column(name="gross_tonnage", attrName="grossTonnage", label="总吨", isUpdateForce=true),
  @Column(name="deadweight_tonnage", attrName="deadweightTonnage", label="载重吨", isUpdateForce=true),
  @Column(name="main_engine_power", attrName="mainEnginePower", label="主机功率", isUpdateForce=true),
  @Column(name="ship_type", attrName="shipType", label="船舶种类"),
  @Column(name="port_of_registry", attrName="portOfRegistry", label="船籍港"),
  @Column(name="ship_length", attrName="shipLength", label="船舶长度", isUpdateForce=true),
  @Column(name="ship_width", attrName="shipWidth", label="船舶宽度", isUpdateForce=true),
  @Column(name="in_out_port", attrName="inOutPort", label="进出港"),
  @Column(name="sea_river_ship", attrName="seaRiverShip", label="海/河船"),
  @Column(name="reporting_agency", attrName="reportingAgency", label="报告机构"),
  @Column(name="estimated_arrival_departure_time", attrName="estimatedArrivalDepartureTime", label="预抵离时间", isUpdateForce=true),
  @Column(name="report_time", attrName="reportTime", label="报告时间", isUpdateForce=true),
  @Column(name="port", attrName="port", label="港口"),
  @Column(name="berth", attrName="berth", label="泊位"),
  @Column(name="up_down_port", attrName="upDownPort", label="上下港"),
  @Column(name="voyage_daily_report", attrName="voyageDailyReport", label="航次日报"),
  @Column(name="voyage_count", attrName="voyageCount", label="航次数量", isQuery=false, isUpdateForce=true),
  @Column(name="actual_cargo_volume", attrName="actualCargoVolume", label="实载货量", isUpdateForce=true),
  @Column(name="loading_unloading_cargo_volume", attrName="loadingUnloadingCargoVolume", label="装卸货量", isUpdateForce=true),
  @Column(name="actual_vehicle_count", attrName="actualVehicleCount", label="实载车辆", isUpdateForce=true),
  @Column(name="loading_unloading_vehicle_count", attrName="loadingUnloadingVehicleCount", label="装卸车辆", isUpdateForce=true),
  @Column(name="actual_passenger_count", attrName="actualPassengerCount", label="实载客量", isUpdateForce=true),
  @Column(name="up_down_passenger_count", attrName="upDownPassengerCount", label="上下客量", isUpdateForce=true),
  @Column(name="cargo_type", attrName="cargoType", label="货物种类", queryType=QueryType.LIKE),
  @Column(name="cargo_name", attrName="cargoName", label="品名", queryType=QueryType.LIKE),
  @Column(name="mmsi", attrName="mmsi", label="MMSI"),
  @Column(name="draft_fore", attrName="draftFore", label="前吃水", isUpdateForce=true),
  @Column(name="draft_aft", attrName="draftAft", label="后吃水", isUpdateForce=true),
  @Column(name="ship_owner", attrName="shipOwner", label="船舶所有人", queryType=QueryType.LIKE),
  @Column(name="applicant", attrName="applicant", label="申请人", queryType=QueryType.LIKE),
  @Column(name="applicant_contact", attrName="applicantContact", label="申请人联系方式", isQuery=false),
  @Column(name="is_hazardous_cargo", attrName="isHazardousCargo", label="是否危险货物"),
  @Column(name="hazardous_cargo_quantity", attrName="hazardousCargoQuantity", label="危险货物数量", isUpdateForce=true),
  @Column(name="barge_count", attrName="bargeCount", label="驳船数量", isQuery=false, isUpdateForce=true),
  @Column(name="local_barge_operations_count", attrName="localBargeOperationsCount", label="本港加/解驳船数量", isQuery=false, isUpdateForce=true),
  @Column(name="actual_container_volume", attrName="actualContainerVolume", label="实载集装箱运量", isUpdateForce=true),
  @Column(name="local_container_operations_volume", attrName="localContainerOperationsVolume", label="本港装/卸集装箱运量", isUpdateForce=true),
  @Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
  @Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
  @Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
  @Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
  @Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
  }, orderBy="a.update_date DESC"
  )
  public class ShipReport extends DataEntity<ShipReport> {

  private static final long serialVersionUID = 1L;
  private String reportId;		// 报告编号
  private String shipNameCn;		// 中文船名
  private String shipIdentificationNumber;		// 船舶识别号
  private Double grossTonnage;		// 总吨
  private Double deadweightTonnage;		// 载重吨
  private Double mainEnginePower;		// 主机功率
  private String shipType;		// 船舶种类
  private String portOfRegistry;		// 船籍港
  private Double shipLength;		// 船舶长度
  private Double shipWidth;		// 船舶宽度
  private String inOutPort;		// 进出港
  private String seaRiverShip;		// 海/河船
  private String reportingAgency;		// 报告机构
  private Date estimatedArrivalDepartureTime;		// 预抵离时间
  private Date reportTime;		// 报告时间
  private String port;		// 港口
  private String berth;		// 泊位
  private String upDownPort;		// 上下港
  private String voyageDailyReport;		// 航次日报
  private Long voyageCount;		// 航次数量
  private Double actualCargoVolume;		// 实载货量
  private Double loadingUnloadingCargoVolume;		// 装卸货量
  private Long actualVehicleCount;		// 实载车辆
  private Long loadingUnloadingVehicleCount;		// 装卸车辆
  private Long actualPassengerCount;		// 实载客量
  private Long upDownPassengerCount;		// 上下客量
  private String cargoType;		// 货物种类
  private String cargoName;		// 品名
  private String mmsi;		// MMSI
  private Double draftFore;		// 前吃水
  private Double draftAft;		// 后吃水
  private String shipOwner;		// 船舶所有人
  private String applicant;		// 申请人
  private String applicantContact;		// 申请人联系方式
  private String isHazardousCargo;		// 是否危险货物
  private Double hazardousCargoQuantity;		// 危险货物数量
  private Long bargeCount;		// 驳船数量
  private Long localBargeOperationsCount;		// 本港加/解驳船数量
  private Double actualContainerVolume;		// 实载集装箱运量
  private Double localContainerOperationsVolume;		// 本港装/卸集装箱运量

  

}
基于上述数据结构，帮忙编写一个进出港船舶的数据分析展示页面。

上面有一个时间选择，类似相关前端页面的做法。

根据选择的时间，系统自动计算该时间段内的数据。
包括以下数据卡片：该卡片需要计算环比数据。根据当前选择的时间段，去取环比的相应的时间端。
1.进出船舶的数量（按照预抵离时间字段筛选）
2.进港数量（进出港字段包含 “进”关键字）
3.出港数量（进出港字段包含 “出”关键字）
4.内河船舶数量(海/河船字段包含“内河船”关键字)
5.海船数量(海/河船字段包含“海船”关键字)
6.危化品船舶数量(是否危险货物字段为是)
7.80米以下船舶数量（按照船舶长度筛选）
8.80-150米船舶数量（按照船舶长度筛选）
9.150米以上船舶数量（按照船舶长度筛选）
接下来是一些数据图表
这些图表都可以按照进出港，海船/内河船，是否为危险品船舶，船舶长度（三个范围进行选择）进行筛选。默认是全部
1.各海事机构的船舶数量
2.各泊位船舶数量
3.时间趋势图（按照日、周、月、季、年进行展示曲线图） （该趋势图增加海事机构、泊位筛选）海事机构、泊位通过查询报告机构、泊位来查询不同的数据，然后供用户下拉选择。
html页面参照下面的风格设计
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>张家港海事局动态执法数据看板</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${ctxStatic}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome - 添加图标支持 -->
    <link rel="stylesheet" href="${ctxStatic}/nickjs/fontawesome/css/all.min.css">
    <!-- Custom CSS - 科技风格设计 -->
    <style>
        :root {
            --primary-color: #0466c8;
            --secondary-color: #0353a4;
            --accent-color: #023e7d;
            --text-color: #001845;
            --highlight-color: #33a1fd;
            --background-color: #f8f9fa;
            --card-bg: rgba(255, 255, 255, 0.9);
            --positive-color: #38b000;
            --negative-color: #d90429;
            --grid-line: rgba(4, 102, 200, 0.2);
            --chart-colors: #64B5F6, #81C784, #FFB74D, #E57373, #BA68C8, #4DD0E1;
        }

        body {
            background-color: var(--background-color);
            background-image: linear-gradient(to bottom, rgba(4, 102, 200, 0.05), rgba(1, 22, 56, 0.1));
            color: var(--text-color);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            font-size: 16px;
        }

        .main-header {
            background-color: var(--primary-color);
            color: white;
            padding: 20px 0;
            margin-bottom: 30px;
            border-bottom: 3px solid var(--accent-color);
            position: relative;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .main-title {
            text-align: center;
            font-size: 28px;
            font-weight: 600;
            margin: 0;
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3);
        }

        .chart-container {
            height: 400px;
            margin: 20px 0 30px;
            border-radius: 8px;
            background-color: var(--card-bg);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            padding: 15px;
        }

        .data-card {
            border: 1px solid rgba(4, 102, 200, 0.2);
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            text-align: center;
            background-color: var(--card-bg);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }

        .data-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        }

        .data-card .label {
            color: var(--accent-color);
            font-weight: 600;
            font-size: 1.1rem;
            padding: 0;
            display: block;
            margin-bottom: 5px;
        }

        .data-card .value {
            font-size: 2.5em;
            font-weight: 700;
            color: var(--primary-color);
            margin: 10px 0;
        }

        .data-card .change-rate {
            font-size: 1.1em;
            margin-top: 5px;
            font-weight: 600;
        }

        .change-rate.positive {
            color: var(--positive-color);
        }

        .change-rate.negative {
            color: var(--negative-color);
        }

        .change-rate.neutral {
            color: #607d8b;
        }

        .custom-date-input {
            margin-bottom: 25px;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: var(--card-bg);
            border-radius: 8px;
            padding: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        .custom-date-input input {
            margin: 0 15px;
            border-radius: 4px;
            border: 1px solid var(--grid-line);
            font-size: 1.1rem;
        }

        .btn-primary {
            background-color: var(--primary-color);
            border-color: var(--secondary-color);
            font-weight: 600;
            padding: 8px 20px;
            transition: all 0.3s ease;
            font-size: 1.1rem;
        }

        .btn-primary:hover {
            background-color: var(--accent-color);
            border-color: var(--accent-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        /* 表格样式优化 */
        .table-container {
            position: relative;
            overflow-x: auto;
            width: 100%;
            margin: 20px auto 40px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            background-color: var(--card-bg);
        }

        .summary-table {
            margin: 0;
            width: 100%;
            background-color: var(--card-bg);
            font-size: 0.95rem;
        }

        .summary-table th {
            text-align: center;
            vertical-align: middle;
            padding: 8px 4px;
            white-space: nowrap;
            background-color: var(--primary-color);
            color: white;
            border: none;
        }

        .summary-table th[colspan="6"] {
            background-color: var(--secondary-color);
        }

        .summary-table td {
            text-align: center;
            vertical-align: middle;
            border-color: var(--grid-line);
            padding: 6px 4px;
            font-size: 1rem;
            white-space: nowrap;
            transition: background-color 0.3s ease;
        }

        .summary-table tbody tr:hover td {
            background-color: rgba(4, 102, 200, 0.08);
        }

        .summary-table th:first-child,
        .summary-table td:first-child {
            position: sticky;
            left: 0;
            z-index: 1;
            box-shadow: 2px 0 5px -2px rgba(0, 0, 0, 0.1);
        }

        .summary-table th:first-child {
            background-color: var(--primary-color);
        }

        .summary-table td:first-child {
            background-color: var(--card-bg);
        }

        .summary-table tr:hover td:first-child {
            background-color: rgba(4, 102, 200, 0.08);
        }

        .table-section-title {
            margin-top: 40px;
            margin-bottom: 20px;
            font-size: 24px;
            font-weight: 600;
            text-align: center;
            color: var(--primary-color);
            position: relative;
            padding-bottom: 10px;
        }

        .table-section-title:after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 3px;
            background-color: var(--highlight-color);
        }

        .positive-change {
            color: var(--positive-color);
            font-weight: 600;
        }

        .negative-change {
            color: var(--negative-color);
            font-weight: 600;
        }

        .neutral-change {
            color: #607d8b;
        }

        .table-active {
            background-color: rgba(4, 102, 200, 0.08) !important;
        }

        /* 科技风格图标 */
        .icon-box {
            margin-right: 8px;
            color: var(--primary-color);
        }

        /* 科技风格图表区域 */
        .charts-section {
            background-color: rgba(248, 249, 250, 0.5);
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            margin-bottom: 30px;
        }

        .charts-title {
            color: var(--primary-color);
            font-size: 1.4rem;
            font-weight: 600;
            margin-bottom: 30px;
            text-align: center;
        }

        /* 数据分类导航 */
        .data-nav {
            margin: 20px 0;
            text-align: center;
        }

        .data-nav .nav-link {
            color: var(--text-color);
            font-weight: 600;
            padding: 10px 20px;
            border-radius: 8px;
            margin: 0 5px;
            transition: all 0.3s ease;
        }

        .data-nav .nav-link:hover,
        .data-nav .nav-link.active {
            background-color: var(--primary-color);
            color: white;
        }

        /* 调整日期选择器的响应式布局 */
        @media (max-width: 768px) {
            .custom-date-input {
                flex-direction: column;
                align-items: stretch;
            }

            .custom-date-input input {
                margin: 10px 0;
                width: 100%;
            }

            .btn-primary {
                width: 100%;
                margin-top: 10px;
            }

            .chart-container {
                height: 300px;
            }
        }
    </style>
    <!-- jQuery 和 ECharts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
</head>

<body>
<div class="main-header">
    <h2 class="main-title"><i class="fas fa-ship icon-box"></i>张家港海事局动态执法数据看板</h2>
</div>

<div class="container-fluid">
    <div class="custom-date-input">
        <label for="reportDate" style="margin-right: 10px; line-height: 38px; font-weight: 600; font-size: 1.1rem;">
            <i class="far fa-calendar-alt icon-box"></i>选择日期：
        </label>
        <input type="date" id="reportDate" class="form-control" style="width: auto;">
        <button type="button" id="fetchDataButton" class="btn btn-primary">
            <i class="fas fa-sync-alt"></i> 拉取数据
        </button>
    </div>

    <!-- 增加统计卡片区域 -->
    <div class="row">
        <div class="col-md-4">
            <div class="data-card">
                <div class="label"><i class="fas fa-ship icon-box"></i>海巡艇巡航发现异常</div>
                <div class="value" id="patrol-boat-abnormal-value">0</div>
                <div class="change-rate" id="patrol-boat-abnormal-rate">0%</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="data-card">
                <div class="label"><i class="fas fa-gavel icon-box"></i>海巡艇巡航立案调查</div>
                <div class="value" id="patrol-boat-investigation-value">0</div>
                <div class="change-rate" id="patrol-boat-investigation-rate">0%</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="data-card">
                <div class="label"><i class="fas fa-desktop icon-box"></i>电子巡航发现异常</div>
                <div class="value" id="electronic-patrol-abnormal-value">0</div>
                <div class="change-rate" id="electronic-patrol-abnormal-rate">0%</div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4">
            <div class="data-card">
                <div class="label"><i class="fas fa-search icon-box"></i>电子巡航立案调查</div>
                <div class="value" id="electronic-patrol-investigation-value">0</div>
                <div class="change-rate" id="electronic-patrol-investigation-rate">0%</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="data-card">
                <div class="label"><i class="fas fa-drone icon-box"></i>无人机巡航发现异常</div>
                <div class="value" id="drone-patrol-abnormal-value">0</div>
                <div class="change-rate" id="drone-patrol-abnormal-rate">0%</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="data-card">
                <div class="label"><i class="fas fa-clipboard-list icon-box"></i>无人机巡航立案调查</div>
                <div class="value" id="drone-patrol-investigation-value">0</div>
                <div class="change-rate" id="drone-patrol-investigation-rate">0%</div>
            </div>
        </div>
    </div>

    <!-- 将三个表格合并为一个表格 -->
    <div class="row">
        <div class="col-12">
            <h4 class="table-section-title"><i class="fas fa-table icon-box"></i>巡航执法数据统计表</h4>
            <div class="table-container">
                <table class="table table-bordered table-striped summary-table">
                    <thead>
                    <tr>
                        <th rowspan="3">部门</th>
                        <th colspan="6">海巡艇巡航</th>
                        <th colspan="6">电子巡航</th>
                        <th colspan="6">无人机巡航</th>
                    </tr>
                    <tr>
                        <th colspan="3">发现异常</th>
                        <th colspan="3">立案调查</th>
                        <th colspan="3">发现异常</th>
                        <th colspan="3">立案调查</th>
                        <th colspan="3">发现异常</th>
                        <th colspan="3">立案调查</th>
                    </tr>
                    <tr>
                        <th>上周</th>
                        <th>本周</th>
                        <th>环比</th>
                        <th>上周</th>
                        <th>本周</th>
                        <th>环比</th>
                        <th>上周</th>
                        <th>本周</th>
                        <th>环比</th>
                        <th>上周</th>
                        <th>本周</th>
                        <th>环比</th>
                        <th>上周</th>
                        <th>本周</th>
                        <th>环比</th>
                        <th>上周</th>
                        <th>本周</th>
                        <th>环比</th>
                    </tr>
                    </thead>
                    <tbody id="patrol-data-body">
                    <!-- 数据将通过JavaScript动态填充 -->
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="charts-section">
        <h5 class="charts-title"><i class="fas fa-ship icon-box"></i>海巡艇巡航发现异常统计</h5>
        <div class="row">
            <div class="col-md-12">
                <div id="patrol-boat-abnormal-chart" class="chart-container"></div>
            </div>
        </div>
    </div>

    <div class="charts-section">
        <h5 class="charts-title"><i class="fas fa-gavel icon-box"></i>海巡艇巡航立案调查统计</h5>
        <div class="row">
            <div class="col-md-12">
                <div id="patrol-boat-investigation-chart" class="chart-container"></div>
            </div>
        </div>
    </div>

    <div class="charts-section">
        <h5 class="charts-title"><i class="fas fa-desktop icon-box"></i>电子巡航发现异常统计</h5>
        <div class="row">
            <div class="col-md-12">
                <div id="electronic-patrol-abnormal-chart" class="chart-container"></div>
            </div>
        </div>
    </div>

    <div class="charts-section">
        <h5 class="charts-title"><i class="fas fa-search icon-box"></i>电子巡航立案调查统计</h5>
        <div class="row">
            <div class="col-md-12">
                <div id="electronic-patrol-investigation-chart" class="chart-container"></div>
            </div>
        </div>
    </div>

    <div class="charts-section">
        <h5 class="charts-title"><i class="fas fa-drone icon-box"></i>无人机巡航发现异常统计</h5>
        <div class="row">
            <div class="col-md-12">
                <div id="drone-patrol-abnormal-chart" class="chart-container"></div>
            </div>
        </div>
    </div>

    <div class="charts-section">
        <h5 class="charts-title"><i class="fas fa-clipboard-list icon-box"></i>无人机巡航立案调查统计</h5>
        <div class="row">
            <div class="col-md-12">
                <div id="drone-patrol-investigation-chart" class="chart-container"></div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS (Popper.js and Bootstrap's JS are required) -->
<script src="${ctxStatic}/nickjs/popper.min.js"></script>
<script src="${ctxStatic}/bootstrap/js/bootstrap.min.js"></script>
<!-- SweetAlert2 为拉取数据时的消息提示 -->
<script src="${ctxStatic}/nickjs/sweetalert2@11"></script>

<script>
    $(document).ready(function() {
        // 初始化 ECharts 实例
        var patrolBoatAbnormalChart = echarts.init(document.getElementById('patrol-boat-abnormal-chart'));
        var patrolBoatInvestigationChart = echarts.init(document.getElementById('patrol-boat-investigation-chart'));
        var electronicPatrolAbnormalChart = echarts.init(document.getElementById('electronic-patrol-abnormal-chart'));
        var electronicPatrolInvestigationChart = echarts.init(document.getElementById('electronic-patrol-investigation-chart'));
        var dronePatrolAbnormalChart = echarts.init(document.getElementById('drone-patrol-abnormal-chart'));
        var dronePatrolInvestigationChart = echarts.init(document.getElementById('drone-patrol-investigation-chart'));

        // 获取日期选择器和拉取数据按钮
        var dateInput = $('#reportDate');
        var fetchDataButton = $('#fetchDataButton');

        // 设置默认日期为今天
        dateInput.val(new Date().toISOString().slice(0, 10));

        // 初始化图表
        fetchDataAndRenderCharts();

        // 日期改变事件
        dateInput.on('change', function() {
            fetchDataAndRenderCharts();
        });

        // 拉取数据按钮点击事件
        fetchDataButton.on('click', function() {
            // 添加加载动画
            $(this).html('<i class="fas fa-spinner fa-spin"></i> 数据加载中...');
            $(this).prop('disabled', true);

            fetchData().then(() => {
                // 恢复按钮状态
                $(this).html('<i class="fas fa-sync-alt"></i> 拉取数据');
                $(this).prop('disabled', false);
            });
        });

        function fetchData() {
            return new Promise((resolve) => {
                // 获取当前选择的日期
                var selectedDate = dateInput.val();
                var currentDate = new Date(selectedDate);
                var currentDayOfWeek = currentDate.getDay();

                // 计算本周和上周的开始日期
                var currentWeekStartDate = new Date(currentDate);
                currentWeekStartDate.setDate(currentDate.getDate() - ((currentDayOfWeek + 2) % 7));

                var lastWeekStartDate = new Date(currentWeekStartDate);
                lastWeekStartDate.setDate(currentWeekStartDate.getDate() - 7);

                // 构建请求参数
                var params = {
                    currentWeekStartDate: currentWeekStartDate.toISOString().slice(0, 10),
                    lastWeekStartDate: lastWeekStartDate.toISOString().slice(0, 10),
                }

                // 发送 AJAX 请求获取数据
                $.ajax({
                    url: 'fetchData',
                    type: 'GET',
                    dataType: 'json',
                    data: params,
                    success: function(data) {
                        Swal.fire({
                            title: '成功!',
                            text: '数据拉取成功',
                            icon: 'success',
                            confirmButtonText: '确定'
                        });
                        renderCharts(data);
                        renderDataCards(data);
                        renderDataTables(data);
                        resolve();
                    },
                    error: function(error){
                        console.error('Error fetching chart data:', error);
                        Swal.fire({
                            title: '错误!',
                            text: '获取数据失败，请查看控制台错误信息。',
                            icon: 'error',
                            confirmButtonText: '确定'
                        });
                        resolve();
                    }
                });
            });
        }

        function fetchDataAndRenderCharts() {
            // 获取当前选择的日期
            var selectedDate = dateInput.val();

            // 计算本周和上周的开始日期
            var currentDate = new Date(selectedDate);
            var currentDayOfWeek = currentDate.getDay();
            var currentWeekStartDate = new Date(currentDate);
            currentWeekStartDate.setDate(currentDate.getDate() - ((currentDayOfWeek + 2) % 7));

            var lastWeekStartDate = new Date(currentWeekStartDate);
            lastWeekStartDate.setDate(currentWeekStartDate.getDate() - 7);

            // 构建请求参数
            var params = {
                currentWeekStartDate: currentWeekStartDate.toISOString().slice(0, 10),
                lastWeekStartDate: lastWeekStartDate.toISOString().slice(0, 10),
            }

            // 计算结束日期
            var currentWeekEndDate = new Date(currentWeekStartDate.getTime() + 6 * 24 * 60 * 60 * 1000);
            var formattedStartDate = currentWeekStartDate.toISOString().slice(0, 10);
            var formattedEndDate = currentWeekEndDate.toISOString().slice(0, 10);

            // 更新标题
            var titleText = "张家港海事局动态执法数据看板 ("+formattedStartDate+" 至 "+ formattedEndDate+")";
            $('.main-title').html('<i class="fas fa-ship icon-box"></i>' + titleText);

            // 发送 AJAX 请求获取数据
            $.ajax({
                url: 'chartDataWithDate', // 使用原始URL
                type: 'GET',
                dataType: 'json',
                data: params,
                success: function(data) {
                    renderCharts(data);
                    renderDataCards(data);
                    renderDataTables(data);
                },
                error: function(error) {
                    console.error('Error fetching chart data:', error);
                    Swal.fire({
                        title: '错误!',
                        text: '获取数据失败，请查看控制台错误信息。',
                        icon: 'error',
                        confirmButtonText: '确定'
                    });
                }
            });
        }

        // 渲染数据卡片
        function renderDataCards(data) {
            // 计算总数
            function calculateTotal(dataArray) {
                if (!dataArray) return 0;
                if (Array.isArray(dataArray)) {
                    return dataArray.reduce((sum, item) => sum + (typeof item === 'number' ? item : (item?.value || 0)), 0);
                }
                return 0;
            }

            // 计算变化率
            function calculateChangeRate(current, previous) {
                if (previous === 0) return current > 0 ? 100 : 0;
                return ((current - previous) / previous) * 100;
            }

            // 更新卡片值和变化率
            function updateCard(valueId, rateId, currentData, lastData) {
                const currentTotal = calculateTotal(currentData);
                const lastTotal = calculateTotal(lastData);
                const changeRate = calculateChangeRate(currentTotal, lastTotal);

                $("#" + valueId).text(currentTotal);

                const rateText = (changeRate > 0 ? "+" : "") + changeRate.toFixed(2) + "%";
                const rateElement = $("#" + rateId);

                rateElement.text(rateText);

                if (changeRate > 0) {
                    rateElement.removeClass('negative neutral').addClass('positive');
                } else if (changeRate < 0) {
                    rateElement.removeClass('positive neutral').addClass('negative');
                } else {
                    rateElement.removeClass('positive negative').addClass('neutral');
                }
            }

            // 更新各项数据卡片
            updateCard(
                "patrol-boat-abnormal-value",
                "patrol-boat-abnormal-rate",
                data.patrolBoatAbnormalDiscoveryCurrent,
                data.patrolBoatAbnormalDiscoveryLast
            );

            updateCard(
                "patrol-boat-investigation-value",
                "patrol-boat-investigation-rate",
                data.patrolBoatInvestigationCasesCurrent,
                data.patrolBoatInvestigationCasesLast
            );

            updateCard(
                "electronic-patrol-abnormal-value",
                "electronic-patrol-abnormal-rate",
                data.electronicPatrolAbnormalDiscoveryCurrent,
                data.electronicPatrolAbnormalDiscoveryLast
            );

            updateCard(
                "electronic-patrol-investigation-value",
                "electronic-patrol-investigation-rate",
                data.electronicPatrolInvestigationCasesCurrent,
                data.electronicPatrolInvestigationCasesLast
            );

            updateCard(
                "drone-patrol-abnormal-value",
                "drone-patrol-abnormal-rate",
                data.droneAbnormalDiscoveryCurrent,
                data.droneAbnormalDiscoveryLast
            );

            updateCard(
                "drone-patrol-investigation-value",
                "drone-patrol-investigation-rate",
                data.droneInvestigationCasesCurrent,
                data.droneInvestigationCasesLast
            );
        }

        // 渲染数据表格 - 使用合并表格
        function renderDataTables(data) {
            renderCombinedPatrolTable(data);
        }

        // 修复渲染合并的巡航数据表格函数
        function renderCombinedPatrolTable(data) {
            const tableBody = $('#patrol-data-body');
            tableBody.empty();

// 获取部门列表
            const departments = data.categories || [];

// 为每个部门创建行 - 改用传统字符串连接
            departments.forEach(function(department, index) {
                const row = $('<tr></tr>');

// 添加部门名称 - 改用传统字符串连接
                row.append('<td>' + department + '</td>');

// 1. 海巡艇发现异常数据
                const patrolBoatAbnormalLastValue = data.patrolBoatAbnormalDiscoveryLast ? data.patrolBoatAbnormalDiscoveryLast[index] || 0 : 0;
                const patrolBoatAbnormalCurrentValue = data.patrolBoatAbnormalDiscoveryCurrent ?
                    (typeof data.patrolBoatAbnormalDiscoveryCurrent[index] === 'object' ?
                        data.patrolBoatAbnormalDiscoveryCurrent[index]?.value || 0 :
                        data.patrolBoatAbnormalDiscoveryCurrent[index] || 0) : 0;

                const patrolBoatAbnormalChangeRate = calculateChangeRate(patrolBoatAbnormalCurrentValue, patrolBoatAbnormalLastValue);

// 改用传统字符串连接
                row.append('<td>' + patrolBoatAbnormalLastValue + '</td>');
                row.append('<td>' + patrolBoatAbnormalCurrentValue + '</td>');
                row.append('<td class="' + getRateClass(patrolBoatAbnormalChangeRate) + '">' + formatRate(patrolBoatAbnormalChangeRate) + '</td>');

// 2. 海巡艇立案调查数据
                const patrolBoatInvestigationLastValue = data.patrolBoatInvestigationCasesLast ? data.patrolBoatInvestigationCasesLast[index] || 0 : 0;
                const patrolBoatInvestigationCurrentValue = data.patrolBoatInvestigationCasesCurrent ?
                    (typeof data.patrolBoatInvestigationCasesCurrent[index] === 'object' ?
                        data.patrolBoatInvestigationCasesCurrent[index]?.value || 0 :
                        data.patrolBoatInvestigationCasesCurrent[index] || 0) : 0;

                const patrolBoatInvestigationChangeRate = calculateChangeRate(patrolBoatInvestigationCurrentValue, patrolBoatInvestigationLastValue);

// 改用传统字符串连接
                row.append('<td>' + patrolBoatInvestigationLastValue + '</td>');
                row.append('<td>' + patrolBoatInvestigationCurrentValue + '</td>');
                row.append('<td class="' + getRateClass(patrolBoatInvestigationChangeRate) + '">' + formatRate(patrolBoatInvestigationChangeRate) + '</td>');

// 3. 电子巡航发现异常数据
                const electronicAbnormalLastValue = data.electronicPatrolAbnormalDiscoveryLast ? data.electronicPatrolAbnormalDiscoveryLast[index] || 0 : 0;
                const electronicAbnormalCurrentValue = data.electronicPatrolAbnormalDiscoveryCurrent ?
                    (typeof data.electronicPatrolAbnormalDiscoveryCurrent[index] === 'object' ?
                        data.electronicPatrolAbnormalDiscoveryCurrent[index]?.value || 0 :
                        data.electronicPatrolAbnormalDiscoveryCurrent[index] || 0) : 0;

                const electronicAbnormalChangeRate = calculateChangeRate(electronicAbnormalCurrentValue, electronicAbnormalLastValue);

// 改用传统字符串连接
                row.append('<td>' + electronicAbnormalLastValue + '</td>');
                row.append('<td>' + electronicAbnormalCurrentValue + '</td>');
                row.append('<td class="' + getRateClass(electronicAbnormalChangeRate) + '">' + formatRate(electronicAbnormalChangeRate) + '</td>');

// 4. 电子巡航立案调查数据
                const electronicInvestigationLastValue = data.electronicPatrolInvestigationCasesLast ? data.electronicPatrolInvestigationCasesLast[index] || 0 : 0;
                const electronicInvestigationCurrentValue = data.electronicPatrolInvestigationCasesCurrent ?
                    (typeof data.electronicPatrolInvestigationCasesCurrent[index] === 'object' ?
                        data.electronicPatrolInvestigationCasesCurrent[index]?.value || 0 :
                        data.electronicPatrolInvestigationCasesCurrent[index] || 0) : 0;

                const electronicInvestigationChangeRate = calculateChangeRate(electronicInvestigationCurrentValue, electronicInvestigationLastValue);

// 改用传统字符串连接
                row.append('<td>' + electronicInvestigationLastValue + '</td>');
                row.append('<td>' + electronicInvestigationCurrentValue + '</td>');
                row.append('<td class="' + getRateClass(electronicInvestigationChangeRate) + '">' + formatRate(electronicInvestigationChangeRate) + '</td>');

// 5. 无人机巡航发现异常数据
                const droneAbnormalLastValue = data.droneAbnormalDiscoveryLast ? data.droneAbnormalDiscoveryLast[index] || 0 : 0;
                const droneAbnormalCurrentValue = data.droneAbnormalDiscoveryCurrent ?
                    (typeof data.droneAbnormalDiscoveryCurrent[index] === 'object' ?
                        data.droneAbnormalDiscoveryCurrent[index]?.value || 0 :
                        data.droneAbnormalDiscoveryCurrent[index] || 0) : 0;

                const droneAbnormalChangeRate = calculateChangeRate(droneAbnormalCurrentValue, droneAbnormalLastValue);

// 改用传统字符串连接
                row.append('<td>' + droneAbnormalLastValue + '</td>');
                row.append('<td>' + droneAbnormalCurrentValue + '</td>');
                row.append('<td class="' + getRateClass(droneAbnormalChangeRate) + '">' + formatRate(droneAbnormalChangeRate) + '</td>');

// 6. 无人机巡航立案调查数据
                const droneInvestigationLastValue = data.droneInvestigationCasesLast ? data.droneInvestigationCasesLast[index] || 0 : 0;
                const droneInvestigationCurrentValue = data.droneInvestigationCasesCurrent ?
                    (typeof data.droneInvestigationCasesCurrent[index] === 'object' ?
                        data.droneInvestigationCasesCurrent[index]?.value || 0 :
                        data.droneInvestigationCasesCurrent[index] || 0) : 0;

                const droneInvestigationChangeRate = calculateChangeRate(droneInvestigationCurrentValue, droneInvestigationLastValue);

// 改用传统字符串连接
                row.append('<td>' + droneInvestigationLastValue + '</td>');
                row.append('<td>' + droneInvestigationCurrentValue + '</td>');
                row.append('<td class="' + getRateClass(droneInvestigationChangeRate) + '">' + formatRate(droneInvestigationChangeRate) + '</td>');

                tableBody.append(row);
            });

// 添加合计行
            addCombinedPatrolTotalRow(tableBody, data);
        }

// 添加合并的巡航数据合计行 - 同样修改为传统字符串连接
        function addCombinedPatrolTotalRow(tableBody, data) {
// 计算总计 - 保持不变
            function sumArray(arr) {
                if (!arr) return 0;
                return arr.reduce((sum, item) => {
                    if (typeof item === 'object' && item) {
                        return sum + (item.value || 0);
                    }
                    return sum + (typeof item === 'number' ? item : 0);
                }, 0);
            }

// 计算各项总数
// 1. 海巡艇数据
            const patrolBoatAbnormalLastTotal = sumArray(data.patrolBoatAbnormalDiscoveryLast);
            const patrolBoatAbnormalCurrentTotal = sumArray(data.patrolBoatAbnormalDiscoveryCurrent);
            const patrolBoatInvestigationLastTotal = sumArray(data.patrolBoatInvestigationCasesLast);
            const patrolBoatInvestigationCurrentTotal = sumArray(data.patrolBoatInvestigationCasesCurrent);

// 2. 电子巡航数据
            const electronicAbnormalLastTotal = sumArray(data.electronicPatrolAbnormalDiscoveryLast);
            const electronicAbnormalCurrentTotal = sumArray(data.electronicPatrolAbnormalDiscoveryCurrent);
            const electronicInvestigationLastTotal = sumArray(data.electronicPatrolInvestigationCasesLast);
            const electronicInvestigationCurrentTotal = sumArray(data.electronicPatrolInvestigationCasesCurrent);

// 3. 无人机巡航数据
            const droneAbnormalLastTotal = sumArray(data.droneAbnormalDiscoveryLast);
            const droneAbnormalCurrentTotal = sumArray(data.droneAbnormalDiscoveryCurrent);
            const droneInvestigationLastTotal = sumArray(data.droneInvestigationCasesLast);
            const droneInvestigationCurrentTotal = sumArray(data.droneInvestigationCasesCurrent);

// 计算变化率
            const patrolBoatAbnormalChangeRate = calculateChangeRate(patrolBoatAbnormalCurrentTotal, patrolBoatAbnormalLastTotal);
            const patrolBoatInvestigationChangeRate = calculateChangeRate(patrolBoatInvestigationCurrentTotal, patrolBoatInvestigationLastTotal);
            const electronicAbnormalChangeRate = calculateChangeRate(electronicAbnormalCurrentTotal, electronicAbnormalLastTotal);
            const electronicInvestigationChangeRate = calculateChangeRate(electronicInvestigationCurrentTotal, electronicInvestigationLastTotal);
            const droneAbnormalChangeRate = calculateChangeRate(droneAbnormalCurrentTotal, droneAbnormalLastTotal);
            const droneInvestigationChangeRate = calculateChangeRate(droneInvestigationCurrentTotal, droneInvestigationLastTotal);

// 创建合计行
            const totalRow = $('<tr class="table-active"></tr>');
            totalRow.append('<td><strong>合计</strong></td>');

// 1. 添加海巡艇发现异常总计 - 改用传统字符串连接
            totalRow.append('<td><strong>' + patrolBoatAbnormalLastTotal + '</strong></td>');
            totalRow.append('<td><strong>' + patrolBoatAbnormalCurrentTotal + '</strong></td>');
            totalRow.append('<td class="' + getRateClass(patrolBoatAbnormalChangeRate) + '"><strong>' + formatRate(patrolBoatAbnormalChangeRate) + '</strong></td>');

// 2. 添加海巡艇立案调查总计 - 改用传统字符串连接
            totalRow.append('<td><strong>' + patrolBoatInvestigationLastTotal + '</strong></td>');
            totalRow.append('<td><strong>' + patrolBoatInvestigationCurrentTotal + '</strong></td>');
            totalRow.append('<td class="' + getRateClass(patrolBoatInvestigationChangeRate) + '"><strong>' + formatRate(patrolBoatInvestigationChangeRate) + '</strong></td>');

// 3. 添加电子巡航发现异常总计 - 改用传统字符串连接
            totalRow.append('<td><strong>' + electronicAbnormalLastTotal + '</strong></td>');
            totalRow.append('<td><strong>' + electronicAbnormalCurrentTotal + '</strong></td>');
            totalRow.append('<td class="' + getRateClass(electronicAbnormalChangeRate) + '"><strong>' + formatRate(electronicAbnormalChangeRate) + '</strong></td>');

// 4. 添加电子巡航立案调查总计 - 改用传统字符串连接
            totalRow.append('<td><strong>' + electronicInvestigationLastTotal + '</strong></td>');
            totalRow.append('<td><strong>' + electronicInvestigationCurrentTotal + '</strong></td>');
            totalRow.append('<td class="' + getRateClass(electronicInvestigationChangeRate) + '"><strong>' + formatRate(electronicInvestigationChangeRate) + '</strong></td>');

// 5. 添加无人机巡航发现异常总计 - 改用传统字符串连接
            totalRow.append('<td><strong>' + droneAbnormalLastTotal + '</strong></td>');
            totalRow.append('<td><strong>' + droneAbnormalCurrentTotal + '</strong></td>');
            totalRow.append('<td class="' + getRateClass(droneAbnormalChangeRate) + '"><strong>' + formatRate(droneAbnormalChangeRate) + '</strong></td>');

// 6. 添加无人机巡航立案调查总计 - 改用传统字符串连接
            totalRow.append('<td><strong>' + droneInvestigationLastTotal + '</strong></td>');
            totalRow.append('<td><strong>' + droneInvestigationCurrentTotal + '</strong></td>');
            totalRow.append('<td class="' + getRateClass(droneInvestigationChangeRate) + '"><strong>' + formatRate(droneInvestigationChangeRate) + '</strong></td>');

            tableBody.append(totalRow);
        }


        // 计算变化率
        function calculateChangeRate(currentValue, lastValue) {
            if (lastValue === 0) return currentValue > 0 ? 100 : 0;
            return ((currentValue - lastValue) / lastValue) * 100;
        }

        // 格式化变化率显示
        function formatRate(rate) {
            if (rate === 0) return "0%";
            return (rate > 0 ? "+" : "") + rate.toFixed(2) + "%";
        }

        // 获取变化率的CSS类
        function getRateClass(rate) {
            if (rate > 0) return "positive-change";
            if (rate < 0) return "negative-change";
            return "neutral-change";
        }

        // 渲染图表
        function renderCharts(data) {
            // 图表共用的基础配置
            const baseOption = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    },
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    borderColor: '#0466c8',
                    borderWidth: 1,
                    textStyle: {
                        color: '#333',
                        fontSize: 14
                    },
                    formatter: function(params) {
                        let tooltipText = params[0].name;
                        params.forEach(function(item) {
                            tooltipText += "<br/>" + item.seriesName + ": " + item.value;
                            if (item.data && item.data.changeRate !== undefined) {
                                tooltipText += " (" + (item.data.changeRate > 0 ? "+" : "") + item.data.changeRate.toFixed(2) + "%)";
                            }
                        });
                        return tooltipText;
                    }
                },
                legend: {
                    data: ['上周', '本周'],
                    textStyle: {
                        color: '#0466c8',
                        fontSize: 14
                    },
                    top: 30,
                    left: 'center'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '15%',
                    top: 80,
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    data: data.categories || [],
                    axisLabel: {
                        rotate: 30,
                        interval: 0,
                        color: '#001845',
                        fontSize: 12
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#0466c8'
                        }
                    }
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        fontSize: 12
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#0466c8'
                        }
                    },
                    splitLine: {
                        lineStyle: {
                            color: 'rgba(4, 102, 200, 0.1)'
                        }
                    }
                }
            };

            // 图表配置生成函数
            function createChartOption(titleText, currentData, lastData, chartColor) {
                const option = JSON.parse(JSON.stringify(baseOption)); // 深拷贝

                option.title = {
                    text: titleText,
                    textStyle: {
                        color: '#0466c8',
                        fontWeight: 'bold',
                        fontSize: 16
                    },
                    left: 'center',
                    top: 10
                };

                option.series = [
                    {
                        name: '上周',
                        type: 'bar',
                        data: lastData || [],
                        label: {
                            show: true,
                            position: 'top',
                            color: '#67C23A',
                            fontSize: 12
                        },
                        itemStyle: {
                            color: {
                                type: 'linear',
                                x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [{
                                    offset: 0, color: '#67C23A'
                                }, {
                                    offset: 1, color: '#95d475'
                                }]
                            },
                            borderRadius: [3, 3, 0, 0]
                        }
                    },
                    {
                        name: '本周',
                        type: 'bar',
                        data: currentData || [],
                        label: {
                            show: true,
                            position: 'top',
                            color: chartColor,
                            fontSize: 12,
                            formatter: function(params) {
                                if (params.data && params.data.changeRate !== undefined) {
                                    return params.value + " (" + (params.data.changeRate > 0 ? "+" : "") + params.data.changeRate.toFixed(2) + "%)";
                                }
                                return params.value;
                            }
                        },
                        itemStyle: {
                            color: {
                                type: 'linear',
                                x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [{
                                    offset: 0, color: chartColor
                                }, {
                                    offset: 1, color: chartColor.replace(')', ', 0.7)')
                                }]
                            },
                            borderRadius: [3, 3, 0, 0]
                        }
                    }
                ];

                return option;
            }

            // 设置各图表的选项
            patrolBoatAbnormalChart.setOption(createChartOption(
                '海巡艇巡航发现异常',
                data.patrolBoatAbnormalDiscoveryCurrent,
                data.patrolBoatAbnormalDiscoveryLast,
                '#64B5F6'
            ));

            patrolBoatInvestigationChart.setOption(createChartOption(
                '海巡艇巡航立案调查',
                data.patrolBoatInvestigationCasesCurrent,
                data.patrolBoatInvestigationCasesLast,
                '#81C784'
            ));

            electronicPatrolAbnormalChart.setOption(createChartOption(
                '电子巡航发现异常',
                data.electronicPatrolAbnormalDiscoveryCurrent,
                data.electronicPatrolAbnormalDiscoveryLast,
                '#FFB74D'
            ));

            electronicPatrolInvestigationChart.setOption(createChartOption(
                '电子巡航立案调查',
                data.electronicPatrolInvestigationCasesCurrent,
                data.electronicPatrolInvestigationCasesLast,
                '#E57373'
            ));

            dronePatrolAbnormalChart.setOption(createChartOption(
                '无人机巡航发现异常',
                data.droneAbnormalDiscoveryCurrent,
                data.droneAbnormalDiscoveryLast,
                '#BA68C8'
            ));

            dronePatrolInvestigationChart.setOption(createChartOption(
                '无人机巡航立案调查',
                data.droneInvestigationCasesCurrent,
                data.droneInvestigationCasesLast,
                '#4DD0E1'
            ));
        }

        // 窗口大小变化时调整图表大小
        $(window).resize(function() {
            patrolBoatAbnormalChart.resize();
            patrolBoatInvestigationChart.resize();
            electronicPatrolAbnormalChart.resize();
            electronicPatrolInvestigationChart.resize();
            dronePatrolAbnormalChart.resize();
            dronePatrolInvestigationChart.resize();
        });
    });
</script>
</body>

</html>

下方继续增加图表
一行是左边一个饼图，显示ship_type的分类数量，同时也要和前面的筛选条件联动。饼图仅展示前九名的船舶种类，其他归类为其他。一行右边是cargo_type的饼图，饼图仅展示前九名的，其他归类为其他，同时也要和前面的筛选条件联动。cargo_type不按照数量，按照loading_unloading_cargo_volume来累加。

下方继续增加图表。
一行的左边我希望做一个船舶长度的分布图，同时也要和前面的筛选条件联动。一行的右边是船舶载重吨的分布图，同时也要和前面的筛选条件联动。载重吨按照deadweight_tonnage进行查询。分布图请选用合适的图表来进行展现。

下方继续增加图表，
是一个 航线分布的图片。按照up_down_port的艘次数 由高到低排序，同时也要和前面的筛选条件联动。我希望展示最高的前五十名。然后该图表可以使用范围拖动筛选，显示范围可以增大缩小。

新建一个危防数据的展示页面，风格和其他展示页面要一致。
主要包含以下 要素。
首先是卡片数据。分别为危险品类处罚，防污染类处罚，危险品类检查，防污染类检查。
然后下面是一个表格，分别展示各单位危险品类处罚、防污染处罚、危险品类检查、防污染类检查的数量。
在下面使用表格展示危险品类处罚、防污染处罚、危险品类检查、防污染类检查分组对比图
