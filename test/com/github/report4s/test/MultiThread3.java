package com.github.report4s.test;

import com.github.report4s.Level;
import com.github.report4s.Report4s;
import org.testng.SkipException;
import org.testng.annotations.*;

public class MultiThread3 {

    @Test
    public void testMethod1() {
        Report4s.logMessage(Level.INFO, "hola");
    }
 
    @Test
    public void testMethod2() {
		throw new SkipException("Skipping");
    }

}
