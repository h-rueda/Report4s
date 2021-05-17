package net.sourceforge.report4s.test;

import org.testng.annotations.*;

public class AllConf2 {

	static int i = 0;
	
	@BeforeSuite(alwaysRun=true)
	public void beforeSuite() { }
	
	@BeforeClass
    public void beforeClass() { }

	@BeforeTest
    public void beforeTest() { }
    
	@BeforeMethod
    public void beforeMethod() { }

	@Test
	public void Test() {	}
	
	@AfterSuite
    public void afterSuite() { }

	@AfterClass
    public void afterClass() { }

	@AfterTest
    public void afterTest() { }
    
	@AfterMethod
    public void afterMethod() { }

}
