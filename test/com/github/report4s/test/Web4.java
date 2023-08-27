package com.github.report4s.test;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.*;

import com.github.report4s.Report4s;

public class Web4 extends TemplateTest {
    
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
		throw new SkipException("Skipping setGender");
	}

	@Test(description = "Single select")
	public void selectCar() {
		Select sel = new Select(driver.findElement(By.name("car")));
		sel.selectByValue("fiat");
		throw new SkipException("Skipping selectCar");
	}

	@Test(description = "Multiple select")
	public void selectCountry() {
		Select sel;
		sel = new Select(driver.findElement(By.name("countries")));
		sel.selectByValue("poland");
		sel.selectByValue("colombia");
		sel.selectByValue("france");
		sel.deselectByValue("poland");
		sel.deselectAll();
	}

	@Test(description = "Click checkbox")
	public void selectFruit() {
		WebElement elem;
		elem = driver.findElement(By.id("fruit2"));
		elem.click();
	}

}
