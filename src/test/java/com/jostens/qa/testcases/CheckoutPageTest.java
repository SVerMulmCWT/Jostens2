package com.jostens.qa.testcases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jostens.qa.base.TestBase;
import com.jostens.qa.pages.CheckoutPage;
import com.jostens.qa.util.ExtentFactory;
import com.jostens.qa.util.TestUtil;
import com.relevantcodes.extentreports.LogStatus;

public class CheckoutPageTest extends TestBase {
	//Define Variable(s)
	SoftAssert checkpoint;
	
	//Constructor
	public CheckoutPageTest() {
		super();
	}
	
	//Setup variable(s) and other info for the class
	@BeforeClass
	public void beforeClass() {
		//Initialize Variable(s)
		genMethods = new TestUtil();
		genMethods.setSheetName("Checkout");
		column = 16;
		
		//Setup the Report
		report = ExtentFactory.getInstance();
		reportLogger = report.startTest("CheckoutPageTest Script");
		
		//Initialize PageFactories
		System.out.println("Initializing the script's PageFactories");
		reportLogger.log(LogStatus.INFO, "Initializing the script's PageFactories");
		
		checkoutPage = new CheckoutPage(eDriver, reportLogger);
	}
	
	@Test(dataProvider="inputs", dataProviderClass=TestUtil.class)
	public void eproceedWithCheckoutTest(String product, String productQuantity, String productPrice, String checkoutPageTitle, String email, String enableEmails, String firstName, String lastName, String address, String city, String country, String state, String zipCode, String phoneNumber, String saveInfo, String finalResult, String dataRow) throws InterruptedException {
		System.out.println("@Test - proceedWithCheckoutTest()");
		
		//Initialize Variable(s)
		checkpoint = new SoftAssert(); //SoftAssert Setup (for identifying checkpoints)
		iteration = Integer.valueOf(dataRow); //Indicates which row of Excel data the @Test is reading & which row to output the results
//		if (!dataRow.equals("3")) {
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
//		}
		
		//Change the Excel Datasheet for the next @Test's input parameters
		genMethods.setSheetName("Checkout");
	}
	
	@Test(dataProvider="inputs", dataProviderClass=TestUtil.class)
	public void ftest2(String product, String productQuantity, String productPrice, String checkoutPageTitle, String email, String enableEmails, String firstName, String lastName, String address, String city, String country, String state, String zipCode, String phoneNumber, String saveInfo, String finalResult, String dataRow) {
		checkpoint = checkoutPage.verifyProductFromCheckout(checkpoint, product, productQuantity, productPrice);
	}
}