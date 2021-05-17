package net.sourceforge.report4s.test;

import net.sourceforge.report4s.Level;
import net.sourceforge.report4s.Report4s;

import org.testng.Assert;
import org.testng.annotations.*;

public class Conf1 {

	@BeforeClass
	public void setUp() { Assert.fail(); }

	public void myTest1() {	
		Report4s.logMessage(Level.INFO, "hola");
	}
	@Test()
	public void myTest2() {	}
	
	@AfterClass
	public void tearDown() { }
}
