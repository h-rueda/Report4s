package net.sourceforge.report4s.test;

import net.sourceforge.report4s.Level;
import net.sourceforge.report4s.Report4s;

import org.testng.Assert;
import org.testng.annotations.*;

public class Percentage {

	static int i = 0;
	
	@Test(successPercentage=10, invocationCount=5)
	public void myTest4a() {
		i++;
        Report4s.logMessage(Level.INFO, "assertion");
		Assert.assertEquals(i%2, 1);
	}
	
}
