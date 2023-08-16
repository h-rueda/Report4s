package com.github.report4s.test;

import org.testng.Assert;
import org.testng.annotations.*;


public class Percentage {

	static int i = 0;
	
	@Test(successPercentage=50, invocationCount=5, description = "Failure within success percentage")
	public void percentage() {
		i++;
		Assert.assertEquals(i%2, 0);
	}
	
}
