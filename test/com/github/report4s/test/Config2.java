package com.github.report4s.test;

import com.github.report4s.Level;
import com.github.report4s.Report4s;
import org.testng.annotations.*;

public class Config2 {
	
	@BeforeSuite(alwaysRun=true)
	public void beforeSuite() { }
	
	@BeforeClass
    public void beforeClass() { }

	@BeforeTest
    public void beforeTest() { }
    
	@BeforeMethod
    public void beforeMethod() { }

	@Test
	public void Test() { 
		Report4s.logMessage(Level.INFO, "Test");
	}
	
	@AfterSuite
    public void afterSuite() { }

	@AfterClass
    public void afterClass() { }

	@AfterTest
    public void afterTest() { }
    
	@AfterMethod
    public void afterMethod() { }

}
