package com.eoa.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ChengLong
 * @ClassName: DateUtils
 * @Description 时间工具类
 * @Date 2021/9/8 0008 10:41
 * @Version 1.0.0
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils{

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
    };

    /**
     * 获取当前Date类型日期
     * @return date 当前日期
     */
    public static Date getNowDate(){
        return new Date();
    }

    /**
     * 获取当前日期，默认格式yyyy-MM-dd
     * @return
     */
    public static String getDate(){
        return dateTimeNow(YYYY_MM_DD);
    }
    
    public static final String getTime(){
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }
    
    public static final String dateTimeNow(){
        return dateTimeNow(YYYYMMDDHHMMSS);
    }
    
    public static final String dateTimeNow(final String format){
        return parseDateToStr(format, new Date());
    }
    
    public static final String dateTime(final Date date){
        return parseDateToStr(YYYY_MM_DD, date);
    }
    
    public static final String parseDateToStr(final String format, final Date date){
        return new SimpleDateFormat(format).format(date);
    }
    
    public static final Date dateTime(final String format, final String ts){
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     * @return
     */
    public static final String datePath(){
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年月日 如20180808
     */
    public static final String dateTime(){
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期格式
     * @param str
     * @return
     */
    public static Date parseDate(Object str){
        if (str ==  null){
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e){
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     * @return
     */
    public static Date getServerStartDate(){
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2){
        return Math.abs((int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     * @param endDate
     * @param nowDate
     * @return
     */
    public static String getDatePoor(Date endDate, Date nowDate){
        long nd = 1000 * 24 * 60 * 60; 
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        //获取两个时间的毫秒差
        long diff = endDate.getTime() - nowDate.getTime();
        //计算相差多少天
        long day = diff / nd;
        //计算相差多少小时
        long hour = diff % nd / nh;
        //计算相差多少分钟
        long minute = diff % nd % nh / nm;
        return day + "天" + hour + "小时" + minute + "分钟";
    }
}
