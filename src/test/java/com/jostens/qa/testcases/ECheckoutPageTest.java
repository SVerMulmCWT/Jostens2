package com.jostens.qa.testcases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jostens.qa.base.TestBase;
import com.jostens.qa.pages.CheckoutPage;
import com.jostens.qa.util.ExcelUtil;
import com.jostens.qa.util.ExtentFactory;
import com.relevantcodes.extentreports.LogStatus;

public class ECheckoutPageTest extends TestBase {
	//Define Variable(s)
	SoftAssert checkpoint;
	
	//Constructor
	public ECheckoutPageTest() {
		super();
	}
	
	//Setup variable(s) and other info for the class
	@BeforeClass
	@Parameters({"dataTable"})
	public void beforeClass(String excelPath) {
		//Initialize Variable(s)
		excelMethods = new ExcelUtil();
		excelMethods.setDataTablePath(excelPath);
		excelMethods.setSheetName("Checkout");
		column = 16;
		
		//Setup the Report
		report = ExtentFactory.getInstance();
		reportLogger = report.startTest("CheckoutPageTest Script");
		
		//Initialize PageFactories
		System.out.println("Initializing the script's PageFactories");
		reportLogger.log(LogStatus.INFO, "Initializing the script's PageFactories");
		
		checkoutPage = new CheckoutPage(eDriver, reportLogger);
	}
	
//	@Test(dataProvider="inputs", dataProviderClass=ExcelUtil.class)
//	public void checkoutListVerification(String product, String productQuantity, String productPrice, String checkoutPageTitle, String email, String enableEmails, String firstName, String lastName, String address, String city, String country, String state, String zipCode, String phoneNumber, String saveInfo, String finalResult, String notes, String dataRow) {
//		System.out.println("@Test - checkoutListVerification()");
//		
//		//Verify that the checkout products matches expectations
//		checkpoint = checkoutPage.verifyProductFromCheckout(checkpoint, product, productQuantity, productPrice);
//		
////		//Assert all checkpoints
////		checkpoint.assertAll();
//	}
	
	@Test(dataProvider="inputs", dataProviderClass=ExcelUtil.class)
	public void proceedWithCheckoutTest(String product, String productQuantity, String productPrice, String checkoutPageTitle, String email, String enableEmails, String firstName, String lastName, String address, String city, String country, String state, String zipCode, String phoneNumber, String saveInfo, String finalResult, String notes, String dataRow) throws InterruptedException {
		System.out.println("@Test - proceedWithCheckoutTest()");
		
		//Initialize Variable(s)
		checkpoint = new SoftAssert(); //SoftAssert Setup (for identifying checkpoints)
		iteration = Integer.valueOf(dataRow); //Indicates which row of Excel data the @Test is reading & which row to output the results
		
		//Proceed to the Checkout Page
		if (dataRow.equals("1")) {
			checkoutPage.proceedToCheckout();
		} else {
			checkoutPage.returnToShippingInputPage();
		}
		
		//Check if the Checkout Page successfully loaded
		checkpoint = checkoutPage.verifyCheckoutPage(checkpoint, checkoutPageTitle);
		
		//Enter the Checkout Shipping Info
		checkoutPage.enterEmail(email, enableEmails);
		checkoutPage.enterName(firstName, lastName);
		checkoutPage.enterAddress(address, city, country, state, zipCode);
		checkoutPage.enterPhoneNumber(phoneNumber);
		
		//Pause the script for a bit
		Thread.sleep(2000);
		
		//Proceed to the Shipping Page
		checkoutPage.continueToShipping(saveInfo);
		
		checkpoint = checkoutPage.verifyShippingInfo(checkpoint, email, address, city, state, zipCode, country);
		
		//Assert all checkpoints
		checkpoint.assertAll();
		
		//Change the Excel Datasheet for the next @Test's input parameters
		excelMethods.setSheetName("Checkout");
			
	}
}