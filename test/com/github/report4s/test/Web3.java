package com.github.report4s.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import com.github.report4s.Report4s;

public class Web3 extends TemplateTest {
	
	@BeforeSuite(alwaysRun=true)
	public void suiteSetUp() {
		//Report4s.skipSuiteAfterTestFailure = false;
	}
	
	@Test(description = "Write input element")
	public void search() {
		this.driver.get(this.page);
		WebElement elem;
		elem = this.driver.findElement(By.name("text"));
		elem.sendKeys("Hola");
		elem.sendKeys(" Mundo!");
		elem.clear();
		elem.sendKeys("Adios Mundo!");
		elem.sendKeys("");
	}

	@Test(description = "Single select")
	public void selectCar1() {
		Select sel = new Select(this.driver.findElement(By.name("car")));
        sel.selectByValue("fiat");
		sel.selectByValue("xxxx");
	}

	@Test(description = "Single select")
	public void selectCar2() {
		Select sel = new Select(this.driver.findElement(By.name("xxxx")));
		sel.selectByValue("xxxx");
	}

	@Test(description = "Single select")
	public void selectCar3() {
		Select sel = new Select(this.driver.findElement(By.name("car")));
		sel.selectByValue("fiat");
		Assert.assertEquals(1, 2);
	}

	@Test(description = "Click checkbox")
	public void selectFruit() {
		WebElement elem;
		elem = this.driver.findElement(By.id("fruit2"));
		elem.click();
	}

	@Test(description = "depends on failed method", dependsOnMethods = {"selectCar1"})
	public void setGender() {
		WebElement elem;
		elem = this.driver.findElement(By.id("gender2"));
		elem.click();
	}

	@AfterSuite(alwaysRun=true)
	public void suiteTearDoan() {
		//Report4s.skipSuiteAfterTestFailure = true;
	}

}
