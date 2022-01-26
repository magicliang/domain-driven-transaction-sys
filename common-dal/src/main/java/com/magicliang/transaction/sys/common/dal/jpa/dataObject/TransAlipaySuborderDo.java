package com.magicliang.transaction.sys.common.dal.jpa.dataObject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_trans_alipay_suborder")
public class TransAlipaySuborderDo implements Serializable {
    private static final long serialVersionUID = -4290762271017491703L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date gmtCreated;

    private Date gmtModified;

    private Long payOrderNo;

    private String toAccountNo;

    private String name;

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    @Column(name = "to_account_no", nullable = false)
    public String getToAccountNo() {
        return toAccountNo;
    }

    @Column(name = "pay_order_no", nullable = false)
    public Long getPayOrderNo() {
        return payOrderNo;
    }

    @Column(name = "gmt_modified", nullable = false)
    public Date getGmtModified() {
        return gmtModified;
    }

    @Column(name = "gmt_created", nullable = false)
    public Date getGmtCreated() {
        return gmtCreated;
    }

    public Long getId() {
        return id;
    }
}