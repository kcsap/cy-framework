package com.cy.framework.util.time;

import com.cy.framework.util.FinalConfigParam;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {

    private static final DateTimeFormatter YYYY = DateTimeFormat.forPattern("yyyy");
    private static final DateTimeFormatter MM = DateTimeFormat.forPattern("MM");
    private static final DateTimeFormatter DD = DateTimeFormat.forPattern("dd");
    private static final DateTimeFormatter YYYYMM = DateTimeFormat.forPattern("yyyyMM");
    private static final DateTimeFormatter YYYYMMDD = DateTimeFormat.forPattern("yyyyMMdd");
    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormat.forPattern("yyyyMMddHHmmssSSSSSS");
    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS_SSS = DateTimeFormat
            .forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter HH_MM_SS_SSS = DateTimeFormat.forPattern("HHmmssSSS");
    public final static Timestamp MAX_DATE = Timestamp.valueOf("2099-12-31 23:59:59");
    public static final String YYYY_MM_DD_HH_MM_SS_Str = "yyyy-MM-dd HH:mm:ss";

    /**
     * @return Timestamp
     */
    public static Timestamp getMaxTime() {
        return MAX_DATE;
    }

    /**
     * 时间转换string
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String date2string(Date date, String formatStr) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String sDate = format.format(date);
        return sDate;
    }

    /**
     * @param time
     * @return
     */
    public static String formatYYYY(long time) {
        return YYYY.print(time);
    }

    /**
     * @param time
     * @return
     */
    public static String formatMM(long time) {
        return MM.print(time);
    }

    /**
     * @param time
     * @return
     */
    public static String formatDD(long time) {
        return DD.print(time);
    }

    /**
     * @param time
     * @return
     */
    public static String formatYYYYMM(long time) {
        return YYYYMM.print(time);
    }

    /**
     * @param time
     * @return
     */
    public static String formatYYYMMDD(long time) {
        return YYYYMMDD.print(time);
    }

    /**
     * @param time
     * @return
     */
    public static String formatYYYYMMDDHHMMSS(long time) {
        return YYYYMMDDHHMMSS.print(time);
    }

    /**
     * 描述：
     *
     * @param time
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月19日 下午2:23:53
     */
    public static String formatYYYYMMDDHHMMSSSSS(long time) {
        return YYYY_MM_DD_HH_MM_SS_SSS.print(time);
    }

    public static String formatHHMMSSSSS(long time) {
        return HH_MM_SS_SSS.print(time);
    }

    public static Date parseYYYYMMDDHHMMSSSSS(String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        dateFormat.setTimeZone(FinalConfigParam.TIME_ZONE);
        Date date = null;
        try {
            date = dateFormat.parse(text);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return date;
    }

    public static Date parseDate(String text, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(FinalConfigParam.TIME_ZONE);
        Date date = null;
        try {
            date = dateFormat.parse(text);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return date;
    }

    /**
     * 在一个时间上加上对应的年
     *
     * @param ti long
     * @param i  int
     * @return Date
     * @throws Exception
     */
    public static Date addOrMinusYear(long ti, int i) throws Exception {
        Date rtn = null;
        GregorianCalendar cal = new GregorianCalendar();
        Date date = new Date(ti);
        cal.setTime(date);
        cal.add(GregorianCalendar.YEAR, i);
        rtn = cal.getTime();
        return rtn;
    }

    /**
     * 在一个时间上加上对应的月份数
     *
     * @param ti long
     * @param i  int
     * @return Date
     * @throws Exception
     */
    public static Date addOrMinusMonth(long ti, int i) throws Exception {
        Date rtn = null;
        GregorianCalendar cal = new GregorianCalendar();
        Date date = new Date(ti);
        cal.setTime(date);
        cal.add(GregorianCalendar.MONTH, i);
        rtn = cal.getTime();
        return rtn;
    }

    /**
     * 在一个时间上加上或减去周
     *
     * @param ti long
     * @param i  int
     * @return Date
     */
    public static Date addOrMinusWeek(long ti, int i) {
        Date rtn = null;
        GregorianCalendar cal = new GregorianCalendar();
        Date date = new Date(ti);
        cal.setTime(date);
        cal.add(GregorianCalendar.WEEK_OF_YEAR, i);
        rtn = cal.getTime();
        return rtn;
    }

    /**
     * 在一个时间上加上或减去天数
     *
     * @param ti long
     * @param i  int
     * @return Date
     */
    public static Date addOrMinusDays(long ti, int i) {
        Date rtn = null;
        GregorianCalendar cal = new GregorianCalendar();
        Date date = new Date(ti);
        cal.setTime(date);
        cal.add(GregorianCalendar.DAY_OF_MONTH, i);
        rtn = cal.getTime();
        return rtn;
    }

    /**
     * 在一个时间上加上或减去小时
     *
     * @param ti long
     * @param i  int
     * @return Date
     */
    public static Date addOrMinusHours(long ti, int i) {
        Date rtn = null;
        GregorianCalendar cal = new GregorianCalendar();
        Date date = new Date(ti);
        cal.setTime(date);
        cal.add(GregorianCalendar.HOUR, i);
        rtn = cal.getTime();
        return rtn;
    }

    /**
     * 在一个时间上加上或减去分钟
     *
     * @param ti long
     * @param i  int
     * @return Date
     */
    public static Date addOrMinusMinutes(long ti, int i) {
        Date rtn = null;
        GregorianCalendar cal = new GregorianCalendar();
        Date date = new Date(ti);
        cal.setTime(date);
        cal.add(GregorianCalendar.MINUTE, i);
        rtn = cal.getTime();
        return rtn;
    }

    /**
     * 在一个时间上加上或减去秒数
     *
     * @param ti long
     * @param i  int
     * @return Date
     */
    public static Date addOrMinusSecond(long ti, int i) {
        Date rtn = null;
        GregorianCalendar cal = new GregorianCalendar();
        Date date = new Date(ti);
        cal.setTime(date);
        cal.add(GregorianCalendar.SECOND, i);
        rtn = cal.getTime();
        return rtn;
    }

    /**
     * 两个日期之间的年数
     *
     * @param start Date
     * @param end   Date
     * @return int
     */
    public static int yearsBetween(Date start, Date end) {
        return Years.yearsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getYears();
    }

    /**
     * 两个日期之间的月数
     *
     * @param start Date
     * @param end   Date
     * @return int
     */
    public static int monthsBetween(Date start, Date end) {
        return Months.monthsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getMonths();
    }

    /**
     * 两个日期之间的周数
     *
     * @param start Date
     * @param end   Date
     * @return int
     */
    public static int weeksBetween(Date start, Date end) {
        return Weeks.weeksBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getWeeks();
    }

    /**
     * 两个日期之间的天数
     *
     * @param start Date
     * @param end   Date
     * @return int
     */
    public static int daysBetween(Date start, Date end) {
        return Days.daysBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getDays();
    }

    /**
     * 两个日期之间的小时数
     *
     * @param start Date
     * @param end   Date
     * @return int
     */
    public static int hoursBetween(Date start, Date end) {
        return Hours.hoursBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getHours();
    }

    /**
     * 两个日期之间的分钟数
     *
     * @param start Date
     * @param end   Date
     * @return int
     */
    public static int minutesBetween(Date start, Date end) {
        return Minutes.minutesBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getMinutes();
    }

    /**
     * 两个日期之间的秒数
     *
     * @param start Date
     * @param end   Date
     * @return int
     */
    public static int secondsBetween(Date start, Date end) {
        return Seconds.secondsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getSeconds();
    }

    /**
     * 根据指定的日期获取下个月的第一天的时间
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp getNextMonthStartDate(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.DAY_OF_MONTH, 1);
        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.MILLISECOND, 0);
        rightNow.set(Calendar.SECOND, 0);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH) + 1);
        return new Timestamp(rightNow.getTimeInMillis());
    }

    /**
     * 根据指定的日期获取上个月的第一天的时间
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp getBeforeMonthStartDate(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.DAY_OF_MONTH, 1);
        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.MILLISECOND, 0);
        rightNow.set(Calendar.SECOND, 0);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH) - 1);
        return new Timestamp(rightNow.getTimeInMillis());
    }

    /**
     * 根据指定日期获取该月的最后一天的最后时间点
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp getCurrentMonthEndDate(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.DAY_OF_MONTH, rightNow.getActualMaximum(Calendar.DAY_OF_MONTH));
        rightNow.set(Calendar.HOUR_OF_DAY, 23);
        rightNow.set(Calendar.MILLISECOND, 59);
        rightNow.set(Calendar.SECOND, 59);
        rightNow.set(Calendar.MINUTE, 59);
        rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
        return new Timestamp(rightNow.getTimeInMillis());
    }

    /**
     * 根据指定日期获取该月的第一天的开始时间点
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp getCurrentMonthFirstDate(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.DAY_OF_MONTH, 1);
        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.MILLISECOND, 0);
        rightNow.set(Calendar.SECOND, 0);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
        return new Timestamp(rightNow.getTimeInMillis());
    }

    /**
     * 根据指定日期获取当天的最后的时间点
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp getCurrentDayEndDate(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.HOUR_OF_DAY, 23);
        rightNow.set(Calendar.MILLISECOND, 59);
        rightNow.set(Calendar.SECOND, 59);
        rightNow.set(Calendar.MINUTE, 59);
        rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
        return new Timestamp(rightNow.getTimeInMillis());
    }

    /**
     * 根据指定日期获取当天的开始的时间点
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp getCurrentDayStartDate(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.MILLISECOND, 0);
        rightNow.set(Calendar.SECOND, 0);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
        return new Timestamp(rightNow.getTimeInMillis());
    }

    /**
     * 根据指定日期获取前一天的最后的时间点
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp getBeforeDayEndDate(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.DAY_OF_MONTH, rightNow.get(Calendar.DAY_OF_MONTH) - 1);
        rightNow.set(Calendar.HOUR_OF_DAY, 23);
        rightNow.set(Calendar.MILLISECOND, 59);
        rightNow.set(Calendar.SECOND, 59);
        rightNow.set(Calendar.MINUTE, 59);
        rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
        return new Timestamp(rightNow.getTimeInMillis());
    }

    /**
     * 根据指定日期获取下一天的开始的时间点
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp getNextDayStartDay(Date date) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.set(Calendar.DAY_OF_MONTH, rightNow.get(Calendar.DAY_OF_MONTH) + 1);
        rightNow.set(Calendar.HOUR_OF_DAY, 0);
        rightNow.set(Calendar.MILLISECOND, 0);
        rightNow.set(Calendar.SECOND, 0);
        rightNow.set(Calendar.MINUTE, 0);
        rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
        return new Timestamp(rightNow.getTimeInMillis());
    }

    /**
     * 是否在区间内(前闭后闭)
     *
     * @param date
     * @param closeStart
     * @param closeEnd
     * @return
     */
    public static boolean isBetween(Date date, Date closeStart, Date closeEnd) {
        boolean rtn = false;

        long time = date.getTime();
        if (time >= closeStart.getTime() && time <= closeEnd.getTime()) {
            rtn = true;
        }
        return rtn;
    }

    /**
     * 是否在区间内(前闭后闭)
     *
     * @param date
     * @param closeStart
     * @param closeEnd
     * @return
     */
    public static boolean isBetween(long date, long closeStart, long closeEnd) {
        boolean rtn = false;

        if (date >= closeStart && date <= closeEnd) {
            rtn = true;
        }
        return rtn;
    }

    /**
     * @param time
     * @return
     */
    public static Date parseYYYYMM(String time) {
        return YYYYMM.parseDateTime(time).toDate();
    }

    /**
     * @param time
     * @return
     */
    public static Date parseYYYYMMDD(String time) {
        return YYYYMMDD.parseDateTime(time).toDate();
    }

    /**
     * 描述：
     *
     * @param time
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月16日 下午2:19:39
     */
    public static Date parseYYYY_MM_DD(String time) {
        return YYYY_MM_DD.parseDateTime(time).toDate();
    }

    /**
     * @param time
     * @return
     */
    public static Date parseYYYYMMDDHHMMSS(String time) {
        return YYYYMMDDHHMMSS.parseDateTime(time).toDate();
    }

    /**
     * @param time
     * @return
     */
    public static Date parseYYYY_MM_DD_HH_MM_SS(String time) {
        return YYYY_MM_DD_HH_MM_SS.parseDateTime(time).toDate();
    }

    /**
     * 获得上个季度的当前时间
     *
     * @param date
     * @return
     */
    public static Date getLastQuarter(Date date) {
        DateTime time = new DateTime(date.getTime());
        return time.minusMonths(3).toDate();
    }

    /**
     * 获得上个月的当前时间
     *
     * @param date
     * @return
     */
    public static Date getLastMonth(Date date) {
        DateTime time = new DateTime(date.getTime());
        return time.minusMonths(1).toDate();
    }

    /**
     * 计算月份差，格式化为1号比较
     *
     * @param d1
     * @param d2
     * @return d1 > d2 返回负数 -1 ；d1 < d2 返回正数 1 ；如果是同个月，返回0
     */
    public static int monthsDiff(Date d1, Date d2) {
        Date start = TimeUtil.getCurrentMonthFirstDate(d1);
        Date end = TimeUtil.getCurrentMonthFirstDate(d2);
        return Months.monthsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getMonths();
    }

    /**
     * 获得系统当前时间
     *
     * @return
     */
    public static Timestamp getSysDate() {
        /** todo 缓存多少秒后每次取差值计算后得到真实值 */
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获得帐期的开始时间
     *
     * @param billMonth
     * @return
     */
    public static Timestamp getBillMonthStartDate(long billMonth) {
        Date billMonthDate = TimeUtil.parseYYYYMM(String.valueOf(billMonth));
        return getCurrentMonthFirstDate(billMonthDate);
    }

    /**
     * 获得帐期的结束时间
     *
     * @param billMonth
     * @return
     */
    public static Timestamp getBillMonthEndDate(long billMonth) {
        Date billMonthDate = TimeUtil.parseYYYYMM(String.valueOf(billMonth));
        return getCurrentMonthEndDate(billMonthDate);
    }

    /**
     * 根据传入的日期字符串转换成相应的日期对象， 如果字符串为空或不符合日期格式，则返回当前时间。 FrameWork使用
     *
     * @param stdDate String 日期字符串
     * @return java.sql.Timestamp 日期对象
     */
    public static Date getDateByString(String stdDate) {
        if (null == stdDate || stdDate.trim().equals("")) {
            return TimeUtil.getSysDate();
        }
        try {
            if (stdDate.contains(".0")) {
                stdDate = stdDate.replace(".0", "");
            }
            return YYYY_MM_DD_HH_MM_SS.parseDateTime(stdDate).toDate();
        } catch (Exception ex) {
            return TimeUtil.getSysDate();
        }
    }

    /**
     * 将日期格式化成String类型
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatTime(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(FinalConfigParam.TIME_ZONE);
        return dateFormat.format(date);
    }
}
