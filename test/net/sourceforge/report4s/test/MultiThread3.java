package net.sourceforge.report4s.test;

import net.sourceforge.report4s.Level;
import net.sourceforge.report4s.Report4s;
import org.testng.annotations.*;
import org.testng.Assert;

public class MultiThread3 {
	
    @BeforeMethod
    public void beforeMethod() {
        Report4s.logMessage(Level.INFO, "hola");
    }
 
    @Test
    public void testMethod1() {
        Report4s.logMessage(Level.INFO, "hola");
    }
 
    @Test
    public void testMethod2() {
    	Assert.fail();
        Report4s.logMessage(Level.INFO, "hola");
    }
 
    @Test
    public void testMethod3() {
        Report4s.logMessage(Level.INFO, "hola");
    }

    @Test(dependsOnMethods = {"testMethod2"})
    public void testMethod4() {
        Report4s.logMessage(Level.INFO, "hola");
    }

    @AfterMethod
    public void afterMethod() {
        Report4s.logMessage(Level.INFO, "hola");
    }

}
