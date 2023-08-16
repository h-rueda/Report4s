package com.github.report4s.test;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import com.github.report4s.Report4s;

public class Web2 extends TemplateTest {
	
	@BeforeSuite(alwaysRun=true)
	public void quiteSetUp() {
		Report4s.skipSuiteAfterTestFailure = true;
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
	public void selectCar() {
		Select sel = new Select(this.driver.findElement(By.name("car")));
		sel.selectByValue("fiat");
		Assert.fail();
	}

	@Test(description = "Click checkbox")
	public void selectFruit() {
		WebElement elem;
		elem = this.driver.findElement(By.id("fruit2"));
		elem.click();
	}

	@Test(description = "Click radio-button")
	public void setGender() {
		WebElement elem;
		elem = this.driver.findElement(By.id("gender2"));
		elem.click();
	}

}
