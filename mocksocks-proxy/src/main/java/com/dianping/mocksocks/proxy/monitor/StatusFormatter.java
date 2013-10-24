package com.dianping.mocksocks.proxy.monitor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author yihua.huang@dianping.com
 */
public class StatusFormatter {

    public static final int MILLS_IN_SECOND = 1000;
    public static final int MILLS_IN_MINUTE = MILLS_IN_SECOND * 60;
    public static final int MILLS_IN_HOUR = MILLS_IN_MINUTE * 60;

    public static String formatBytes(int bytes) {
		NumberFormat numberFormat = DecimalFormat.getInstance(Locale.CHINA);
		numberFormat.setMaximumFractionDigits(2);
		if (bytes < (1 << 10)) {
			return String.valueOf(bytes);
		} else if (bytes < (1 << 20)) {
			return numberFormat.format(bytes * 1.0 / (1 << 10)) + "K";
		} else if (bytes < (1 << 30)) {
			return numberFormat.format(bytes * 1.0 / (1 << 20)) + "M";
		} else {
			return numberFormat.format(bytes * 1.0 / (1 << 30)) + "G";
		}
	}

	public static String formatTimeStamp(long time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss/SSS");
		return dateFormat.format(new Date(time));
	}

	public static String formatTimePeriod(long time) {
		NumberFormat numberFormat = DecimalFormat.getInstance(Locale.CHINA);
		numberFormat.setMaximumFractionDigits(2);
		if (time < MILLS_IN_SECOND) {
			return String.valueOf(time) + "ms";
		} else if (time < MILLS_IN_MINUTE) {
			return numberFormat.format(time * 1.0 / (MILLS_IN_SECOND)) + "s";
		} else if (time < (MILLS_IN_HOUR)) {
			return numberFormat.format(time * 1.0 / (MILLS_IN_MINUTE)) + "m";
		} else {
			return numberFormat.format(time * 1.0 / (MILLS_IN_HOUR)) + "h";
		}
	}

	public static void main(String[] args) {
		System.out.println(formatBytes(111111));
		System.out.println(formatBytes(1111111));
		System.out.println(formatBytes(1111111111));
		System.out.println(formatTimeStamp(System.currentTimeMillis()));
        System.out.println(formatTimePeriod(111));
        System.out.println(formatTimePeriod(11111));
        System.out.println(formatTimePeriod(11111111));
	}
}
