/**
 * @author Charles
 * @create 2019/3/29
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 日期格式化工具类 给定日期格式-->Date
     * @param startDate
     * @param format
     * @return
     */
    public static Date parse(String startDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 日期转字符串
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static void main(String[] args) {
        Date date = DateUtil.parse("20180101000000", "yyyyMMddHHmmss");
        System.out.println(date);
        System.out.println(DateUtil.format(date, "yyyyMMddHHmmss"));
    }


}
