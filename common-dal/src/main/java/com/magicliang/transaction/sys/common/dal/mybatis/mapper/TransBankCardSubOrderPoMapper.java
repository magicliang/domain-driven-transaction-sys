package com.magicliang.transaction.sys.common.dal.mybatis.mapper;

import com.magicliang.transaction.sys.common.dal.mybatis.po.TransBankCardSubOrderPo;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransBankCardSubOrderPoExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface TransBankCardSubOrderPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @SelectProvider(type = TransBankCardSubOrderPoSqlProvider.class, method = "countByExample")
    long countByExample(TransBankCardSubOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @DeleteProvider(type = TransBankCardSubOrderPoSqlProvider.class, method = "deleteByExample")
    int deleteByExample(TransBankCardSubOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Delete({
            "delete from tb_trans_bank_card_suborder",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Insert({
            "insert into tb_trans_bank_card_suborder (gmt_created, gmt_modified, ",
            "pay_order_no, to_bank_account, ",
            "account_type, name, ",
            "bank_id, branch_id, ",
            "city_id, branch_name, ",
            "to_branch_no)",
            "values (#{gmtCreated,jdbcType=TIMESTAMP}, #{gmtModified,jdbcType=TIMESTAMP}, ",
            "#{payOrderNo,jdbcType=BIGINT}, #{toBankAccount,jdbcType=VARCHAR}, ",
            "#{accountType,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
            "#{bankId,jdbcType=INTEGER}, #{branchId,jdbcType=INTEGER}, ",
            "#{cityId,jdbcType=INTEGER}, #{branchName,jdbcType=VARCHAR}, ",
            "#{toBranchNo,jdbcType=VARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(TransBankCardSubOrderPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @InsertProvider(type = TransBankCardSubOrderPoSqlProvider.class, method = "insertSelective")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int insertSelective(TransBankCardSubOrderPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @SelectProvider(type = TransBankCardSubOrderPoSqlProvider.class, method = "selectByExample")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "gmt_created", property = "gmtCreated", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_modified", property = "gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "pay_order_no", property = "payOrderNo", jdbcType = JdbcType.BIGINT),
            @Result(column = "to_bank_account", property = "toBankAccount", jdbcType = JdbcType.VARCHAR),
            @Result(column = "account_type", property = "accountType", jdbcType = JdbcType.INTEGER),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "bank_id", property = "bankId", jdbcType = JdbcType.INTEGER),
            @Result(column = "branch_id", property = "branchId", jdbcType = JdbcType.INTEGER),
            @Result(column = "city_id", property = "cityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "branch_name", property = "branchName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "to_branch_no", property = "toBranchNo", jdbcType = JdbcType.VARCHAR)
    })
    List<TransBankCardSubOrderPo> selectByExample(TransBankCardSubOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Select({
            "select",
            "id, gmt_created, gmt_modified, pay_order_no, to_bank_account, account_type, ",
            "name, bank_id, branch_id, city_id, branch_name, to_branch_no",
            "from tb_trans_bank_card_suborder",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "gmt_created", property = "gmtCreated", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_modified", property = "gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "pay_order_no", property = "payOrderNo", jdbcType = JdbcType.BIGINT),
            @Result(column = "to_bank_account", property = "toBankAccount", jdbcType = JdbcType.VARCHAR),
            @Result(column = "account_type", property = "accountType", jdbcType = JdbcType.INTEGER),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "bank_id", property = "bankId", jdbcType = JdbcType.INTEGER),
            @Result(column = "branch_id", property = "branchId", jdbcType = JdbcType.INTEGER),
            @Result(column = "city_id", property = "cityId", jdbcType = JdbcType.INTEGER),
            @Result(column = "branch_name", property = "branchName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "to_branch_no", property = "toBranchNo", jdbcType = JdbcType.VARCHAR)
    })
    TransBankCardSubOrderPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @UpdateProvider(type = TransBankCardSubOrderPoSqlProvider.class, method = "updateByExampleSelective")
    int updateByExampleSelective(@Param("record") TransBankCardSubOrderPo record, @Param("example") TransBankCardSubOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @UpdateProvider(type = TransBankCardSubOrderPoSqlProvider.class, method = "updateByExample")
    int updateByExample(@Param("record") TransBankCardSubOrderPo record, @Param("example") TransBankCardSubOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @UpdateProvider(type = TransBankCardSubOrderPoSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TransBankCardSubOrderPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_bank_card_suborder
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Update({
            "update tb_trans_bank_card_suborder",
            "set gmt_created = #{gmtCreated,jdbcType=TIMESTAMP},",
            "gmt_modified = #{gmtModified,jdbcType=TIMESTAMP},",
            "pay_order_no = #{payOrderNo,jdbcType=BIGINT},",
            "to_bank_account = #{toBankAccount,jdbcType=VARCHAR},",
            "account_type = #{accountType,jdbcType=INTEGER},",
            "name = #{name,jdbcType=VARCHAR},",
            "bank_id = #{bankId,jdbcType=INTEGER},",
            "branch_id = #{branchId,jdbcType=INTEGER},",
            "city_id = #{cityId,jdbcType=INTEGER},",
            "branch_name = #{branchName,jdbcType=VARCHAR},",
            "to_branch_no = #{toBranchNo,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(TransBankCardSubOrderPo record);
}