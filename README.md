
# Report4s

Simple and yet elegant Selenium HTML report.
Report4s is a Selenium HTML reporter for the TestNG framework.

### Features

* It allows you to capture screenshots of :
  * Web elements
  * Web pages
* The stack trace of exceptions are displayed to facilitate you a better error analysis.
* The test report can be exported in Excel .xlsx format.
* It is compatible with both Selenium 2 and Selenium 3.

### Logged browser events

Report4s is able to log the following web browser events :
```
Select.deselectByIndex(int)
Select.deselectByValue(java.lang.String)
Select.deselectByVisibleText(java.lang.String)

Select.selectByIndex(int)
Select.selectByValue(java.lang.String)
Select.selectByVisibleText(java.lang.String)

WebDriver.get(java.lang.String)
WebDriver.navigate().back()
WebDriver.navigate().forward()
WebDriver.navigate().refresh()
WebDriver.navigate().to(java.lang.String)
WebDriver.navigate().to(java.net.URL)

WebElement.click()
WebElement.sendKeys(CharSequence[])
```

### ZIP file contents
```
report4.zip
|---javadoc                    The API documentation
|---report4s.properties        Configuration file (optional)
|---lib                        Required external libraries
    |---report4s.jar
    |---commons-io-2.5.jar
    |---poi-3.14.jar
    |---poi-ooxml-3.14.jar
    |---poi-ooxml-schemas-3.14.jar
    |---xmlbeans-2.6.0.jar
    |---guava-22.0.jar               Required for selenium 2
```

### Requirements
Java 7 or later
testng 6.9.4 or later

### Caution
DO NOT RENAME THE report4s JAR FILE.

### Installation
Add the jar files to the classpath.
Add report4s.properties file in your project root folder (optional).

### TestNG XML file configuration
Add the following lines before the closing </suite> tag in your testng xml file :
```
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="..." >
    ...
    ...
    <listeners>
        <listener class-name="net.sourceforge.report4s.ReportIndex" />
        <listener class-name="net.sourceforge.report4s.TestListener" />
        <listener class-name="net.sourceforge.report4s.SuiteListener" />
        <!-- The configuration listener is optional -->
        <listener class-name="net.sourceforge.report4s.ConfigurationListener" />
    </listeners>
</suite> 
```

### The packages to import
```
import net.sourceforge.report4s.*;
```
or
```
import net.sourceforge.report4s.EventHandler;
import net.sourceforge.report4s.Report4s;
import net.sourceforge.report4s.Level;
```

### The test configuration
We need to wrap the WebDriver into an Event Listener
```
public class MyTest {
    private static WebDriver driver;
    private static EventFiringWebDriver eventDriver;

    @BeforeSuite(alwaysRun=true)
    public void setUp() {
        . . . . 
        . . . . 
        eventDriver = new EventFiringWebDriver(driver);
        EventHandler handler = new EventHandler();
        eventDriver.register(handler);
        eventDriver.manage().window().maximize();
    }
}
```

### How to log
The log levels are :
`PASSED`, `FAILED`, `INFO`, `WARNING` and `DEBUG`

To log a web browser event :
`Report4s.logEvent(String description, boolean screenshot);`

To log a custom message :
`Report4s.logMessage(Level.*, String description);`

To log a custom message with a full page screenshot :
`Report4s.logMessage(Level.*, description, webdriver);`

To log custom message with a WebElement screenshot :
`Report4s.logMessage(Level.*, description, webdriver, webelement);`

For WebElement screenshots you have the possibility to apply an extra padding :
`Report4s.logMessage(Level.*, description, webdriver, webelement, padding);`

replace `*` by `INFO`, `WARNING` or `DEBUG`
No screenshot will be taken if the driver is an instance of HtmlUnitDriver.

### Sample Java code
The `Report4s.logEvent` method should be called right before the call of the event you want to log.
```
@Test(description = "My test description")
public void test1() {

    Report4s.logEvent("Open web site", true);
    eventDriver.get("http://www.example.com");

    WebElement elem;
    elem = eventDriver.findElement(By.name("xxxx"));
    Report4s.logEvent("send keys", true);
    elem.sendKeys("Hello World !!");

    elem = eventDriver.findElement(By.id("xxxx"));
    Report4s.logEvent("click on button", true);
    elem.click();

    Select sel = new Select(eventDriver.findElement(By.name("xxxx")));
    Report4s.logEvent("click on select", true);
    sel.selectByValue("xxxx");

    Report4s.logEvent("Navigate back", true);
    eventDriver.navigate().back();
}
```

You are encouraged to use the description attribute of the `@test` annotation to verbose your reports.

A good tutorial on how to use testng with Selenium can be found here: http://testng.org/doc/selenium.html
The example is for Selenium v1 but the same principles apply also to Selenium v2.

### report4s.properties file
```
#IF YOU WANT TO MODIFY A PROPERTY DEFAULT VALUE,
#UNCOMMENT THE APPROPRIATE LINE AND SET THE VALUE OF YOUR CHOICE.

#The report directory relative to the workspace (working directory).
#It is advisable to avoid using the same TestNG default report directory (test-output).
#report4s.report.dir=report

#The file name of the report homepage.
#report4s.report.homepage=index.html

#The title of the report home page.
#report4s.report.title=Test Execution Summary

#The excel report filename.
#report4s.report.excel=Test report.xlsx

#Whether to enable screenshots.
#Regardless its value, a screenshot is taken right after a test failure whenever possible.
#report4s.screenshots.enabled=true

#Padding in pixels to be applied to WebElement screenshots.
#Defines the area around the WebElement to be included in the screenshot.
#report4s.screenshots.padding=0

#The number of decimals of precision to be displayed in execution time labels.
#The value should be an integer between 0 and 3.
#report4s.time.precision=0

#The pie chart aggregation to be plotted.
#Possible values: suite, test or both.
#report4s.piechart.aggregation=test

#Whether to display tooltips with the execution result for each individual suite.
#report4s.suite.tooltips.enabled=true

#Whether to display a test result piechart or a status icon for each individual suite.
#Possible values: piechart or icon. (The icons are: PASSED, FAILED or SKIPPED)
#report4s.suite.status.content=piechart

#The width and height in pixels for the piecharts in the status column.
#This property will be ignored if report4s.suite.status.content is set to "icon".
#report4s.suite.status.size=35px

#Whether to skip the remaining steps of the current test execution
#if a step is logged with the FAILED level.
#report4s.execution.skipTestAfterStepFailure=false

#Whether to skip the remaining tests of the current suite if a test fails.
#report4s.execution.skipSuiteAfterTestFailure=false
```

### Browser Compatibility
Report4s has been successfully tested in Selenium 2 and Selenium 3 with the following configurations:

| OS | Browser |
|----|---------|
| Linux | Firefox |
| Linux	| Chrome |
| Windows 7	| Firefox |
| Windows 7	| Chrome |

Screenshots work better with Firefox.

### Limitations
No support for multi-threaded tests.
