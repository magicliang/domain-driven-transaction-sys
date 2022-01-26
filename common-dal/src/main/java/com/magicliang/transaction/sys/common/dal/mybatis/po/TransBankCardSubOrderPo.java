package com.magicliang.transaction.sys.common.dal.mybatis.po;

import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table tb_trans_bank_card_suborder
 */
public class TransBankCardSubOrderPo {
    /**
     * Database Column Remarks:
     * 自增物理主键，单表唯一
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.id
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Long id;

    /**
     * Database Column Remarks:
     * 创建时间
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.gmt_created
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Date gmtCreated;

    /**
     * Database Column Remarks:
     * 最后修改时间
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.gmt_modified
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Date gmtModified;

    /**
     * Database Column Remarks:
     * 引用 tb_trans_pay_order 支付订单号，业务主键，全局唯一
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.pay_order_no
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Long payOrderNo;

    /**
     * Database Column Remarks:
     * 目标银行账户
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.to_bank_account
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private String toBankAccount;

    /**
     * Database Column Remarks:
     * 支付账户类型，1 对公 2 对私
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.account_type
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Integer accountType;

    /**
     * Database Column Remarks:
     * 目标银行账户户名
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.name
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private String name;

    /**
     * Database Column Remarks:
     * 银行 id
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.bank_id
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Integer bankId;

    /**
     * Database Column Remarks:
     * 支行 id
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.branch_id
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Integer branchId;

    /**
     * Database Column Remarks:
     * 城市 id
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.city_id
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Integer cityId;

    /**
     * Database Column Remarks:
     * 支行 id
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.branch_name
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private String branchName;

    /**
     * Database Column Remarks:
     * 收款支行联行号
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_bank_card_suborder.to_branch_no
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private String toBranchNo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.id
     *
     * @return the value of tb_trans_bank_card_suborder.id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.id
     *
     * @param id the value for tb_trans_bank_card_suborder.id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.gmt_created
     *
     * @return the value of tb_trans_bank_card_suborder.gmt_created
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Date getGmtCreated() {
        return gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.gmt_created
     *
     * @param gmtCreated the value for tb_trans_bank_card_suborder.gmt_created
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.gmt_modified
     *
     * @return the value of tb_trans_bank_card_suborder.gmt_modified
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.gmt_modified
     *
     * @param gmtModified the value for tb_trans_bank_card_suborder.gmt_modified
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.pay_order_no
     *
     * @return the value of tb_trans_bank_card_suborder.pay_order_no
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Long getPayOrderNo() {
        return payOrderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.pay_order_no
     *
     * @param payOrderNo the value for tb_trans_bank_card_suborder.pay_order_no
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setPayOrderNo(Long payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.to_bank_account
     *
     * @return the value of tb_trans_bank_card_suborder.to_bank_account
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String getToBankAccount() {
        return toBankAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.to_bank_account
     *
     * @param toBankAccount the value for tb_trans_bank_card_suborder.to_bank_account
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setToBankAccount(String toBankAccount) {
        this.toBankAccount = toBankAccount == null ? null : toBankAccount.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.account_type
     *
     * @return the value of tb_trans_bank_card_suborder.account_type
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Integer getAccountType() {
        return accountType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.account_type
     *
     * @param accountType the value for tb_trans_bank_card_suborder.account_type
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.name
     *
     * @return the value of tb_trans_bank_card_suborder.name
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.name
     *
     * @param name the value for tb_trans_bank_card_suborder.name
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.bank_id
     *
     * @return the value of tb_trans_bank_card_suborder.bank_id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Integer getBankId() {
        return bankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.bank_id
     *
     * @param bankId the value for tb_trans_bank_card_suborder.bank_id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.branch_id
     *
     * @return the value of tb_trans_bank_card_suborder.branch_id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.branch_id
     *
     * @param branchId the value for tb_trans_bank_card_suborder.branch_id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.city_id
     *
     * @return the value of tb_trans_bank_card_suborder.city_id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.city_id
     *
     * @param cityId the value for tb_trans_bank_card_suborder.city_id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.branch_name
     *
     * @return the value of tb_trans_bank_card_suborder.branch_name
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.branch_name
     *
     * @param branchName the value for tb_trans_bank_card_suborder.branch_name
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName == null ? null : branchName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_bank_card_suborder.to_branch_no
     *
     * @return the value of tb_trans_bank_card_suborder.to_branch_no
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String getToBranchNo() {
        return toBranchNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_bank_card_suborder.to_branch_no
     *
     * @param toBranchNo the value for tb_trans_bank_card_suborder.to_branch_no
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setToBranchNo(String toBranchNo) {
        this.toBranchNo = toBranchNo == null ? null : toBranchNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TransBankCardSubOrderPo other = (TransBankCardSubOrderPo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getGmtCreated() == null ? other.getGmtCreated() == null : this.getGmtCreated().equals(other.getGmtCreated()))
                && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
                && (this.getPayOrderNo() == null ? other.getPayOrderNo() == null : this.getPayOrderNo().equals(other.getPayOrderNo()))
                && (this.getToBankAccount() == null ? other.getToBankAccount() == null : this.getToBankAccount().equals(other.getToBankAccount()))
                && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getBankId() == null ? other.getBankId() == null : this.getBankId().equals(other.getBankId()))
                && (this.getBranchId() == null ? other.getBranchId() == null : this.getBranchId().equals(other.getBranchId()))
                && (this.getCityId() == null ? other.getCityId() == null : this.getCityId().equals(other.getCityId()))
                && (this.getBranchName() == null ? other.getBranchName() == null : this.getBranchName().equals(other.getBranchName()))
                && (this.getToBranchNo() == null ? other.getToBranchNo() == null : this.getToBranchNo().equals(other.getToBranchNo()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGmtCreated() == null) ? 0 : getGmtCreated().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getPayOrderNo() == null) ? 0 : getPayOrderNo().hashCode());
        result = prime * result + ((getToBankAccount() == null) ? 0 : getToBankAccount().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getBankId() == null) ? 0 : getBankId().hashCode());
        result = prime * result + ((getBranchId() == null) ? 0 : getBranchId().hashCode());
        result = prime * result + ((getCityId() == null) ? 0 : getCityId().hashCode());
        result = prime * result + ((getBranchName() == null) ? 0 : getBranchName().hashCode());
        result = prime * result + ((getToBranchNo() == null) ? 0 : getToBranchNo().hashCode());
        return result;
    }
}