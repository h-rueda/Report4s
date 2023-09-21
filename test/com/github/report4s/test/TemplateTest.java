package com.github.report4s.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.annotations.*;
import com.github.report4s.DriverListener;


public class TemplateTest {

	protected WebDriver driver;
	protected String page = "file://" + FileSystems.getDefault()
	            .getPath("test", "page.html").toAbsolutePath().toString();
	private String logs_dir = FileSystems.getDefault().getPath("logs").toAbsolutePath().toString();

	
	@BeforeSuite(alwaysRun=true)
	public void setUp() throws MalformedURLException {
		WebDriver raw_driver = null;
		String browser = null;
		FileInputStream input = null;
  		System.setProperty("webdriver.gecko.driver", "/home/harmin/eclipse/webdrivers/geckodriver");
		System.setProperty("webdriver.chrome.driver", "/home/harmin/eclipse/webdrivers/chromedriver");
  		System.setProperty("webdriver.edge.driver", "/home/harmin/eclipse/webdrivers/msedgedriver");
  		System.setProperty("webdriver.firefox.logfile", this.logs_dir + "/geckodriver.log");
        System.setProperty("webdriver.chrome.logfile", this.logs_dir + "/chromedriver.log");
        System.setProperty("webdriver.edge.logfile", this.logs_dir + "/msedgedriver.log");
		try {
			input = new FileInputStream("browser.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			browser = browser == null ? "firefox" : browser;
		} catch (Exception e) {
			System.err.println("Failed to open and load browser.properties file");
		} finally {
			if (input != null) {
				try { input.close(); }
				catch (IOException e) { System.err.println("Failed to close browser.properties file"); }
			}
		}
		if(browser.equals("firefox")) {
		    FirefoxOptions options = new FirefoxOptions();
		    options.addArguments("--headless");
            GeckoDriverService service = GeckoDriverService.createDefaultService();
			raw_driver = new FirefoxDriver(service, options);
		}
		if(browser.equals("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--headless");
            EdgeDriverService service = EdgeDriverService.createDefaultService();
			raw_driver = new EdgeDriver(service, options);
		}
		if(browser.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            ChromeDriverService service = ChromeDriverService.createDefaultService();
			raw_driver = new ChromeDriver(service, options);
		}
		DriverListener listener = new DriverListener();
		this.driver = new EventFiringDecorator<WebDriver>(listener).decorate(raw_driver);
		this.driver.manage().window().maximize();
	}


	@AfterSuite
	public void tearDown() {
		try { this.driver.quit(); }
		catch (Exception e) { }
	}
}
