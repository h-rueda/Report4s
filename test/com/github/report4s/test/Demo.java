package com.github.report4s.test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;

import com.github.report4s.Report4s;

public class Demo extends TemplateTest {

    
    @Test(description = "Get homepage")
    public void loginPage() {
        this.driver.get("https://www.selenium.dev/selenium/web/web-form.html");
        driver.findElement(By.id("my-text-id")).sendKeys("login");
        driver.findElement(By.name("my-password")).sendKeys("password");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }
    
	@Test(description = "Get homepage")
	public void getHomePage() {
		this.driver.get("http://www.oxfordlearnersdictionaries.com/");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("onetrust-accept-btn-handler")));
		driver.findElement(By.id("onetrust-accept-btn-handler")).click();
	}

	@Test(description = "Get homepage")
	public void getHomePage2() {
		this.driver.get("http://www.oxfordlearnersdictionaries.com/");
		Report4s.screenshots = null;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("onetrust-accept-btn-handler")));
		this.driver.findElement(By.id("onetrust-accept-btn-handler")).click();
		this.driver.navigate().refresh();
	}

	@Test(description = "Look up a word definition")
	@Parameters({"keyword"})
	public void search(String keyword) {
		this.driver.findElement(By.id("q")).sendKeys(keyword);
		try { Thread.sleep(3000); } catch (InterruptedException e) { }
        Report4s.screenshots = "all";
        this.driver.findElement(By.linkText(keyword)).click();
	}

	@Test(description = "Look up a word definition")
	@Parameters({"keyword"})
	public void search2(String keyword) {
		this.driver.findElement(By.id("q")).sendKeys(keyword);
		try { Thread.sleep(3000); } catch (InterruptedException e) { }
		this.driver.findElement(By.linkText(keyword)).click();
		throw new SkipException("skip");
	}

	@Test(description = "Look up a word definition")
	@Parameters({"keyword"})
	public void search3(String keyword) {
		this.driver.findElement(By.id("q")).sendKeys(keyword);
		try { Thread.sleep(3000); } catch (InterruptedException e) { }
		this.driver.findElement(By.linkText(keyword)).click();
		Assert.fail();
	}

	@Test(description = "Verify a verb definition")
	@Parameters({"keyword"})
	public void verifyVerbDefinitionPage(String keyword) {
		String verification = "Definition of " + keyword + " verb from the Oxford Advanced Learner's Dictionary";
		WebElement element = this.driver.findElement(By.cssSelector("p.definition-title"));
		Assert.assertEquals(element.getText(), verification);
	}
	
	@Test(description = "Verify a verb definition", dependsOnMethods = {"search2"})
	@Parameters({"keyword"})
	public void verifyVerbDefinitionPage2(String keyword) {
		String verification = "Definition of " + keyword + " verb from the Oxford Advanced Learner's Dictionary";
		WebElement element = this.driver.findElement(By.cssSelector("p.definition-title"));
		Assert.assertEquals(element.getText(), verification);
	}

	@Test(description = "Verify a verb definition", dependsOnMethods = {"search3"})
	@Parameters({"keyword"})
	public void verifyVerbDefinitionPage3(String keyword) {
		String verification = "Definition of " + keyword + " verb from the Oxford Advanced Learner's Dictionary";
		WebElement element = this.driver.findElement(By.cssSelector("p.definition-title"));
		Assert.assertEquals(element.getText(), verification);
	}
}
