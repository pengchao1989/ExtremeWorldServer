package com.jixianxueyuan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String DateToString(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
}
