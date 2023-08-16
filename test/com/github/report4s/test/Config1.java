package com.github.report4s.test;

import com.github.report4s.Level;
import com.github.report4s.Report4s;
import org.testng.annotations.*;


public class Config1 {
	
	@BeforeSuite(alwaysRun=true)
	public void beforeSuite() {
        Report4s.logMessage(Level.INFO, "@BeforeSuite");		
	}
	
	@BeforeClass
    public void beforeClass() {
        Report4s.logMessage(Level.INFO, "@BeforeClass");
    }

	@BeforeTest
    public void beforeTest() {
        Report4s.logMessage(Level.INFO, "@BeforeTest");
    }
    
	@BeforeMethod
    public void beforeMethod() {
        Report4s.logMessage(Level.INFO, "@BeforeMethod");
    }

	@Test
	public void Test1() {
        Report4s.logMessage(Level.INFO, "@Test");
	}
	
	@Test
	public void Test2() {
        Report4s.logMessage(Level.INFO, "@Test");
	}

	@AfterSuite
    public void afterSuite() {
        Report4s.logMessage(Level.INFO, "@AfterSuite");
    }

	@AfterClass
    public void afterClass() {
        Report4s.logMessage(Level.INFO, "@AfterClass");
    }

	@AfterTest
    public void afterTest() {
        Report4s.logMessage(Level.INFO, "@AfterTest");
    }
    
	@AfterMethod
    public void afterMethod() {
        Report4s.logMessage(Level.INFO, "@AfterMethod");
    }
    
}
