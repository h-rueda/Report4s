package com.github.report4s.test;

import com.github.report4s.Level;
import com.github.report4s.Report4s;
import org.testng.Assert;
import org.testng.annotations.*;


public class MultiThread2 {

    @Test
    public void testMethod1() {
        Report4s.logMessage(Level.INFO, "hola");
    }
 
    @Test
    public void testMethod2() {
        Report4s.logMessage(Level.INFO, "hola");
        Assert.fail();
    }

}
