package com.magicliang.transaction.sys.common.dal.mybatis.mapper;

import com.magicliang.transaction.sys.common.dal.mybatis.po.TransPayOrderPo;
import com.magicliang.transaction.sys.common.dal.mybatis.po.TransPayOrderPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface TransPayOrderPoMapper {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @SelectProvider(type = TransPayOrderPoSqlProvider.class, method = "countByExample")
    long countByExample(TransPayOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @DeleteProvider(type = TransPayOrderPoSqlProvider.class, method = "deleteByExample")
    int deleteByExample(TransPayOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Delete({
            "delete from tb_trans_pay_order",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Insert({
            "insert into tb_trans_pay_order (gmt_created, gmt_modified, ",
            "pay_order_no, sys_code, ",
            "biz_identify_no, biz_unique_no, ",
            "money, pay_channel_type, ",
            "target_account_type, accounting_entry, ",
            "gmt_accepted_time, gmt_payment_begin_time, ",
            "gmt_payment_success_time, gmt_payment_failure_time, ",
            "gmt_payment_closed_time, gmt_payment_bounced_time, ",
            "status, version, ",
            "memo, channel_payment_trace_no, ",
            "channel_dishonor_trace_no, channel_error_code, ",
            "business_entity, notify_uri, ",
            "extend_info, biz_info, ",
            "env)",
            "values (#{gmtCreated,jdbcType=TIMESTAMP}, #{gmtModified,jdbcType=TIMESTAMP}, ",
            "#{payOrderNo,jdbcType=BIGINT}, #{sysCode,jdbcType=VARCHAR}, ",
            "#{bizIdentifyNo,jdbcType=VARCHAR}, #{bizUniqueNo,jdbcType=VARCHAR}, ",
            "#{money,jdbcType=BIGINT}, #{payChannelType,jdbcType=INTEGER}, ",
            "#{targetAccountType,jdbcType=INTEGER}, #{accountingEntry,jdbcType=INTEGER}, ",
            "#{gmtAcceptedTime,jdbcType=TIMESTAMP}, #{gmtPaymentBeginTime,jdbcType=TIMESTAMP}, ",
            "#{gmtPaymentSuccessTime,jdbcType=TIMESTAMP}, #{gmtPaymentFailureTime,jdbcType=TIMESTAMP}, ",
            "#{gmtPaymentClosedTime,jdbcType=TIMESTAMP}, #{gmtPaymentBouncedTime,jdbcType=TIMESTAMP}, ",
            "#{status,jdbcType=INTEGER}, #{version,jdbcType=INTEGER}, ",
            "#{memo,jdbcType=VARCHAR}, #{channelPaymentTraceNo,jdbcType=VARCHAR}, ",
            "#{channelDishonorTraceNo,jdbcType=VARCHAR}, #{channelErrorCode,jdbcType=VARCHAR}, ",
            "#{businessEntity,jdbcType=VARCHAR}, #{notifyUri,jdbcType=VARCHAR}, ",
            "#{extendInfo,jdbcType=VARCHAR}, #{bizInfo,jdbcType=VARCHAR}, ",
            "#{env,jdbcType=INTEGER})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(TransPayOrderPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @InsertProvider(type = TransPayOrderPoSqlProvider.class, method = "insertSelective")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int insertSelective(TransPayOrderPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @SelectProvider(type = TransPayOrderPoSqlProvider.class, method = "selectByExample")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "gmt_created", property = "gmtCreated", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_modified", property = "gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "pay_order_no", property = "payOrderNo", jdbcType = JdbcType.BIGINT),
            @Result(column = "sys_code", property = "sysCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "biz_identify_no", property = "bizIdentifyNo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "biz_unique_no", property = "bizUniqueNo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "money", property = "money", jdbcType = JdbcType.BIGINT),
            @Result(column = "pay_channel_type", property = "payChannelType", jdbcType = JdbcType.INTEGER),
            @Result(column = "target_account_type", property = "targetAccountType", jdbcType = JdbcType.INTEGER),
            @Result(column = "accounting_entry", property = "accountingEntry", jdbcType = JdbcType.INTEGER),
            @Result(column = "gmt_accepted_time", property = "gmtAcceptedTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_begin_time", property = "gmtPaymentBeginTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_success_time", property = "gmtPaymentSuccessTime", jdbcType =
                    JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_failure_time", property = "gmtPaymentFailureTime", jdbcType =
                    JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_closed_time", property = "gmtPaymentClosedTime", jdbcType =
                    JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_bounced_time", property = "gmtPaymentBouncedTime", jdbcType =
                    JdbcType.TIMESTAMP),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "version", property = "version", jdbcType = JdbcType.INTEGER),
            @Result(column = "memo", property = "memo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "channel_payment_trace_no", property = "channelPaymentTraceNo", jdbcType =
                    JdbcType.VARCHAR),
            @Result(column = "channel_dishonor_trace_no", property = "channelDishonorTraceNo", jdbcType =
                    JdbcType.VARCHAR),
            @Result(column = "channel_error_code", property = "channelErrorCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "business_entity", property = "businessEntity", jdbcType = JdbcType.VARCHAR),
            @Result(column = "notify_uri", property = "notifyUri", jdbcType = JdbcType.VARCHAR),
            @Result(column = "extend_info", property = "extendInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "biz_info", property = "bizInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "env", property = "env", jdbcType = JdbcType.INTEGER)
    })
    List<TransPayOrderPo> selectByExample(TransPayOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Select({
            "select",
            "id, gmt_created, gmt_modified, pay_order_no, sys_code, biz_identify_no, biz_unique_no, ",
            "money, pay_channel_type, target_account_type, accounting_entry, gmt_accepted_time, ",
            "gmt_payment_begin_time, gmt_payment_success_time, gmt_payment_failure_time, ",
            "gmt_payment_closed_time, gmt_payment_bounced_time, status, version, memo, channel_payment_trace_no, ",
            "channel_dishonor_trace_no, channel_error_code, business_entity, notify_uri, ",
            "extend_info, biz_info, env",
            "from tb_trans_pay_order",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "gmt_created", property = "gmtCreated", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_modified", property = "gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "pay_order_no", property = "payOrderNo", jdbcType = JdbcType.BIGINT),
            @Result(column = "sys_code", property = "sysCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "biz_identify_no", property = "bizIdentifyNo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "biz_unique_no", property = "bizUniqueNo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "money", property = "money", jdbcType = JdbcType.BIGINT),
            @Result(column = "pay_channel_type", property = "payChannelType", jdbcType = JdbcType.INTEGER),
            @Result(column = "target_account_type", property = "targetAccountType", jdbcType = JdbcType.INTEGER),
            @Result(column = "accounting_entry", property = "accountingEntry", jdbcType = JdbcType.INTEGER),
            @Result(column = "gmt_accepted_time", property = "gmtAcceptedTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_begin_time", property = "gmtPaymentBeginTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_success_time", property = "gmtPaymentSuccessTime", jdbcType =
                    JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_failure_time", property = "gmtPaymentFailureTime", jdbcType =
                    JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_closed_time", property = "gmtPaymentClosedTime", jdbcType =
                    JdbcType.TIMESTAMP),
            @Result(column = "gmt_payment_bounced_time", property = "gmtPaymentBouncedTime", jdbcType =
                    JdbcType.TIMESTAMP),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "version", property = "version", jdbcType = JdbcType.INTEGER),
            @Result(column = "memo", property = "memo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "channel_payment_trace_no", property = "channelPaymentTraceNo", jdbcType =
                    JdbcType.VARCHAR),
            @Result(column = "channel_dishonor_trace_no", property = "channelDishonorTraceNo", jdbcType =
                    JdbcType.VARCHAR),
            @Result(column = "channel_error_code", property = "channelErrorCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "business_entity", property = "businessEntity", jdbcType = JdbcType.VARCHAR),
            @Result(column = "notify_uri", property = "notifyUri", jdbcType = JdbcType.VARCHAR),
            @Result(column = "extend_info", property = "extendInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "biz_info", property = "bizInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "env", property = "env", jdbcType = JdbcType.INTEGER)
    })
    TransPayOrderPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @UpdateProvider(type = TransPayOrderPoSqlProvider.class, method = "updateByExampleSelective")
    int updateByExampleSelective(@Param("record") TransPayOrderPo record,
            @Param("example") TransPayOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @UpdateProvider(type = TransPayOrderPoSqlProvider.class, method = "updateByExample")
    int updateByExample(@Param("record") TransPayOrderPo record, @Param("example") TransPayOrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @UpdateProvider(type = TransPayOrderPoSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TransPayOrderPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_trans_pay_order
     *
     * @mbg.generated Wed Jan 26 17:49:11 CST 2022
     */
    @Update({
            "update tb_trans_pay_order",
            "set gmt_created = #{gmtCreated,jdbcType=TIMESTAMP},",
            "gmt_modified = #{gmtModified,jdbcType=TIMESTAMP},",
            "pay_order_no = #{payOrderNo,jdbcType=BIGINT},",
            "sys_code = #{sysCode,jdbcType=VARCHAR},",
            "biz_identify_no = #{bizIdentifyNo,jdbcType=VARCHAR},",
            "biz_unique_no = #{bizUniqueNo,jdbcType=VARCHAR},",
            "money = #{money,jdbcType=BIGINT},",
            "pay_channel_type = #{payChannelType,jdbcType=INTEGER},",
            "target_account_type = #{targetAccountType,jdbcType=INTEGER},",
            "accounting_entry = #{accountingEntry,jdbcType=INTEGER},",
            "gmt_accepted_time = #{gmtAcceptedTime,jdbcType=TIMESTAMP},",
            "gmt_payment_begin_time = #{gmtPaymentBeginTime,jdbcType=TIMESTAMP},",
            "gmt_payment_success_time = #{gmtPaymentSuccessTime,jdbcType=TIMESTAMP},",
            "gmt_payment_failure_time = #{gmtPaymentFailureTime,jdbcType=TIMESTAMP},",
            "gmt_payment_closed_time = #{gmtPaymentClosedTime,jdbcType=TIMESTAMP},",
            "gmt_payment_bounced_time = #{gmtPaymentBouncedTime,jdbcType=TIMESTAMP},",
            "status = #{status,jdbcType=INTEGER},",
            "version = #{version,jdbcType=INTEGER},",
            "memo = #{memo,jdbcType=VARCHAR},",
            "channel_payment_trace_no = #{channelPaymentTraceNo,jdbcType=VARCHAR},",
            "channel_dishonor_trace_no = #{channelDishonorTraceNo,jdbcType=VARCHAR},",
            "channel_error_code = #{channelErrorCode,jdbcType=VARCHAR},",
            "business_entity = #{businessEntity,jdbcType=VARCHAR},",
            "notify_uri = #{notifyUri,jdbcType=VARCHAR},",
            "extend_info = #{extendInfo,jdbcType=VARCHAR},",
            "biz_info = #{bizInfo,jdbcType=VARCHAR},",
            "env = #{env,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(TransPayOrderPo record);
}