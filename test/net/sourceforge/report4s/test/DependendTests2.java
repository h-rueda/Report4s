package net.sourceforge.report4s.test;

import net.sourceforge.report4s.Level;
import net.sourceforge.report4s.Report4s;

import org.testng.Assert;
import org.testng.annotations.*;

public class DependendTests2 {
	
    @BeforeTest
    public void beforeTest() {
    	Report4s.skipSuiteAfterTestFailure = true;
    }
    
    @Test
    public void testMethod1() {  }
 
    @Test
    public void testMethod2() {
        Report4s.logMessage(Level.INFO, "hola");
    }

    @Test
    public void testMethod3() {
        Report4s.logMessage(Level.INFO, "hola");
        Assert.fail();
    }

    @Test
    public void testMethod4() {
        Assert.fail();
    }

    @Test
    public void testMethod5() {
    	Report4s.logMessage(Level.INFO, "hola");
    }
}
