package asakichy.architecture.domain_logic;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;

/**
 * 日付ユーティリティ.
 */
public class DateUtils {
	/**
	 * 加算した日付を返します.
	 * 
	 * @param date 被加算日付
	 * @param days 加算日数
	 * @return 加算後日付
	 */
	static public Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 指定した日付を時間以下をクリアして返します.
	 * 
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 日付（時間以下は0クリア）
	 */
	static public Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance(Locale.JAPAN);
		cal.clear();
		cal.set(year, month, day);
		return new Date(cal.getTimeInMillis());
	}

}
