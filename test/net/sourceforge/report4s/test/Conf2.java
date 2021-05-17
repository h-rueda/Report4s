package net.sourceforge.report4s.test;

import net.sourceforge.report4s.Level;
import net.sourceforge.report4s.Report4s;

import org.testng.Assert;
import org.testng.annotations.*;

public class Conf2 {

	@BeforeClass
	public void setUp() { }

	@Test()
	public void myTest1() { }

	@Test()
	public void myTest2() {	
		Report4s.logMessage(Level.INFO, "hola");
	}
	
	@AfterClass
	public void tearDown() { 
		Report4s.logMessage(Level.INFO, "hola");
		Report4s.logMessage(Level.INFO, "adios");
		Assert.fail();
	}
}
