/**
 * 
 */
package com.pgault04.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Paul Gault - 40126005
 * 
 *         Facilitates conversion from string to date of dates held in objects
 *         as strings
 *
 */
public class StringToDateUtil {

	public StringToDateUtil() {};
	
	/**
	 * Converts date from the String object it is stored in to a java.util date to
	 * allow for comparison
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date stringToDate(String dateTime) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return formatter.parse(dateTime);
	}
	
	public static String convertInputDateToCorrectFormat(String dateTime) throws ParseException {
		
		SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		SimpleDateFormat dateToString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		Date date = stringToDate.parse(dateTime);
		
		return dateToString.format(date);
	
	}
	
public static String convertReadableFormat(String dateTime) throws ParseException {
		
		SimpleDateFormat dateToString = new SimpleDateFormat("HH:mm 'on' dd-MM-yyyy");
		SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		Date date = stringToDate.parse(dateTime);
		
		return dateToString.format(date);
	
	}
}
