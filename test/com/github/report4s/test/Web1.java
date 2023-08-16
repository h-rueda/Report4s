package com.github.report4s.test;


import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;

public class Web1 extends TemplateTest {
	
	@Test(description = "Write input element")
	public void test_1_search() {
		this.driver.get(this.page);
		WebElement elem;
		elem = this.driver.findElement(By.name("text"));
		elem.sendKeys("Hola");
		elem.sendKeys(" Mundo!");
		elem.clear();
		elem.sendKeys("Adios Mundo!");
		elem.sendKeys("");
	}

	@Test(description = "Click radio-button")
	public void test_2_setGender() {
		WebElement elem;
		elem = this.driver.findElement(By.id("gender2"));
		elem.click();
	}

	@Test(description = "Single select")
	public void test_3_selectCar() {
		Select sel = new Select(driver.findElement(By.name("car")));
		sel.selectByValue("fiat");
	}

	@Test(description = "Multiple select")
	public void test_4_selectCountry() {
		Select sel;
		sel = new Select(this.driver.findElement(By.name("countries")));
		sel.selectByValue("poland");
		sel.selectByValue("colombia");
		sel.selectByValue("france");
		sel.deselectByValue("poland");
		sel.deselectAll();
	}

	@Test(description = "Click checkbox")
	public void test_5_selectFruit() {
		WebElement elem;
		elem = this.driver.findElement(By.id("fruit2"));
		elem.click();
	}

	@Test(description = "Write TextArea")
	public void test_6_writeTextArea() {
		WebElement elem;
		elem = this.driver.findElement(By.id("textarea"));
		elem.sendKeys("Hola Mundo\nAdios mundo");
	}
	
	@Test(description = "Click link")
	public void test_7_link() {
		WebElement elem;
		elem = this.driver.findElement(By.linkText("example.com"));
		elem.click();

		this.driver.navigate().back();
	}

	@Test(description = "Click button")
	public void test_8_button() {
		WebElement elem;
		elem = this.driver.findElement(By.name("button"));
		assert StringUtils.equals(elem.getAttribute("value"), "wikipedia");
		elem.click();
	}
	
	@Test(description = "Navigation")
	public void test_9_navigation() {	
		this.driver.navigate().back();
		this.driver.navigate().forward();
		this.driver.navigate().to(this.page);
		this.driver.navigate().refresh();
	}

}
