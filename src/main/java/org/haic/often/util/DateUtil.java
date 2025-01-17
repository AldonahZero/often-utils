package org.haic.often.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author haicdust
 * @version 1.0
 * @since 2022/10/25 13:45
 */
public class DateUtil {

	/**
	 * 判断time是否在from，to之内
	 *
	 * @param time 指定日期
	 * @param from 开始日期
	 * @param to   结束日期
	 * @return 判断结果
	 */
	public static boolean belongCalendar(Date time, Date from, Date to) {
		Calendar date = Calendar.getInstance();
		date.setTime(time);
		Calendar after = Calendar.getInstance();
		after.setTime(from);
		Calendar before = Calendar.getInstance();
		before.setTime(to);
		return date.after(after) && date.before(before);
	}

	/**
	 * 判断time是否在now的n天之内
	 *
	 * @param time 指定日期
	 * @param now  指定日期
	 * @param n    正数表示在条件时间n天之后，负数表示在条件时间n天之前
	 * @return 判断结果
	 */
	public static boolean belongDate(Date time, Date now, int n) {
		Calendar calendar = Calendar.getInstance();  //得到日历
		calendar.setTime(now);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, n);
		return calendar.getTime().getTime() < time.getTime();
	}

	/**
	 * 判断给定时间与当前时间相差多少天
	 *
	 * @param time 时间
	 * @return 判断结果
	 */
	public static long getDistanceDays(long time) {
		return getDistanceDays(new Date(time));
	}

	/**
	 * 判断给定时间与当前时间相差多少天
	 *
	 * @param time 时间
	 * @return 判断结果
	 */
	public static long getDistanceDays(Date time) {
		return (time.getTime() - new Date().getTime()) / 86400000;
	}

	/**
	 * 判断是不是同一天
	 *
	 * @param frist  第一个时间
	 * @param second 第二个时间
	 * @return 判断结果
	 */
	public static boolean sameDay(Date frist, Date second) {
		return sameDay(frist.getTime(), second.getTime());
	}

	/**
	 * 判断是不是同一天
	 *
	 * @param frist  第一个时间
	 * @param second 第二个时间
	 * @return 判断结果
	 */
	public static boolean sameDay(long frist, long second) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(frist);
		int d = instance.get(Calendar.DAY_OF_YEAR);
		instance.setTimeInMillis(second);
		return d == instance.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 判断日期是否是今天
	 *
	 * @param time 日期
	 * @return 判断结果
	 */
	public static boolean isToday(long time) {
		return isToday(new Date(time));
	}

	/**
	 * 判断日期是否是今天
	 *
	 * @param time 日期
	 * @return 判断结果
	 */
	public static boolean isToday(Date time) {
		return isThisTime(time, "yyyy-MM-dd");
	}

	/**
	 * 判断日期是否是本周
	 *
	 * @param time 日期
	 * @return 判断结果
	 */
	public static boolean isThisWeek(long time) {
		return isThisWeek(new Date(time));
	}

	/**
	 * 判断日期是否是本周
	 *
	 * @param time 日期
	 * @return 判断结果
	 */
	public static boolean isThisWeek(Date time) {
		Calendar calendar = Calendar.getInstance();
		int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.setTime(time);
		int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		return paramWeek == currentWeek;
	}

	/**
	 * 判断日期是否是本月
	 *
	 * @param time 日期
	 * @return 判断结果
	 */
	public static boolean isThisMonth(long time) {
		return isThisMonth(new Date(time));
	}

	/**
	 * 判断日期是否是本月
	 *
	 * @param time 日期
	 * @return 判断结果
	 */
	public static boolean isThisMonth(Date time) {
		return isThisTime(time, "yyyy-MM");
	}

	private static boolean isThisTime(Date time, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(time).equals(sdf.format(new Date()));
	}

}
