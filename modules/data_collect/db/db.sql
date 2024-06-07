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