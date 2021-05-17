package net.sourceforge.report4s.test;

import java.io.*;
import java.net.*;
import java.util.Properties;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
import net.sourceforge.report4s.*;


public class MyTest7 {

	private static WebDriver driver;
	private static EventFiringWebDriver eventDriver;

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
			driver = new RemoteWebDriver(new URL("http://localhost:" + port + "/wd/hub"), DesiredCapabilities.firefox());
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
		Report4s.screenshots = false;
	}
	
	@Test(description = "look up a word definition")
	public void search() {
		Report4s.logEvent("Open web site", true);
		eventDriver.get("file:///home/harmin/workspace/Report4s/test/home.html");
		//eventDriver.get("file:///C:/Users/harmin/workspace/Report4s/test/home.html");
		WebElement elem;
		elem = eventDriver.findElement(By.name("name"));
		Report4s.logEvent("send keys", true);
		elem.sendKeys("Hola");
		Report4s.logEvent("send keys", true);
		elem.sendKeys(" Mundo!");
		Report4s.logEvent("clear", true);
		elem.clear();
		Report4s.logEvent("send keys", true);
		elem.sendKeys("Adios Mundo!");

		elem = eventDriver.findElement(By.id("gender2"));
		Report4s.logEvent("click on gender2", true);
		elem.click();
		//Report4s.log(Level.PASSED, "after click", eventDriver);
		
		Select sel = new Select(eventDriver.findElement(By.name("car")));
		Report4s.logEvent("click on select fiat", true);
		sel.selectByValue("fiat");

		elem = eventDriver.findElement(By.id("vehicle2"));
		Report4s.logEvent("click on vehicle2", true);
		elem.click();

		sel = new Select(eventDriver.findElement(By.name("countries")));
		Report4s.logEvent("click on select poland", true);
		sel.selectByValue("poland");

		eventDriver.findElement(By.linkText("Googlevdfgfdg")).click();
		elem.click();
	}

	
	@AfterSuite
	public void tearDown() {
		eventDriver.close();
		eventDriver.quit();
		Report4s.screenshots = true;
	}

}
