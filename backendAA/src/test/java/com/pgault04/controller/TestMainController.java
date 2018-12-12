/**
 * 
 */
package com.pgault04.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import com.pgault04.controller.MainController;


/**
 * @author Paul Gault - 40126005
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class TestMainController {

	private MainController mainC;
	
	private Model mockedModel;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mainC = new MainController();
		mockedModel = mock(Model.class);
	}

	/**
	 * Test method for {@link com.pgault04.controller.MainController#login(org.springframework.ui.Model)}.
	 */
	@Test
	public void testLogin() {
		
		//assertEquals("login", mainC.login(mockedModel));
	}

	/**
	 * Test method for {@link com.pgault04.controller.MainController#logoutSuccessfulPage(org.springframework.ui.Model)}.
	 */
	@Test
	public void testLogoutSuccessfulPage() {
	//	assertEquals("login", mainC.logoutSuccessfulPage(mockedModel));
	}

}
