package net.sourceforge.report4s.test;

import net.sourceforge.report4s.Level;
import net.sourceforge.report4s.Report4s;
import org.testng.annotations.*;

public class MultiThread2 {
	
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
        Report4s.logMessage(Level.INFO, "hola");
    }
 
    @AfterMethod
    public void afterMethod() {
        Report4s.logMessage(Level.INFO, "hola");
   }

}
