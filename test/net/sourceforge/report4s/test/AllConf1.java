package net.sourceforge.report4s.test;

import net.sourceforge.report4s.Level;
import net.sourceforge.report4s.Report4s;
import org.testng.annotations.*;

public class AllConf1 {

	static int i = 0;
	
	@BeforeSuite(alwaysRun=true)
	public void beforeSuite() {
        Report4s.logMessage(Level.INFO, "hola suite");		
	}
	
	@BeforeClass
    public void beforeClass() {
        Report4s.logMessage(Level.INFO, "hola class");
    }

	@BeforeTest
    public void beforeTest() {
        Report4s.logMessage(Level.INFO, "hola test");
    }
    
	@BeforeMethod
    public void beforeMethod() {
        Report4s.logMessage(Level.INFO, "hola metodo");
    }

	@Test
	public void Test1() {
        Report4s.logMessage(Level.INFO, "hola metodo 1");
	}
	
	@Test
	public void Test2() {
        Report4s.logMessage(Level.INFO, "hola metodo 2");
	}

	@AfterSuite
    public void afterSuite() {
        Report4s.logMessage(Level.INFO, "adios suite");
    }

	@AfterClass
    public void afterClass() {
        Report4s.logMessage(Level.INFO, "adios class");
    }

	@AfterTest
    public void afterTest() {
        Report4s.logMessage(Level.INFO, "adios test");
    }
    
	@AfterMethod
    public void afterMethod() {
        Report4s.logMessage(Level.INFO, "adios metodo");
    }
    
}
