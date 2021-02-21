package com.jostens.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.jostens.qa.util.TestUtil;
import com.jostens.qa.util.WebEventListener;
import com.jostens.qa.pages.CheckoutPage;
import com.jostens.qa.pages.SearchSchoolPage;
import com.jostens.qa.pages.LoginPage;
import com.jostens.qa.pages.PaymentPage;
import com.jostens.qa.pages.ProductDetailPage;
import com.jostens.qa.pages.ShoppingCartPage;
import com.jostens.qa.util.EventHandler;
import com.jostens.qa.util.ExcelUtil;
import com.jostens.qa.util.ExtentFactory;

public class TestBase {
	//Initialize BrowserStack Variable(s)
//	public static final String USERNAME = "johndang1";
	public static final String USERNAME = "cynastralis1";
//	public static final String AUTOMATE_KEY = "ZDrTyrS1hQrUixbHrJVA";
	public static final String AUTOMATE_KEY = "3Jbbqzr7wpzLJyLZSTi6";
//	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
	
	//Define Variable(s)
	public static Properties prop;
	public TestUtil genMethods;
	public ExcelUtil excelMethods;
	public String sheetName;
	
	//Variable(s) used to export script results
	public int iteration;
	public int column;
	
	//Variable(s) used to initialize the WebDriver
	public static DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
	private static WebDriver driver; //used to setup a connection to one or more browsers for testing
	private static EventHandler eHandler;  //used in conjunction with the EventFiringWebDriver
	private static WebEventListener webEventListener;  //used in conjunction with the EventFiringWebDriver
	public static EventFiringWebDriver eDriver; //used in conjunction with the WebDriver
	
	//Setup the report logger
	public ExtentReports report; //used to setup a report that will hold the testing info of the script(s)
	public ExtentTest reportLogger; //used to store testing details in the report
	
	//Define PageFactories
	public LoginPage loginPage;
	public SearchSchoolPage searchSchoolPage;
	public ProductDetailPage productDetailPage;
	public ShoppingCartPage shoppingCartPage;
	public CheckoutPage checkoutPage;
	public PaymentPage paymentPage;
	
	public TestBase() {
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\com\\jostens\\qa\\config\\config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void initializeDriver(String browserName, String browser_version, String os, String os_version, String browserStack) {
		String testingBrowser = browserName;
		
		//Set BrowserStack browser details to 'caps'
//		String methodName = name.getName();
		caps.setCapability("os", os);
		caps.setCapability("os_version", os_version);
		caps.setCapability("browser_version", browser_version);
//		caps.setCapability("name", methodName);
		
		//Initialize the relevant browser driver
		if (testingBrowser.equalsIgnoreCase("internetexplorer") || testingBrowser.equalsIgnoreCase("edge")) {
			//Setup caps properties for IE, if IE is going to be used as the Driver
			caps.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
			caps.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
			caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
			caps.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			caps.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			
			WebDriverManager.iedriver().setup();
			
			if (browserStack.equalsIgnoreCase("y")) {
				caps.setCapability("browser", "Edge");
			} else {
				driver = new InternetExplorerDriver(caps);
			}
			
		} else if (testingBrowser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			
			if (browserStack.equalsIgnoreCase("y")) {
				caps.setCapability("browser", "Firefox");
			} else {
				driver = new FirefoxDriver();
			}
			
		} else if (testingBrowser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			
			if (browserStack.equalsIgnoreCase("y")) {
				caps.setCapability("browser", "Chrome");
			} else {
				driver = new ChromeDriver();
			}
			
		} else {
			System.out.println("Cannot setup the driver due to invalid input");
			driver.quit();
		}
		
		//Open the browser
		if (browserStack.equalsIgnoreCase("y")) {
			try {
				System.out.println("hello");
				driver = new RemoteWebDriver(new URL(URL), caps);
				System.out.println("bye");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		//Setup the Event Driver
		eDriver = new EventFiringWebDriver(driver);
		eHandler = new EventHandler();
//		webEventListener = new WebEventListener();
		eDriver.register(eHandler);
//		eDriver.register(webEventListener);
		
		eDriver.manage().window().maximize();
		eDriver.manage().deleteAllCookies();
		eDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		eDriver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
	}
	
//	@BeforeSuite
	@BeforeTest
//	@Parameters({ "browser", "browser_version", "os", "os_version", "browserStack" })
	public void beforeSuite() {
		String browserName = "Chrome";
		String browser_version = "81";
		String os = "Windows";
		String os_version = "10";
		String browserStack = "Y";
		
		//Delete previous Extent Report
		ExtentFactory.deleteExtentReport();
		
		//Initialize Variable(s)
		System.out.println("Performing the script's setups (@BeforeSuite)");
		initializeDriver(browserName, browser_version, os, os_version, browserStack); //Sets up WebDriver with Listeners
		genMethods = new TestUtil();
	}
	
	@AfterSuite
	public void afterSuite() {
		eDriver.quit();
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		if(result.getStatus() == ITestResult.FAILURE) {
//			//Do not output to Excel File, if performing the @Test=aloginTest, since there is no excel file for it
//			if (!result.getName().equals("loginTest")) {
//				excelMethods.setDataTableCell("Failure - " + excelMethods.getCurrentDateTime(), iteration, column);
//			}
			
			//Do not output to Excel File, if performing the @Test=aloginTest, since there is no excel file for it
			excelMethods.setDataTableCell("Failure - " + excelMethods.getCurrentDateTime(), iteration, column);
			
			reportLogger.log(LogStatus.FAIL,  "The Test Case that failed is: " + result.getName()); //adds name to ExtentReport
			reportLogger.log(LogStatus.FAIL,  "The Test Case that failed is: " + result.getThrowable()); //adds error/exception to ExtentReport
			
			String screenshotPath = TestUtil.getScreenshot(eDriver, result.getName());
			reportLogger.log(LogStatus.FAIL,  reportLogger.addScreenCapture(screenshotPath)); //adds screenshot to ExtentReport
		} else if (result.getStatus() == ITestResult.SKIP) {
			System.out.println("The Test Case that was skipped is: " + result.getName());
			reportLogger.log(LogStatus.SKIP,  "The Test Case that was skipped is: " + result.getName());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			//Do not output to Excel File, if performing the @Test=aloginTest, since there is no excel file for it
			if (!result.getName().equals("aloginTest")) {
				excelMethods.setDataTableCell("Success - " + excelMethods.getCurrentDateTime(), iteration, column);
			}
			
			System.out.println("The Test Case that passed is: " + result.getName());
			reportLogger.log(LogStatus.PASS,  "The Test Case that passed is: " + result.getName());
		} else {
			reportLogger.log(LogStatus.UNKNOWN,  "Unknown error upon completion of a @Test - the @Test neither passed, failed, or was skipped");
		}
		
		report.endTest(reportLogger);
		report.flush();
//		eDriver.quit();
	}
	
}