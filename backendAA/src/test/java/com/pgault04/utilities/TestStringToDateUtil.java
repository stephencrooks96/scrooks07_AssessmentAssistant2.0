package com.pgault04.utilities;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Paul Gault - 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestStringToDateUtil {

	private StringToDateUtil stringToDateUtil;
	private String dateInputFormat, dateUnreadable;

	@Before
	public void setUp() throws Exception {
		stringToDateUtil = new StringToDateUtil();
		dateInputFormat = "2018-12-10T12:00";
		dateUnreadable = "2018-12-10 12:00:00";
	}
	@Test
	public void testStringToDateCons() {
		assertNotNull(stringToDateUtil);
	}

	@Test
	public void testStringToDate() throws ParseException {
		assertThat(StringToDateUtil.stringToDate("0000-00-00 00:00:00"), instanceOf(Date.class));
	}

	@Test
	public void testDateCorrectFormat() {
		Date date = new Date(1569285049284L);
		assertEquals("2019-09-24 01:30:49", StringToDateUtil.dateCorrectFormat(date));
	}

	@Test(expected = ParseException.class)
	public void testStringToDateInvalid() throws ParseException {
		StringToDateUtil.stringToDate("x");
	}

	@Test
	public void testDateFrontendFormat() throws ParseException {
		assertEquals("2018-12-10T12:00:00", StringToDateUtil.convertDateToFrontEndFormat("2018-12-10 12:00"));
	}

	@Test
	public void testConvertInputDateToCorrectFormat() throws ParseException {
		assertEquals("2018-12-10 12:00:00", StringToDateUtil.convertInputDateToCorrectFormat(dateInputFormat));
	}

	@Test(expected = ParseException.class)
	public void testConvertInputDateToCorrectFormatInvalid() throws ParseException {
		StringToDateUtil.convertInputDateToCorrectFormat("x");
	}

	@Test
	public void testConvertReadableFormat() throws ParseException {
		assertEquals("12:00 on 10-12-2018", StringToDateUtil.convertReadableFormat(dateUnreadable));
	}

	@Test(expected = ParseException.class)
	public void testConvertReadableFormatInvalid() throws ParseException {
		StringToDateUtil.convertReadableFormat("x");
	}

}
