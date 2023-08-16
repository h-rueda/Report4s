package com.github.report4s.test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class Demo extends TemplateTest {

	@Test(description = "Get homepage")
	public void getHomePage() {
		this.driver.get("http://www.oxfordlearnersdictionaries.com/");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("onetrust-accept-btn-handler")));
		driver.findElement(By.id("onetrust-accept-btn-handler")).click();
	}

	@Test(description = "Look up a word definition")   //, dependsOnMethods = {"getHomePage"})
	@Parameters({"keyword"})
	public void search(String keyword) {
		this.driver.findElement(By.id("q")).sendKeys(keyword);
		try { Thread.sleep(3000); } catch (InterruptedException e) { }
		this.driver.findElement(By.linkText(keyword)).click();
	}

	@Test(description = "Verify a verb definition", dependsOnMethods = {"search"})
	@Parameters({"keyword"})
	public void verifyVerbDefinitionPage(String keyword) {
		String verification = "Definition of " + keyword + " verb from the Oxford Advanced Learner's Dictionary";
		WebElement element = this.driver.findElement(By.cssSelector("p.definition-title"));
		Assert.assertEquals(element.getText(), verification);
	}
	
}
