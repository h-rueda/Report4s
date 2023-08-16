package com.github.report4s.test;


import org.openqa.selenium.*;
import org.testng.annotations.*;

public class Web6 extends TemplateTest {
	
	@BeforeSuite(alwaysRun=true)
	public void setUp() {
		assert false;
	}

	@Test(description = "Write input element")
	public void search() {
		driver.get(this.page);
		WebElement elem;
		elem = driver.findElement(By.name("text"));
		elem.sendKeys("Hola");
		elem.sendKeys(" Mundo!");
		elem.clear();
		elem.sendKeys("Adios Mundo!");
		elem.sendKeys("");
	}

	@Test(description = "Click checkbox")
	public void setGender() {
		WebElement elem;
		elem = driver.findElement(By.id("gender2"));
		elem.click();
	}

}
