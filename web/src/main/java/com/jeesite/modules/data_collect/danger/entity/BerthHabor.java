package com.jeesite.modules.data_collect.danger.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 泊位港口对应表Entity
 * @author 系统自动生成
 * @version 2025-01-20
 */
@Table(name="berth_habor", alias="a", label="泊位港口对应信息", columns={
    @Column(name="id", attrName="id", label="编号", isPK=true),
    @Column(name="berth", attrName="berth", label="泊位", queryType=QueryType.LIKE),
    @Column(name="habor", attrName="habor", label="港口", queryType=QueryType.LIKE),
    @Column(name="agency", attrName="agency", label="机构", queryType=QueryType.LIKE),
}, orderBy="a.berth"
)
public class BerthHabor extends DataEntity<BerthHabor> {
    
    private static final long serialVersionUID = 1L;
    private String berth;   // 泊位
    private String habor;   // 港口
    private String agency;  // 机构
    
    public BerthHabor() {
        this(null);
    }
    
    public BerthHabor(String id) {
        super(id);
    }
    
    public String getBerth() {
        return berth;
    }
    
    public void setBerth(String berth) {
        this.berth = berth;
    }
    
    public String getHabor() {
        return habor;
    }
    
    public void setHabor(String habor) {
        this.habor = habor;
    }
    
    public String getAgency() {
        return agency;
    }
    
    public void setAgency(String agency) {
        this.agency = agency;
    }
} 