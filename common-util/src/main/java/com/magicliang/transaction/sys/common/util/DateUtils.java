package com.magicliang.transaction.sys.common.util;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 日期工具类
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-30 17:12
 */
public class DateUtils {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDD_ZH = "yyyy年MM月dd日";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYYYMM_FORMAT = "yyyyMM";
    public static final String HHMMSS = "HH:mm:ss";
    public static final Date expireDate = new Date(2999, 1, 1);
    /**
     * 一天的秒数
     */
    public static final int ONE_DAY_IN_SECONDS = 60 * 60 * 24;
    /**
     * 一天的最后一秒
     */
    public static final String LAST_SECOND_OF_ONE_DAY = " 23:59:59";
    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 私有构造器
     */
    private DateUtils() {
        throw new UnsupportedOperationException();
    }

    public static Date removeMill(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(String smdate, Date bdate) {
        long between_days = -1;
        try {
            if (null != smdate && (!smdate.equals(""))) {
                SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
                Date smdateNew = sdf.parse(smdate);
                bdate = sdf.parse(sdf.format(bdate));
                Calendar cal = Calendar.getInstance();
                cal.setTime(smdateNew);
                long time1 = cal.getTimeInMillis();
                cal.setTime(bdate);
                long time2 = cal.getTimeInMillis();
                between_days = (time2 - time1) / (1000 * 3600 * 24);
            }
        } catch (ParseException e) {
            return -1;
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param first 开始时间
     * @param last 结束时间
     * @return 相差年数
     */
    public static int yearsBetween(Date first, Date last) {
        if (first == null || last == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) || (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatDate(Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD);
        return sf.format(date);
    }

    public static String formatDateTime(Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return sf.format(date);
    }

    /**
     * 格式化日期为字符串
     *
     * @param date 日期
     * @param pattern 格式，如"yyyy-MM-dd HH:mm:ss"
     * @return 字符串形式的日期
     */
    public static String convertDateToString(Date date, String pattern) {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (date != null) {
            result = sdf.format(date);
        }
        return result;
    }

    /**
     * 格式化字符串为日期
     *
     * @param date 日期
     * @param pattern 格式，如"yyyy-MM-dd HH:mm:ss"
     * @return 字符串形式的日期
     */
    public static Date convertStringToDate(String date, String pattern) {
        Date result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (date != null) {
            try {
                sdf.setLenient(false);
                result = sdf.parse(date);
            } catch (ParseException e) {
//                ExceptionUtil.logError(LOGGER,"解析异常", e);
                throw new UnsupportedOperationException(e);
            }
        }
        return result;
    }

    /**
     * 日期格式转换
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date covertDateToDate(Date date, String pattern) {
        String patternStr = convertDateToString(date, pattern);
        return convertStringToDate(patternStr, pattern);
    }

    /**
     * 转换毫秒到日期
     *
     * @param mills 必须大于0 否则返回null
     * @return
     */
    public static Date convertMills2Date(long mills) {
        if (mills <= 0) {
            return null;
        }
        return new Date(mills);
    }


    /**
     * long time 转格式字符串
     */
    public static String convertTimeToString(Long time, String formats) {
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern(formats);
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    /**
     * 获得指定日期的下 N 天的日期
     *
     * @param startDate
     * @return
     */
    public static Date getNextNumDay(Date startDate, int num) {
        if (num == 0) {
            return startDate;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }

    /**
     * 获得指定日期的下 N 年的日期
     *
     * @param startDate
     * @return
     */
    public static Date getNextNumYear(Date startDate, int num) {
        if (num == 0) {
            return startDate;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, num);
        return calendar.getTime();
    }

    /**
     * 增加秒到日期
     *
     * @param startDate
     * @param num
     * @return
     */
    public static Date addSecond2Date(Date startDate, int num) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(Calendar.SECOND, num);
        return calendar.getTime();
    }

    /**
     * 将时间转换为文本 yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static boolean between(Date start, Date end, Date time) {
        return start.getTime() <= time.getTime() && time.getTime() <= end.getTime();
    }

    public static boolean isSameDay(final Calendar cal1, final Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 检查是否是相同天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(final Date date1, final Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }

    /**
     * 计算时间差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Long caculateTimeIntervalMinute(Date date1, Date date2) {
        return Long.valueOf(caculateTimeIntervalSecond(date1, date2).longValue() / 60L);
    }


    public static Long caculateTimeIntervalDay(Date date1, Date date2) {
        return Long.valueOf(caculateTimeIntervalMinute(date1, date2).longValue() / 60L / 24);
    }

    public static Long caculateTimeIntervalSecond(Date date1, Date date2) {
        Long interval = null;
        interval = Long.valueOf((date2.getTime() - date1.getTime()) / 1000L);
        return interval;
    }

    /**
     * 设置某天的准确时间, 时、分、秒、毫秒
     *
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date setExactTime(Date date, int hour, int minute, int second, int millisecond) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        // getInstance() returns a new object, so this method is thread safe.
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        // 设置时
        c.set(Calendar.HOUR_OF_DAY, hour);
        // 设置分
        c.set(Calendar.MINUTE, minute);
        // 设置秒
        c.set(Calendar.SECOND, second);
        // 设置毫秒
        c.set(Calendar.MILLISECOND, millisecond);

        return c.getTime();
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatSS(Date date) {
        return DateFormatUtils.format(date, YYYY_MM_DD_HH_MM_SS);
    }

    public static Date parseYYYYMMDD(String dateStr) {
        return parseDate(dateStr, YYYYMMDD);
    }

    public static Date parseYYYY_MM_DD(String dateStr) {
        return parseDate(dateStr, YYYY_MM_DD);
    }

    public static Date parseYYYYMMDDHHMMSS(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            log.error("DateUtils parseYYYYMMDDHHMMSS error,dateStr:{}", dateStr, e);
        }
        return null;
    }

    /**
     * yyyyMM
     *
     * @param date
     * @return
     */
    public static String formatYYYYMM(Date date) {
        if (null == date) {
            return StringUtils.EMPTY;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMM_FORMAT);
        return sdf.format(date);
    }

    /**
     * 返回今天0时的时间戳
     *
     * @param
     * @return
     */
    public static long getTodayMill() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 返回昨日0时的时间戳
     *
     * @param
     * @return
     */
    public static long getYestdayMill() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.getTimeInMillis();
    }

    /**
     * 返回次日0时的时间戳
     *
     * @param
     * @return
     */
    public static long getTomorrowMill() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return cal.getTimeInMillis();
    }

    /**
     * 返回某个时候天数后的时间戳
     *
     * @param
     * @return
     */
    public static Date getAfterDate(Date date, int afterDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, afterDay);
        return cal.getTime();
    }

    /**
     * 返回某日0时的时间戳
     *
     * @param
     * @return
     */
    public static long getSomeDayMill(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 返回今天23时的时间戳
     *
     * @param
     * @return
     */
    public static long getNightMill() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 返回某日23时的时间戳
     *
     * @param cal
     * @return
     */
    public static long getSomeDayNightMill(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 返回指定一年之后的时间戳
     *
     * @return
     */
    public static long getMillAfterYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, 364);
        return cal.getTimeInMillis();
    }

    /**
     * 格式化处理
     *
     * @return
     */
    public static String formatYYYY_MM_DD(String day) {
        return DateFormatUtils.format(parseYYYYMMDD(day), YYYY_MM_DD);
    }

    /**
     * 格式化处理
     *
     * @return
     */
    public static String formatYYYY_MM_DD(long time) {
        return DateFormatUtils.format(new Date(time), YYYY_MM_DD);
    }

    /**
     * 格式化处理
     *
     * @return
     */
    public static String formatYYYY_MM_DD_HH_MM(long time) {
        return DateFormatUtils.format(new Date(time), "yyyy-MM-dd HH:mm");
    }

    /**
     * 返回指定今天时间
     *
     * @return
     */
    public static int getToday() {
        return Integer.parseInt((DateFormatUtils.format(new Date(), YYYYMMDD)));
    }

    /**
     * 返回昨天int日期
     *
     * @return
     */
    public static int getYestDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return Integer.parseInt(DateFormatUtils.format(cal.getTime(), YYYYMMDD));
    }

    public static int getLastDayOfMonth(int dateInt) {
        Calendar cal = Calendar.getInstance();
        Date date = parseYYYYMMDD(dateInt + "");
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return Integer.parseInt(DateFormatUtils.format(cal.getTime(), YYYYMMDD));
    }

    public static Date getLastDayOfMonthDate(int dateInt) {
        Calendar cal = Calendar.getInstance();
        Date date = parseYYYYMMDD(dateInt + "");
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 校验是否是指定月份的第n天
     *
     * @param date
     * @param dayOfMonth
     * @return
     */
    public static boolean checkDayOfMonth(Date date, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return DateFormatUtils.format(cal.getTime(), YYYYMMDD).equals(DateFormatUtils.format(date, YYYYMMDD));
    }

    /**
     * 指定每周第一天，获取一年中的第几周
     *
     * @param date
     * @param firstDayOfWeek @link Calendar.SUNDAY
     * @return
     */
    public static int getWeekNumByFirstDayOfWeekInYear(Date date, int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        return calendar.get(java.util.Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(YEAR);
    }

    /**
     * 获取一个月中的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfMonthByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取前一个月
     *
     * @param date
     * @return
     */
    public static String getPreMonth(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.convertStringToDate(date, DateUtils.YYYYMM_FORMAT));
        calendar.add(Calendar.MONTH, -1);
        return DateUtils.formatYYYYMM(calendar.getTime());
    }

    public static Date getDateByDateInt(int dateInt) {
        Date date = parseYYYYMMDD(dateInt + "");
        return date;
    }

    public static int getFirstDayOfNextMonth(int dateInt) {
        Calendar cal = Calendar.getInstance();
        Date date = parseYYYYMMDD(dateInt + "");
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        return Integer.parseInt(DateFormatUtils.format(cal.getTime(), YYYYMMDD));
    }

    public static Date getNextMonthFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    public static Date getNextMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 2);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static int differentDaysByMillisecond(long startTime, long afterTime) {
        int days = Math.toIntExact(((afterTime - startTime) / (1000 * 3600 * 24))) + 1;
        return days;
    }

    public static Integer yyyyMMddToInt(Date date) {
        return Integer.parseInt((DateFormatUtils.format(date, YYYYMMDD)));
    }

    public static String formatFromYYYYMMDD(Date date) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat yymmddFormat = new SimpleDateFormat(YYYYMMDD);
            return yymmddFormat.format(date);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    public static Date formatFromLong(long time) {
        if (0 == time) {
            return null;
        }
        try {
            SimpleDateFormat YYYY_MM_DD_HH_MM_SS_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            String stringDate = YYYY_MM_DD_HH_MM_SS_FORMAT.format(time);
            Date date = YYYY_MM_DD_HH_MM_SS_FORMAT.parse(stringDate);
            return date;
        } catch (ParseException e) {

        }
        return null;
    }

    public static String formatStrFromLong(long time) {
        if (0 == time) {
            return null;
        }
        try {
            SimpleDateFormat YYYY_MM_DD_HH_MM_SS_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            String stringDate = YYYY_MM_DD_HH_MM_SS_FORMAT.format(time);
            return stringDate;
        } catch (Exception e) {

        }
        return null;
    }

    public static Date parseDate(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            log.error("DateUtils parseDate error,dateStr:{}", dateStr, e);
        }
        return null;
    }

    /**
     * 达到今天的 0 点 0 分 0 秒 0 毫秒
     *
     * @param date 原始日期
     * @return 实际日期
     */
    public static Date toZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    public static Date getNow() {
        // 日后实现时光偏移，在这里偏移
        return new Date();
    }

    public static String getCurrentDateStr() {
        return convertDateToString(getNow(), YYYY_MM_DD);
    }


    public static Date formatYYYYMMDDHHMMSSToDate(String dateTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);
        LocalDateTime ldt = LocalDateTime.parse(dateTime, dtf);
        DateTimeFormatter fa = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return convertStringToDate(ldt.format(fa), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取月缴的止期，不带有时分秒
     *
     * @param effectiveTime 起期，格式 yyyy-MM-dd
     * @return 下个月的对应日期
     */
    public static String getMonthlyExpirationDate(final String effectiveTime) {
        return convertDateToString(getMonthlyExpirationDateTime(convertStringToDate(effectiveTime, YYYY_MM_DD)),
                YYYY_MM_DD);
    }

    /**
     * 获取月缴的止期，不带有时分秒
     *
     * @param effectiveTime 起期，格式 yyyy-MM-dd
     * @return 下个月的对应日期
     */
    public static String getYearlyExpirationDate(final String effectiveTime) {
        return convertDateToString(getYearlyExpirationDateTime(convertStringToDate(effectiveTime, YYYY_MM_DD)),
                YYYY_MM_DD);
    }

    /**
     * 获取月缴的止期，带有时分秒
     *
     * @param effectiveTime 起期
     * @return 下个月的对应日期
     */
    public static Date getMonthlyExpirationDateTime(final Date effectiveTime) {
        final Date effectiveDate = convertStringToDate(
                convertDateToString(effectiveTime, YYYY_MM_DD) + LAST_SECOND_OF_ONE_DAY, YYYY_MM_DD_HH_MM_SS);
        // 加一月，减一天
        return getNextNumDay(getNextNumMonth(effectiveDate, 1), -1);
    }

    /**
     * 获取月缴的止期，带有时分秒
     *
     * @param effectiveTime 起期
     * @return 下个月的对应日期
     */
    public static Date getYearlyExpirationDateTime(final Date effectiveTime) {
        final Date effectiveDate = convertStringToDate(
                convertDateToString(effectiveTime, YYYY_MM_DD) + LAST_SECOND_OF_ONE_DAY, YYYY_MM_DD_HH_MM_SS);
        // 加十二月，减一天
        return getNextNumDay(getNextNumMonth(effectiveDate, 12), -1);
    }

    /**
     * 给原始日期加上若干月
     *
     * @param date 原始日期
     * @return 计算后得到的日期结果，带有时分秒
     */
    public static Date getNextNumMonth(final Date date, int additionalMonth) {
        final Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, additionalMonth);
        return calendar.getTime();
    }

    /**
     * 给原始日期加上若干月
     * 2020-01-31 -> 2020-02-29
     * 2020-10-31 -> 2020-11-30
     * 2020-11-30 -> 2020-12-30
     *
     * @param str 原始日期，格式 yyyy-MM-dd
     * @return 计算后得到的日期结果，只有日期
     */
    public static String getNextNumMonth(final String str, int additionalMonth) {
        final Date date = convertStringToDate(str, YYYY_MM_DD);
        final Date resultDate = getNextNumMonth(date, additionalMonth);
        return convertDateToString(resultDate, YYYY_MM_DD);
    }

    /**
     * 是不是代扣第 [begin - end]天
     *
     * @param withholdDate 代扣日期
     * @param begin 代扣日期
     * @param end 代扣日期
     * @return 是不是代扣第 x 天
     */
    public static boolean isWithholdDateInRange(final int withholdDate, final int begin, final int end) {
        if (begin < end) {
            throw new IllegalArgumentException("invalid begin, begin must be greater than end");
        }
        return IntStream.range(end, begin + 1).anyMatch((i) -> isWithholdDateWithDelta(withholdDate, i));
    }

    /**
     * 是不是代扣第 x 天
     *
     * @param withholdDate 代扣日期
     * @return 是不是代扣第 x 天
     */
    public static boolean isWithholdDateWithDelta(int withholdDate, int delta) {
        return Objects.equals(withholdDate, convertToInteger(java.time.LocalDate.now().minusDays(delta)));
    }

    /**
     * 转化日期到数字，如 20201011
     *
     * @param localDate 日期
     * @return 数字
     */
    public static Integer convertToInteger(java.time.LocalDate localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYYMMDD);
        String localDateStr = localDate.format(dateTimeFormatter);
        return Integer.parseInt(localDateStr);
    }

    public static long getSafeTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        return date.getTime();
    }

    /**
     * 根据错误次数计算修改时间
     *
     * @param errorCount
     * @return
     */
    public static Date calculateNextExeTime(int errorCount) {
        Date now = new Date();
        int addSecond = errorCount * 300;
        Date modifyTime = addSecond2Date(now, addSecond);
        return modifyTime;
    }

    /**
     * 计算 unix 的秒级时间戳的流失时间
     *
     * @param unixTimestampSeconds unix 的秒级时间戳
     * @return java date
     */
    public static long elapsedFromUnixTimestamp(long unixTimestampSeconds) {
        final Date openDate = DateUtils.fromUnixTimestamp(unixTimestampSeconds);
        Date today = new Date();
        return DateUtils.caculateTimeIntervalDay(openDate, today);
    }


    /**
     * 把 unix 的秒级时间戳转换为 java date
     *
     * @param unixTimestampSeconds unix 的秒级时间戳
     * @return java date
     */
    public static Date fromUnixTimestamp(long unixTimestampSeconds) {
        Instant instant = Instant.ofEpochSecond(unixTimestampSeconds);
        return Date.from(instant);
    }
}
