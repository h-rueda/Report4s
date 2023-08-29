package com.github.report4s.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;
import com.github.report4s.Level;
import com.github.report4s.Report4s;

public class Web9 extends TemplateTest {
	
	@Test(description = "Manual logs - Failed")
	public void search() {
		this.driver.get(this.page);
		Report4s.logMessage(Level.INFO, "The web page", this.driver);
		WebElement elem;
		elem = this.driver.findElement(By.name("text"));
		elem.sendKeys("Hola");
		elem.sendKeys(" Mundo!");
		elem.clear();
		elem.sendKeys("Adios Mundo!");
		elem.sendKeys("");

		elem = this.driver.findElement(By.id("gender2"));
		elem.click();
		Report4s.logMessage(Level.WARNING, "Setting the gender");
		
		Select sel = new Select(this.driver.findElement(By.name("car")));
		sel.selectByValue("fiat");
		Report4s.logMessage(Level.DEBUG, "Selecting the car", this.driver);

		sel = new Select(this.driver.findElement(By.name("countries")));
		sel.selectByValue("poland");
		sel.selectByIndex(1);		
		sel.selectByVisibleText("France");
		sel.deselectByValue("poland");
		sel.deselectAll();

		Report4s.logMessage(Level.ERROR, "message in <b>bold</b>", this.driver);

		elem = this.driver.findElement(By.id("fruit2"));
		elem.click();

		elem = this.driver.findElement(By.id("textarea"));
		elem.sendKeys("Hola Mundo\nAdios mundo");
		Assert.assertEquals(1, 2);
	}

}
