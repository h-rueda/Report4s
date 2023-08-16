package com.github.report4s.test;

import org.testng.annotations.*;

import com.github.report4s.Level;
import com.github.report4s.Report4s;


public class Parameter {
	
	@Test(description = "Test with parameter")
	@Parameters({"param"})
	public void parameter(String param) { 
		Report4s.logMessage(Level.INFO, "The parameter is " + param);
	}
	
}
