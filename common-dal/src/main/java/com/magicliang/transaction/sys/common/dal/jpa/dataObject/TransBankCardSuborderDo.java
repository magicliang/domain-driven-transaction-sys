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
@Table(name = "tb_trans_bank_card_suborder")
public class TransBankCardSuborderDo implements Serializable {
    private static final long serialVersionUID = 6890614318240860189L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date gmtCreated;

    private Date gmtModified;

    private Long payOrderNo;

    private String toBankAccount;

    private Integer accountType;

    private String name;

    private Integer bankId;

    private Integer branchId;

    private Integer cityId;

    private String branchName;

    private String toBranchNo;

    @Column(name = "to_branch_no", nullable = false)
    public String getToBranchNo() {
        return toBranchNo;
    }

    @Column(name = "branch_name", nullable = false)
    public String getBranchName() {
        return branchName;
    }

    @Column(name = "city_id")
    public Integer getCityId() {
        return cityId;
    }

    @Column(name = "branch_id")
    public Integer getBranchId() {
        return branchId;
    }

    @Column(name = "bank_id")
    public Integer getBankId() {
        return bankId;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    @Column(name = "account_type", nullable = false)
    public Integer getAccountType() {
        return accountType;
    }

    @Column(name = "to_bank_account", nullable = false)
    public String getToBankAccount() {
        return toBankAccount;
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