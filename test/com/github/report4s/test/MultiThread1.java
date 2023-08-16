package com.github.report4s.test;

import com.github.report4s.Level;
import com.github.report4s.Report4s;
import org.testng.annotations.*;


public class MultiThread1 {

    @Test
    public void testMethod1() {
        Report4s.logMessage(Level.INFO, "hola");
        try { Thread.sleep(1000); } catch (InterruptedException e) { }
    }
 
    @Test
    public void testMethod2() {
        Report4s.logMessage(Level.INFO, "hola");
    }

    @Test
    public void testMethod3() {
        Report4s.logMessage(Level.INFO, "hola");
    }

}
