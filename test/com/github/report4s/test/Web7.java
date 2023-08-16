package com.github.report4s.test;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
import com.github.report4s.Level;
import com.github.report4s.Report4s;


public class Web7 extends TemplateTest {
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

	@AfterSuite(alwaysRun=true)
	public void tearDown() {
		super.tearDown();
		assert false;
	}

}
