package com.jostens.qa.testcases;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jostens.qa.base.TestBase;
import com.jostens.qa.pages.PaymentPage;
import com.jostens.qa.util.ExcelUtil;
import com.jostens.qa.util.ExtentFactory;
import com.relevantcodes.extentreports.LogStatus;

public class FPaymentPageTest extends TestBase {
	//Define Variable(s)
	SoftAssert checkpoint;
	
	//Constructor
	public FPaymentPageTest() {
		super();
	}
	
	//Setup variable(s) and other info for the class
	@BeforeClass
	@Parameters({"dataTable"})
	public void beforeClass(String excelPath) {
		//Initialize Variable(s)
		excelMethods = new ExcelUtil();
		excelMethods.setDataTablePath(excelPath);
		excelMethods.setSheetName("Payment");
		column = 6;
		
		//Setup the Report
		report = ExtentFactory.getInstance();
		reportLogger = report.startTest("PaymentPageTest Script");
		
		//Initialize PageFactories
		System.out.println("Initializing the script's PageFactories");
		reportLogger.log(LogStatus.INFO, "Initializing the script's PageFactories");
		
		paymentPage = new PaymentPage(eDriver, reportLogger);
	}
	
	@Test(dataProvider="inputs", dataProviderClass=ExcelUtil.class)
	public void paymentPageTest(String creditCardNumber, String cardHolderName, String expiryDate, String securityCode, String expectedErrorMessage, String finalStatus, String notes, String dataRow) throws InterruptedException {
		System.out.println("@Test - PaymentPageTest()");
		
		if (creditCardNumber.equals("") || creditCardNumber == null) {
			throw new SkipException("Skipping '@Test=gtest' due to a lack of data");
		}
		
//		//Initialize Variable(s)
//		checkpoint = new SoftAssert(); //SoftAssert Setup (for identifying checkpoints)
//		iteration = Integer.valueOf(dataRow); //Indicates which row of Excel data the @Test is reading & which row to output the results
//		
//		//Click to continue to the payment page
//		paymentPage.proceedToPayment();
//		
//		//Enter the user's credit card info
//		paymentPage.enterCreditCardInfo(creditCardNumber, cardHolderName, expiryDate, securityCode);
//		
//		//Click 'Pay Now' to confirm payment
//		paymentPage.proceedToPayment();
//		
//		Thread.sleep(12000);
//		
//		//Check if the expected error message appears
//		checkpoint = paymentPage.verifyPayment(checkpoint, expectedErrorMessage);
	}
}