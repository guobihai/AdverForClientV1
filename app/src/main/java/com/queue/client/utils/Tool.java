package com.queue.client.utils;

import java.io.DataOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Tool {

    public static String praseDate(Date date){
        String str = "";
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        str = sim.format(date);
        return str;
    }

    public static String praseDateToYYYYMMDD(Date date){
        String str = "";
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
        str = sim.format(date);
        return str;
    }

    /**
     * 获取系统时间：格式为yyMMddHHMMss
     *
     * @return
     */
    public static String getSystemTime() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
        str = sim.format(date);
        return str;
    }

    /**
     * 获取系统时间：格式为HHMMss
     *
     * @return
     */
    public static String getSystemHourAndMin() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("HHmmss");
        str = sim.format(date);
        return str;
    }

    /**
     * 获取系统时间 格式：yyMMdd
     *
     * @return
     */
    public static int getSystemTimeForVerify() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyMMdd");
        str = sim.format(date);
        return Integer.parseInt(str);
    }

    public static String getDealListTime() {
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat1.format(new Date());
    }

    /**
     * 转换时间格式
     *
     * @param time 接收的时间格式
     * @return 转为 yyMMddHHmmss
     */
    public static String chanceTime(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date2 = dateFormat1.parse(time);
            return dateFormat.format(date2);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 设置系统时间，必须有root权限
     *
     * @param newTime 时间格式为20141013.1056
     * @return 设置是否成功
     */
    public static int SetSystemTime(String newTime) {
        try {
            Process progress = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(
                    progress.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone Asia/Shanghai\n");// 设置成中国的时区
            os.writeBytes("/system/bin/date -s " + newTime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (Exception e) {
            // TODO: handle exception
            return 1;
        }
        return 0;
    }

    /**
     * 获取昨天的日期
     *
     * @return
     */
    public static String getLastDateForDb() {
        String str = "";
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);// 把日期加一为后一天，把日期减一为前一天
        date = calendar.getTime();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        str = sim.format(date);
        return str;
    }

    /**
     * 获取系统时间：格式为yyMMddHHMMss
     *
     * @return
     */
    public static String getCurrentDateForDb() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        str = sim.format(date);
        return str;
    }




    public static String getCurrentYMD() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
        str = sim.format(date);
        return str;
    }

    public static String getCurrentYM() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月");
        str = sim.format(date);
        return str;
    }

    public static String getCurrentYMDHHmmss() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        str = sim.format(date);
        return str;
    }

    /**
     * 获取当前有效期比较时间
     *
     * @return
     */
    public static String getYouXiaoQi() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
        str = sim.format(date);
        return str;
    }

    /**
     * 获取当前日期是星期几
     */

    public static String getWeekOfDate(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int W = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (W < 0) {
            W = 0;
        }
        return weeks[W];
    }

    public static String getDateAndWeek() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
        str = sim.format(date) + "  " + getWeekOfDate(date);
        return str;
    }

    /**
     * 获取今天是星期几
     *
     * @return
     */
    public static int getCurrentDayForWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
        } else {
            day--;
        }
        return day;
    }

    /**
     * 获取本周起至日期
     *
     * @return
     */
    public static String[] getStartEndDateByWeek() {

        String[] Dates = new String[2];

        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        calendar.add(Calendar.DATE, -day_of_week);

        Dates[0] = sim.format(calendar.getTime());

        calendar.add(Calendar.DATE, 6);
        Dates[1] = sim.format(calendar.getTime());

        return Dates;
    }

    /**
     * 获取本月起至日期
     *
     * @return
     */
    public static String[] getStartEndDateByMonth() {
        String[] Dates = new String[2];

        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Dates[0] = sim.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Dates[1] = sim.format(calendar.getTime());
        return Dates;
    }

    public static HashMap<String, Integer> getDate() {
        String[] date = getStartEndDateByWeek();

        HashMap<String, Integer> maps = new HashMap<String, Integer>();
        String start = date[0];
        String[] s = start.split("-");

        String end = date[0];
        String[] m = start.split("-");

        maps.put("start_year", Integer.parseInt(s[0]));
        maps.put("start_month", Integer.parseInt(s[1]));
        maps.put("start_day", Integer.parseInt(s[2]));

        maps.put("end_year", Integer.parseInt(m[0]));
        maps.put("end_month", Integer.parseInt(m[1]));
        maps.put("end_day", Integer.parseInt(m[2]));

        return maps;
    }

    public static int[][] getStartEndDate(String[] dates) {
        String start = dates[0];// 开始日期
        String[] s = start.split("-");// 分开后 ： 年 月 日

        String end = dates[1];// 开始日期
        String[] e = end.split("-");// 分开后 ： 年 月 日
        int[][] strs = {
                {Integer.parseInt(s[0]), Integer.parseInt(s[1]),
                        Integer.parseInt(s[2])},
                {Integer.parseInt(e[0]), Integer.parseInt(e[1]),
                        Integer.parseInt(e[2])}};
        return strs;
    }

    /**
     * 转换时间格式
     *
     * @param time 接收的时间格式
     * @return 转为 yyMMddHHmmss
     */
    public static String chanceTime1(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        DateFormat dateFormat1 = new SimpleDateFormat("yyMMddHHmmss");
        try {
            Date date2 = dateFormat1.parse(time);
            return dateFormat.format(date2);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String getSystemHourMM() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("HHmm");
        str = sim.format(date);
        return str;
    }

    public static String getBirthDay() {
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("MM-dd");
        str = sim.format(date);
        return str;
    }


    /**
     * 获取当前年月日，星期
     * @return
     */
    public static String getCurDayAndWeek() {
        return getCurrentYMD() + " " + getWeekOfDate(new Date());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurTime(){
        String str = "";
        Date date = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("HH:mm:ss");
        str = sim.format(date);
        return str;
    }
}

