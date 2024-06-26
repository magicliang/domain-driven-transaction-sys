package com.magicliang.transaction.sys.common.dal.mybatis.po;

import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table tb_trans_alipay_suborder
 */
public class TransAlipaySubOrderPo {

    /**
     * Database Column Remarks:
     * 自增物理主键，单表唯一
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_alipay_suborder.id
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Long id;

    /**
     * Database Column Remarks:
     * 创建时间
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_alipay_suborder.gmt_created
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Date gmtCreated;

    /**
     * Database Column Remarks:
     * 最后修改时间
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_alipay_suborder.gmt_modified
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Date gmtModified;

    /**
     * Database Column Remarks:
     * 引用 tb_trans_pay_order 支付订单号，业务主键，全局唯一
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_alipay_suborder.pay_order_no
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private Long payOrderNo;

    /**
     * Database Column Remarks:
     * 目标账户
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_alipay_suborder.to_account_no
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private String toAccountNo;

    /**
     * Database Column Remarks:
     * 账户
     * <p>
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_trans_alipay_suborder.name
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    private String name;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_alipay_suborder.id
     *
     * @return the value of tb_trans_alipay_suborder.id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_alipay_suborder.id
     *
     * @param id the value for tb_trans_alipay_suborder.id
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_alipay_suborder.gmt_created
     *
     * @return the value of tb_trans_alipay_suborder.gmt_created
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Date getGmtCreated() {
        return gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_alipay_suborder.gmt_created
     *
     * @param gmtCreated the value for tb_trans_alipay_suborder.gmt_created
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_alipay_suborder.gmt_modified
     *
     * @return the value of tb_trans_alipay_suborder.gmt_modified
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_alipay_suborder.gmt_modified
     *
     * @param gmtModified the value for tb_trans_alipay_suborder.gmt_modified
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_alipay_suborder.pay_order_no
     *
     * @return the value of tb_trans_alipay_suborder.pay_order_no
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public Long getPayOrderNo() {
        return payOrderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_alipay_suborder.pay_order_no
     *
     * @param payOrderNo the value for tb_trans_alipay_suborder.pay_order_no
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setPayOrderNo(Long payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_alipay_suborder.to_account_no
     *
     * @return the value of tb_trans_alipay_suborder.to_account_no
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String getToAccountNo() {
        return toAccountNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_alipay_suborder.to_account_no
     *
     * @param toAccountNo the value for tb_trans_alipay_suborder.to_account_no
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo == null ? null : toAccountNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_trans_alipay_suborder.name
     *
     * @return the value of tb_trans_alipay_suborder.name
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_trans_alipay_suborder.name
     *
     * @param name the value for tb_trans_alipay_suborder.name
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_alipay_suborder
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
        TransAlipaySubOrderPo other = (TransAlipaySubOrderPo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getGmtCreated() == null ? other.getGmtCreated() == null
                : this.getGmtCreated().equals(other.getGmtCreated()))
                && (this.getGmtModified() == null ? other.getGmtModified() == null
                : this.getGmtModified().equals(other.getGmtModified()))
                && (this.getPayOrderNo() == null ? other.getPayOrderNo() == null
                : this.getPayOrderNo().equals(other.getPayOrderNo()))
                && (this.getToAccountNo() == null ? other.getToAccountNo() == null
                : this.getToAccountNo().equals(other.getToAccountNo()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_alipay_suborder
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
        result = prime * result + ((getToAccountNo() == null) ? 0 : getToAccountNo().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }
}