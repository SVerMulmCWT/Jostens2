package com.jostens.qa.testcases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jostens.qa.base.TestBase;
import com.jostens.qa.pages.LoginPage;
import com.jostens.qa.util.ExcelUtil;
import com.jostens.qa.util.ExtentFactory;
import com.relevantcodes.extentreports.LogStatus;

public class ALoginPageTest extends TestBase {
	
	//Define Variable(s)
	SoftAssert checkpoint;
	
	//Constructor
	public ALoginPageTest() {
		super();
	}
	
	//Setup variable(s) and other info for the class
	@BeforeClass
	@Parameters({"dataTable"})
	public void beforeClass(String excelPath) {
		//Initialize Variable(s)
		excelMethods = new ExcelUtil();
		excelMethods.setDataTablePath(excelPath);
		excelMethods.setSheetName("Login");
		column = 7;
		
		//Setup the Report
		report = ExtentFactory.getInstance();
		reportLogger = report.startTest("LoginPageTest Script");
		
		//Initialize PageFactories
		System.out.println("Initializing the script's PageFactories");
		reportLogger.log(LogStatus.INFO, "Initializing the script's PageFactories");
		
		loginPage = new LoginPage(eDriver, reportLogger);
	}
	
	//Test the login functionality
	@Test()
	public void loginTest() {
		System.out.println("@Test - loginTest()");
		
		//Initialize Variable(s)
		checkpoint = new SoftAssert(); //SoftAssert Setup (for identifying checkpoints)
		
		//Go to the desired Website (Jostens.com)
		loginPage.accessWebsite(prop.getProperty("url"));
		
		//Login to the website (Jostens.com)
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		
		//Check if the login was successful
		checkpoint = loginPage.verifySuccessfulLogin(checkpoint, prop.getProperty("name"));
		
//		//Logout, if necessary
//		if (logout.equalsIgnoreCase("y") || logout.equalsIgnoreCase("yes")) {
//			loginPage.logout();
//		}
		
		//Assert all checkpoints
		checkpoint.assertAll();
	}
}