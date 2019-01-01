package com.pgault04.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Facilitates conversion from string to date of dates held in objects
 * as strings
 *
 * @author Paul Gault - 40126005
 * @since November 2018
 */
public class StringToDateUtil {

    /**
     * The default constructor
     */
    public StringToDateUtil() {}

    /**
     * Converts date from the String object it is stored in to a java.util date to
     * allow for comparison
     *
     * @param dateTime the data and time
     * @return the reformatted date and time
     */
    public static Date stringToDate(String dateTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(dateTime);
    }

    public static String dateCorrectFormat(Date date) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * Converts date input on front end to date that is preferred by database
     *
     * @param dateTime the date and time
     * @return the reformatted date and time
     * @throws ParseException arises from parsing date from string
     */
    public static String convertInputDateToCorrectFormat(String dateTime) throws ParseException {
        SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat dateToString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = stringToDate.parse(dateTime);
        return dateToString.format(date);
    }

    /**
     * Converts date input on front end to date that is preferred by database
     *
     * @param dateTime the date and time
     * @return the reformatted date and time
     * @throws ParseException arises from parsing date from string
     */
    public static String convertDateToFrontEndFormat(String dateTime) throws ParseException {
        SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateToString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = stringToDate.parse(dateTime);
        return dateToString.format(date);
    }

    /**
     * Converts the date to a more intuitive and easily readable format before outputting to user
     *
     * @param dateTime the date and time
     * @return the reformatted date and time
     * @throws ParseException arises from parsing string for date
     */
    public static String convertReadableFormat(String dateTime) throws ParseException {
        SimpleDateFormat dateToString = new SimpleDateFormat("HH:mm 'on' dd-MM-yyyy");
        SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = stringToDate.parse(dateTime);
        return dateToString.format(date);
    }
}
