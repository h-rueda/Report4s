package net.sourceforge.report4s.test;

import java.io.*;
import java.net.*;
import java.util.Properties;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import net.sourceforge.report4s.*;

public class Demo {

	private static EventFiringWebDriver eventDriver;
	private static WebDriver driver;
	
	@BeforeSuite(alwaysRun=true)
	public void setUp() throws MalformedURLException {
		String browser = null, port = null;
		FileInputStream input = null;
  		//System.setProperty("webdriver.gecko.driver", "/home/harmin/workspace/geckodriver");
		//System.setProperty("webdriver.chrome.driver", "/home/harmin/workspace/chromedriver");
  		//System.setProperty("webdriver.gecko.driver", "C:\\\\Users\\harmin\\workspace\\geckodriver.exe");
  		//System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\harmin\\workspace\\chromedriver.exe");
		try {
			input = new FileInputStream("browser.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			browser = browser == null ? "firefox" : browser;
			port = prop.getProperty("port");
		} catch (Exception e) {
			System.err.println("Failed to open and load browser.properties file");
		} finally {
			if (input != null) {
				try { input.close(); }
				catch (IOException e) { System.err.println("Failed to close browser.properties file"); }
			}
		}
		if(browser.equals("firefox")) {
			port = port == null ? "4444" : port;
			DesiredCapabilities capabilities=DesiredCapabilities.firefox();
			driver = new RemoteWebDriver(new URL("http://localhost:" + port + "/wd/hub"), capabilities);
			//driver = new FirefoxDriver();
		}
		if(browser.equals("ie")) {
			port = port == null ? "5555" : port;
			driver = new RemoteWebDriver(new URL("http://localhost:" + port), DesiredCapabilities.internetExplorer());
		}
		if(browser.equals("chrome")) {
			port = port == null ? "9515" : port;
			driver = new RemoteWebDriver(new URL("http://localhost:" + port), DesiredCapabilities.chrome());
			//driver = new ChromeDriver();
		}
		eventDriver = new EventFiringWebDriver(driver);
		EventHandler handler = new EventHandler();
		eventDriver.register(handler);
		eventDriver.manage().window().maximize();
		Report4s.screenshots = true;
	}
	
	@Test(description = "look up a word definition")
	@Parameters({"keyword"})
	public void search(String keyword) {
		Report4s.logEvent("Open web site http://www.oxfordlearnersdictionaries.com/", true);
		eventDriver.get("http://www.oxfordlearnersdictionaries.com/");
		Report4s.logEvent("Type keyword", true);
		eventDriver.findElement(By.id("q")).sendKeys(keyword);
		try { Thread.sleep(3000); } catch (InterruptedException e) { }
		Report4s.logEvent("Click suggestion link <b>" + keyword + "</b>", true);
		eventDriver.findElement(By.linkText(keyword)).click();
	}

	@Test(description = "verify a verb definition", dependsOnMethods = {"search"})
	@Parameters({"keyword"})
	public void verifyVerbDefinitionPage(String keyword) {
		String verification = "Definition of " + keyword + " verb from the Oxford Advanced Learner's Dictionary";
		WebElement element = eventDriver.findElement(By.cssSelector("p.definition-title"));
		Assert.assertEquals(element.getText(), verification);
		Report4s.logMessage(Level.INFO, "Assert text of WebElement [css=p.definition-title] == " + verification, driver);
		Report4s.logMessage(Level.WARNING, "I can log a warning message");
		Report4s.logMessage(Level.DEBUG, "I can log a debug message");
	}
	
	@AfterSuite
	public void tearDown() {
		eventDriver.close();
		eventDriver.quit();
	}

}
