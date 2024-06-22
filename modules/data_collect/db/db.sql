CREATE TABLE ship_inspection (
                                 `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
                                 ship_id VARCHAR(50) NOT NULL COMMENT '船舶识别号',
                                 ship_name_cn VARCHAR(100) NOT NULL COMMENT '中文船名',
                                 port_of_registry VARCHAR(100) NOT NULL COMMENT '船籍港',
                                 inspection_type ENUM('初查', '复查') NOT NULL COMMENT '初查/复查',
                                 safety_inspection_type VARCHAR(100) NOT NULL COMMENT '安检类型',
                                 inspection_date DATE NOT NULL COMMENT '检查日期',
                                 inspection_port VARCHAR(100) NOT NULL COMMENT '检查港',
                                 inspection_agency VARCHAR(100) NOT NULL COMMENT '检查机构',
                                 inspector VARCHAR(100) NOT NULL COMMENT '检查员',
                                 defect_count INT NOT NULL COMMENT '缺陷数量',
                                 detained ENUM('是', '否') NOT NULL COMMENT '是否被滞留',
                                 defect_code VARCHAR(50) COMMENT '缺陷代码',
                                 defect_description TEXT COMMENT '缺陷描述',
                                 handling_comments TEXT COMMENT '处理意见说明',
                                 `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
                                 `create_date` datetime NOT NULL COMMENT '创建时间',
                                 `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
                                 `update_date` datetime NOT NULL COMMENT '更新时间',
                                 `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT '海船安全检查信息表' ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE ship_on_site_inspection (
    `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
    ship_id VARCHAR(50) NOT NULL COMMENT '船舶识别号',
    ship_name_cn VARCHAR(100) NOT NULL COMMENT '中文船名',
    inspection_name VARCHAR(100) NOT NULL COMMENT '专项检查名称',
    inspection_type VARCHAR(100) NOT NULL COMMENT '专项检查类型',
    initial_or_recheck VARCHAR(10) NOT NULL COMMENT '初查/复查',
    inspection_date DATE NOT NULL COMMENT '检查日期',
    inspection_location VARCHAR(100) NOT NULL COMMENT '检查地点',
    inspection_agency VARCHAR(100) NOT NULL COMMENT '检查机构',
    inspector VARCHAR(100) NOT NULL COMMENT '检查员',
    issue_found VARCHAR(100) NOT NULL COMMENT '发现问题或违章',
    inspection_content_code VARCHAR(50) COMMENT '检查内容代码',
    inspection_content TEXT COMMENT '检查内容',
    issue_description TEXT COMMENT '违章或问题说明',
    handling_code VARCHAR(50) COMMENT '处理意见代码',
    handling_comments TEXT COMMENT '处理意见说明',
    corrected VARCHAR(10) NOT NULL COMMENT '是否纠正',
    `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
    `create_date` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
    `update_date` datetime NOT NULL COMMENT '更新时间',
    `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT '船舶现场监督检查表' ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE psc_inspection (
                                `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',

                                type VARCHAR(50) NOT NULL COMMENT 'Type of inspection',
                                submitted DATETIME NOT NULL COMMENT 'submitted time',
                                inspection_date DATE NOT NULL COMMENT 'inspection date',
                                port VARCHAR(100) NOT NULL COMMENT 'Port',
                                imo_number VARCHAR(50) NOT NULL COMMENT 'IMO number',
                                name VARCHAR(100) NOT NULL COMMENT 'Name',
                                callsign VARCHAR(50) NOT NULL COMMENT 'Callsign',
                                mmsi VARCHAR(50) NOT NULL COMMENT 'MMSI',
                                flag VARCHAR(100) NOT NULL COMMENT 'Flag',
                                ship_type VARCHAR(100) NOT NULL COMMENT 'Ship Type',
                                psco VARCHAR(100) NOT NULL COMMENT 'PSCO',
                                date_keel_laid VARCHAR(100) NOT NULL COMMENT 'keel laid',
                                class_society VARCHAR(100) NOT NULL COMMENT 'Classification society',
                                gross_tonnage VARCHAR(10) NOT NULL COMMENT 'Gross tonnage',
                                deficiencies VARCHAR(10) NOT NULL COMMENT 'deficiencies found',
                                detention VARCHAR(10) NOT NULL COMMENT 'Detention status',
                                detention_deficiencies VARCHAR(10) COMMENT 'detention deficiencies',
                                ship_risk_profile VARCHAR(100) NOT NULL COMMENT 'Risk profile',
                                priority VARCHAR(100) NOT NULL COMMENT 'Inspection priority',
                                window_inspection_range VARCHAR(100) NOT NULL COMMENT 'Window inspection range',
`create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
    `create_date` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
    `update_date` datetime NOT NULL COMMENT '更新时间',
    `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT 'PSC Inspection Table' ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE data_sharing_inventory (
                                        `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
                                        management_department VARCHAR(64) COMMENT '归口管理部门',
                                        data_category VARCHAR(255) COMMENT '数据大类',
                                        business_segment VARCHAR(255) COMMENT '业务板块',
                                        serial_number VARCHAR(255) COMMENT '编号',
                                        data_item_name VARCHAR(255) COMMENT '数据项目名称',
                                        unit_of_measurement VARCHAR(30) COMMENT '计数单位',
                                        statistical_frequency VARCHAR(255) COMMENT '统计频次',
                                        data_granularity VARCHAR(255) COMMENT '数据粒度',
                                        data_collection_method VARCHAR(255) COMMENT '数据采集方式',
                                        data_definition TEXT COMMENT '数据定义',
                                        `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
                                        `create_date` datetime NOT NULL COMMENT '创建时间',
                                        `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
                                        `update_date` datetime NOT NULL COMMENT '更新时间',
                                        `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT '数据共享清单' ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE data_metrics (
                              `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',

                              data_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据项ID', -- 数据项ID
                              department_id varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门ID', -- 部门ID
                              start_time DATE NOT NULL COMMENT '开始时间', -- 开始时间
                              end_time DATE NOT NULL COMMENT '结束时间', -- 结束时间
                              current_value DECIMAL(10,2) NOT NULL COMMENT '本期数值', -- 本期数值
                              `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
                              `create_date` datetime NOT NULL COMMENT '创建时间',
                              `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
                              `update_date` datetime NOT NULL COMMENT '更新时间',
                              `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT '数据指标表' ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE ship_report (
                            `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',

                            report_id VARCHAR(64) COMMENT '报告编号',
                            ship_name_cn VARCHAR(32) COMMENT '中文船名',
                            ship_identification_number VARCHAR(64) COMMENT '船舶识别号',
                            gross_tonnage DECIMAL(10, 2) COMMENT '总吨',
                            deadweight_tonnage DECIMAL(10, 2) COMMENT '载重吨',
                            main_engine_power DECIMAL(10, 2) COMMENT '主机功率',
                            ship_type VARCHAR(32) COMMENT '船舶种类',
                            port_of_registry VARCHAR(32) COMMENT '船籍港',
                            ship_length DECIMAL(10, 2) COMMENT '船舶长度',
                            ship_width DECIMAL(10, 2) COMMENT '船舶宽度',
                            in_out_port VARCHAR(16) COMMENT '进出港',
                            sea_river_ship VARCHAR(16) COMMENT '海/河船',
                            reporting_agency VARCHAR(32) COMMENT '报告机构',
                            estimated_arrival_departure_time DATETIME COMMENT '预抵离时间',
                            report_time DATETIME COMMENT '报告时间',
                            port VARCHAR(32) COMMENT '港口',
                            berth VARCHAR(64) COMMENT '泊位',
                            up_down_port VARCHAR(32) COMMENT '上下港',
                            voyage_daily_report VARCHAR(32) COMMENT '航次日报',
                            voyage_count INT COMMENT '航次数量',
                            actual_cargo_volume DECIMAL(10, 2) COMMENT '实载货量',
                            loading_unloading_cargo_volume DECIMAL(10, 2) COMMENT '装卸货量',
                            actual_vehicle_count INT COMMENT '实载车辆',
                            loading_unloading_vehicle_count INT COMMENT '装卸车辆',
                            actual_passenger_count INT COMMENT '实载客量',
                            up_down_passenger_count INT COMMENT '上下客量',
                            cargo_type VARCHAR(64) COMMENT '货物种类',
                            cargo_name VARCHAR(64) COMMENT '品名',
                            mmsi VARCHAR(24) COMMENT 'MMSI',
                            draft_fore DECIMAL(10, 2) COMMENT '前吃水',
                            draft_aft DECIMAL(10, 2) COMMENT '后吃水',
                            ship_owner VARCHAR(64) COMMENT '船舶所有人',
                            applicant VARCHAR(32) COMMENT '申请人',
                            applicant_contact VARCHAR(32) COMMENT '申请人联系方式',
                            is_hazardous_cargo VARCHAR(10) COMMENT '是否危险货物',
                            hazardous_cargo_quantity DECIMAL(10, 2) COMMENT '危险货物数量',
                            barge_count INT COMMENT '驳船数量',
                            local_barge_operations_count INT COMMENT '本港加/解驳船数量',
                            actual_container_volume DECIMAL(10, 2) COMMENT '实载集装箱运量',
                            local_container_operations_volume DECIMAL(10, 2) COMMENT '本港装/卸集装箱运量',
                            `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
                            `create_date` datetime NOT NULL COMMENT '创建时间',
                            `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
                            `update_date` datetime NOT NULL COMMENT '更新时间',
                            `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT '船舶报告表' ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;

    /**
    周工作数据表
     */
CREATE TABLE weekly_report (
                                   `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
                                   department_id VARCHAR(64) COMMENT '部门',
                                   sea_ship_inspection_count INT COMMENT '海船安检艘次',
                                   sea_ship_defect_count INT COMMENT '海船缺陷数量',
                                   sea_ship_detention_count INT COMMENT '海船滞留艘次',
                                   river_ship_inspection_count INT COMMENT '内河船安检艘次',
                                   river_ship_defect_count INT COMMENT '内河船缺陷数量',
                                   river_ship_detention_count INT COMMENT '内河船滞留数量',
                                   psc_inspection_count INT COMMENT 'PSC艘次',
                                   psc_defect_count INT COMMENT 'PSC缺陷数量',
                                   psc_detention_count INT COMMENT 'PSC滞留艘次',
                                   bulk_liquid_hazardous_cargo_supervision INT COMMENT '散装液体危险货物作业现场监督检查',
                                   fuel_quick_sampling_inspection INT COMMENT '燃油快速抽样检测',
                                   investigation_cases INT COMMENT '立案调查数据',
                                   patrol_boat_abnormal_discovery INT COMMENT '海巡艇巡航发现异常',
                                   patrol_boat_investigation_cases INT COMMENT '海巡艇巡航立案调查',
                                   drone_abnormal_discovery INT COMMENT '无人机巡航发现异常',
                                   drone_investigation_cases INT COMMENT '无人机巡航立案调查',
                                   electronic_patrol_abnormal_discovery INT COMMENT '电子巡航发现异常',
                                   electronic_patrol_investigation_cases INT COMMENT '电子巡航立案调查',
                                   report_date DATE COMMENT '日期',
                                   restricted_ship_in INT COMMENT '受限船进',
                                   restricted_ship_out INT COMMENT '受限船出',
                                   cape_ship_in INT COMMENT 'cape船进',
                                   cape_ship_out INT COMMENT 'cape船出',
                                   danger_count INT COMMENT '险情数量',
                                   hydraulic_construction_count INT COMMENT '水工数量',
                                   restricted_ship_approval_count INT COMMENT '受限船核准数量',
                                   risk_hazard_identification_count INT COMMENT '排查风险隐患数量',
                                   port_entry_exit_report_count INT COMMENT '进出港报告艘次',
                                   penalty_decision_count INT COMMENT '处罚决定数量',
                                   penalty_amount DECIMAL(10, 2) COMMENT '处罚金额',
                                   illegal_score INT COMMENT '违法计分',
                                       `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
                                   `create_date` datetime NOT NULL COMMENT '创建时间',
                                   `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
                                   `update_date` datetime NOT NULL COMMENT '更新时间',
                                   `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT '周工作数据表' ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;