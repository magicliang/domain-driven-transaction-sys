package com.magicliang.transaction.sys.common.dal.mybatis.mapper;

import com.magicliang.transaction.sys.common.dal.mybatis.po.TransBankCardSubOrderPo;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransBankCardSubOrderPoExample;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransBankCardSubOrderPoExample.Criteria;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransBankCardSubOrderPoExample.Criterion;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class TransBankCardSubOrderPoSqlProvider {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String countByExample(TransBankCardSubOrderPoExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("tb_trans_bank_card_suborder");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String deleteByExample(TransBankCardSubOrderPoExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("tb_trans_bank_card_suborder");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String insertSelective(TransBankCardSubOrderPo record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("tb_trans_bank_card_suborder");

        if (record.getGmtCreated() != null) {
            sql.VALUES("gmt_created", "#{gmtCreated,jdbcType=TIMESTAMP}");
        }

        if (record.getGmtModified() != null) {
            sql.VALUES("gmt_modified", "#{gmtModified,jdbcType=TIMESTAMP}");
        }

        if (record.getPayOrderNo() != null) {
            sql.VALUES("pay_order_no", "#{payOrderNo,jdbcType=BIGINT}");
        }

        if (record.getToBankAccount() != null) {
            sql.VALUES("to_bank_account", "#{toBankAccount,jdbcType=VARCHAR}");
        }

        if (record.getAccountType() != null) {
            sql.VALUES("account_type", "#{accountType,jdbcType=INTEGER}");
        }

        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }

        if (record.getBankId() != null) {
            sql.VALUES("bank_id", "#{bankId,jdbcType=INTEGER}");
        }

        if (record.getBranchId() != null) {
            sql.VALUES("branch_id", "#{branchId,jdbcType=INTEGER}");
        }

        if (record.getCityId() != null) {
            sql.VALUES("city_id", "#{cityId,jdbcType=INTEGER}");
        }

        if (record.getBranchName() != null) {
            sql.VALUES("branch_name", "#{branchName,jdbcType=VARCHAR}");
        }

        if (record.getToBranchNo() != null) {
            sql.VALUES("to_branch_no", "#{toBranchNo,jdbcType=VARCHAR}");
        }

        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String selectByExample(TransBankCardSubOrderPoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("gmt_created");
        sql.SELECT("gmt_modified");
        sql.SELECT("pay_order_no");
        sql.SELECT("to_bank_account");
        sql.SELECT("account_type");
        sql.SELECT("name");
        sql.SELECT("bank_id");
        sql.SELECT("branch_id");
        sql.SELECT("city_id");
        sql.SELECT("branch_name");
        sql.SELECT("to_branch_no");
        sql.FROM("tb_trans_bank_card_suborder");
        applyWhere(sql, example, false);

        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }

        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        TransBankCardSubOrderPo record = (TransBankCardSubOrderPo) parameter.get("record");
        TransBankCardSubOrderPoExample example = (TransBankCardSubOrderPoExample) parameter.get("example");

        SQL sql = new SQL();
        sql.UPDATE("tb_trans_bank_card_suborder");

        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=BIGINT}");
        }

        if (record.getGmtCreated() != null) {
            sql.SET("gmt_created = #{record.gmtCreated,jdbcType=TIMESTAMP}");
        }

        if (record.getGmtModified() != null) {
            sql.SET("gmt_modified = #{record.gmtModified,jdbcType=TIMESTAMP}");
        }

        if (record.getPayOrderNo() != null) {
            sql.SET("pay_order_no = #{record.payOrderNo,jdbcType=BIGINT}");
        }

        if (record.getToBankAccount() != null) {
            sql.SET("to_bank_account = #{record.toBankAccount,jdbcType=VARCHAR}");
        }

        if (record.getAccountType() != null) {
            sql.SET("account_type = #{record.accountType,jdbcType=INTEGER}");
        }

        if (record.getName() != null) {
            sql.SET("name = #{record.name,jdbcType=VARCHAR}");
        }

        if (record.getBankId() != null) {
            sql.SET("bank_id = #{record.bankId,jdbcType=INTEGER}");
        }

        if (record.getBranchId() != null) {
            sql.SET("branch_id = #{record.branchId,jdbcType=INTEGER}");
        }

        if (record.getCityId() != null) {
            sql.SET("city_id = #{record.cityId,jdbcType=INTEGER}");
        }

        if (record.getBranchName() != null) {
            sql.SET("branch_name = #{record.branchName,jdbcType=VARCHAR}");
        }

        if (record.getToBranchNo() != null) {
            sql.SET("to_branch_no = #{record.toBranchNo,jdbcType=VARCHAR}");
        }

        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("tb_trans_bank_card_suborder");

        sql.SET("id = #{record.id,jdbcType=BIGINT}");
        sql.SET("gmt_created = #{record.gmtCreated,jdbcType=TIMESTAMP}");
        sql.SET("gmt_modified = #{record.gmtModified,jdbcType=TIMESTAMP}");
        sql.SET("pay_order_no = #{record.payOrderNo,jdbcType=BIGINT}");
        sql.SET("to_bank_account = #{record.toBankAccount,jdbcType=VARCHAR}");
        sql.SET("account_type = #{record.accountType,jdbcType=INTEGER}");
        sql.SET("name = #{record.name,jdbcType=VARCHAR}");
        sql.SET("bank_id = #{record.bankId,jdbcType=INTEGER}");
        sql.SET("branch_id = #{record.branchId,jdbcType=INTEGER}");
        sql.SET("city_id = #{record.cityId,jdbcType=INTEGER}");
        sql.SET("branch_name = #{record.branchName,jdbcType=VARCHAR}");
        sql.SET("to_branch_no = #{record.toBranchNo,jdbcType=VARCHAR}");

        TransBankCardSubOrderPoExample example = (TransBankCardSubOrderPoExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    public String updateByPrimaryKeySelective(TransBankCardSubOrderPo record) {
        SQL sql = new SQL();
        sql.UPDATE("tb_trans_bank_card_suborder");

        if (record.getGmtCreated() != null) {
            sql.SET("gmt_created = #{gmtCreated,jdbcType=TIMESTAMP}");
        }

        if (record.getGmtModified() != null) {
            sql.SET("gmt_modified = #{gmtModified,jdbcType=TIMESTAMP}");
        }

        if (record.getPayOrderNo() != null) {
            sql.SET("pay_order_no = #{payOrderNo,jdbcType=BIGINT}");
        }

        if (record.getToBankAccount() != null) {
            sql.SET("to_bank_account = #{toBankAccount,jdbcType=VARCHAR}");
        }

        if (record.getAccountType() != null) {
            sql.SET("account_type = #{accountType,jdbcType=INTEGER}");
        }

        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }

        if (record.getBankId() != null) {
            sql.SET("bank_id = #{bankId,jdbcType=INTEGER}");
        }

        if (record.getBranchId() != null) {
            sql.SET("branch_id = #{branchId,jdbcType=INTEGER}");
        }

        if (record.getCityId() != null) {
            sql.SET("city_id = #{cityId,jdbcType=INTEGER}");
        }

        if (record.getBranchName() != null) {
            sql.SET("branch_name = #{branchName,jdbcType=VARCHAR}");
        }

        if (record.getToBranchNo() != null) {
            sql.SET("to_branch_no = #{toBranchNo,jdbcType=VARCHAR}");
        }

        sql.WHERE("id = #{id,jdbcType=BIGINT}");

        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    protected void applyWhere(SQL sql, TransBankCardSubOrderPoExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }

        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d]"
                    + ".criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example"
                    + ".oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d]"
                    + ".secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d]"
                    + ".criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }

        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }

                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }

                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,
                                    criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j,
                                    criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }

        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}