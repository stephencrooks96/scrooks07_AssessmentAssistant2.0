package com.pgault04.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pgault04.entities.TimeModifier;

/**
 * @author Paul Gault 40126005
 * @since November 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTimeModifier {

	private TimeModifier timeModifierObj;

	private Long userID;

	private Double timeModifier;

	@Before
	public void setUp() throws Exception {
		this.timeModifierObj = new TimeModifier();
		this.userID = 1L;
		this.timeModifier = 2.0;
	}

	@Test
	public void testTimeModifierDefaultConstructor() {
		assertNotNull(timeModifierObj);
	}

	@Test
	public void testTimeModifierConstructorWithArgs() {
		timeModifierObj = null;
		timeModifierObj = new TimeModifier(userID, timeModifier);

		assertNotNull(timeModifierObj);
		assertEquals(userID, timeModifierObj.getUserID());
		assertEquals(timeModifier, timeModifierObj.getTimeModifier());
	}

	@Test
	public void testGetSetUserID() {
		timeModifierObj.setUserID(userID);
		assertEquals(userID, timeModifierObj.getUserID());
	}

	@Test
	public void testGetSetTimeModifier() {
		timeModifierObj.setTimeModifier(timeModifier);
		assertEquals(timeModifier, timeModifierObj.getTimeModifier());
	}

	@Test
	public void testToString() {
		timeModifierObj = new TimeModifier(userID, timeModifier);
		assertEquals("TimeModifier{userID=1, timeModifier=2.0}", timeModifierObj.toString());
	}

}
