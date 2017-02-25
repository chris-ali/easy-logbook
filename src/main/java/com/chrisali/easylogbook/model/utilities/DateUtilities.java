package com.chrisali.easylogbook.model.utilities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 
 * @author Christopher Ali
 * 
 * Contains static methods to convert between LocalDate and Date objects 
 *
 */
public class DateUtilities {

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @return Date object from year, month and day using LocalDate converted to Date
	 */
	public static Date dateFromYearMonthDay(int year, int month, int day) {
		return Date.from(LocalDate.of(year, month, day).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * Converts Date object to LocalDate 
	 * @param localDate
	 * @return converted LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
