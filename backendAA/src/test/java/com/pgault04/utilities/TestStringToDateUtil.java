/**
 * 
 */
package com.pgault04.utilities;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Paul Gault - 40126005
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestStringToDateUtil {

	/**
	 * Test method for
	 * {@link com.pgault04.utilities.StringToDateUtil#stringToDateUtil(java.lang.String)}.
	 */
	@Test
	public void testStringToDateCons() {

		assertNotNull(new StringToDateUtil());
	}

	/**
	 * Test method for
	 * {@link com.pgault04.utilities.StringToDateUtil#stringToDate(java.lang.String)}.
	 */
	@Test
	public void testStringToDate() throws ParseException {

		assertThat(StringToDateUtil.stringToDate("0000-00-00 00:00:00"), instanceOf(Date.class));
	}

	@Test(expected = ParseException.class)
	public void testStringToDateInvalid() throws ParseException {
		StringToDateUtil.stringToDate("x");
	}

}
