package com.magicliang.transaction.sys.common.dal.po;

import java.util.Date;
import java.util.Objects;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 子交易订单持久化模型
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 16:50
 */
public class TransAlipaySubOrderPo {

    /**
     * 自增物理主键，单表唯一
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 最后修改时间
     */
    private Date gmtModified;

    /**
     * 引用 tb_pay_order 支付订单号，业务主键，全局唯一
     */
    private Long payOrderNo;

    /**
     * 目标支付宝账户
     */
    private String toAliPayAccount;

    /**
     * get the value of id
     *
     * @return the value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * set the value of the id
     *
     * @param id the value of id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get the value of gmtCreated
     *
     * @return the value of gmtCreated
     */
    public Date getGmtCreated() {
        return gmtCreated;
    }

    /**
     * set the value of the gmtCreated
     *
     * @param gmtCreated the value of gmtCreated
     */
    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    /**
     * get the value of gmtModified
     *
     * @return the value of gmtModified
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * set the value of the gmtModified
     *
     * @param gmtModified the value of gmtModified
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * get the value of payOrderNo
     *
     * @return the value of payOrderNo
     */
    public Long getPayOrderNo() {
        return payOrderNo;
    }

    /**
     * set the value of the payOrderNo
     *
     * @param payOrderNo the value of payOrderNo
     */
    public void setPayOrderNo(Long payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    /**
     * get the value of toAliPayAccount
     *
     * @return the value of toAliPayAccount
     */
    public String getToAliPayAccount() {
        return toAliPayAccount;
    }

    /**
     * set the value of the toAliPayAccount
     *
     * @param toAliPayAccount the value of toAliPayAccount
     */
    public void setToAliPayAccount(String toAliPayAccount) {
        this.toAliPayAccount = toAliPayAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransAlipaySubOrderPo)) return false;
        TransAlipaySubOrderPo that = (TransAlipaySubOrderPo) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getGmtCreated(), that.getGmtCreated()) && Objects.equals(getGmtModified(), that.getGmtModified()) && Objects.equals(getPayOrderNo(), that.getPayOrderNo()) && Objects.equals(getToAliPayAccount(), that.getToAliPayAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGmtCreated(), getGmtModified(), getPayOrderNo(), getToAliPayAccount());
    }

    @Override
    public String toString() {
        return "TransAlipaySubOrderPo{" +
                "id=" + id +
                ", gmtCreated=" + gmtCreated +
                ", gmtModified=" + gmtModified +
                ", payOrderNo=" + payOrderNo +
                ", toAliPayAccount='" + toAliPayAccount + '\'' +
                '}';
    }
}
