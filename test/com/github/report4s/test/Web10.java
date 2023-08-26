package com.github.report4s.test;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;

public class Web10 extends TemplateTest {
	
	@Test(description = "Write input element")
	public void test_1_search() {
		this.driver.get("xxxxx");
	}

	@Test(description = "Click radio-button")
	public void test_2_setGender() {
		WebElement elem;
		elem = this.driver.findElement(By.id("xxxx"));
		elem.click();
	}

	@Test(description = "Single select")
	public void test_3_selectCar() {
		Select sel = new Select(driver.findElement(By.name("xxx")));
		sel.selectByValue("fiat");
	}

	@Test(description = "Single select")
	public void test_4_selectCar() {
        Select sel = new Select(driver.findElement(By.name("car")));
        sel.selectByValue("xxxx");
	}
	
}
