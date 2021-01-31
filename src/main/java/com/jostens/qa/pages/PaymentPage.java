package com.jostens.qa.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.asserts.SoftAssert;

import com.jostens.qa.locators.PaymentPageLocators;
import com.jostens.qa.util.TestUtil;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class PaymentPage extends PaymentPageLocators {
	
	//Initialize Variable(s)
	ExtentTest reportLogger;
	EventFiringWebDriver eDriver;
	
	TestUtil genMethods;
	
	//Constructor
	public PaymentPage(EventFiringWebDriver eDriver, ExtentTest reportLogger) {
		this.reportLogger = reportLogger;
		this.eDriver = eDriver;
		PageFactory.initElements(eDriver, this);
		
		//initialize the generic methods for this class
		genMethods = new TestUtil();
	}
	
	public void proceedToPayment() {
		//Output a message to the report & system
		System.out.println("Submitting the user's credit card info for payment");
		reportLogger.log(LogStatus.INFO, "Submitting the user's credit card info for payment");
		
		//Click to enter the Payment Page
		continueToPaymentButton.click();
	}
	
	public void enterCreditCardNumber(String creditCardNumber) {
		//Output a message to the report & system
		System.out.println("Entering the user's credit card number at the payment page");
		reportLogger.log(LogStatus.INFO, "Entering the user's credit card number at the payment page");
		
		//Initialize Variable(s)
		List<WebElement> iframeList = eDriver.findElements(By.tagName("iframe"));
		
		//Switch to the correct iframe
		System.out.println(iframeList.get(1).getAttribute("id"));
		eDriver.switchTo().frame(iframeList.get(1).getAttribute("id"));
		
		//Enter the Credit Card Number
		creditCardNumberField.sendKeys(creditCardNumber.substring(0, 4));
		creditCardNumberField.sendKeys(creditCardNumber.substring(4, 8));
		creditCardNumberField.sendKeys(creditCardNumber.substring(8, 12));
		creditCardNumberField.sendKeys(creditCardNumber.substring(12, 16));
		
		//Switch back to the main html page
		eDriver.switchTo().defaultContent();
	}
	
	public void enterCardHolderName(String cardHolderName) {
		//Output a message to the report & system
		System.out.println("Entering the user's credit card holder name at the payment page");
		reportLogger.log(LogStatus.INFO, "Entering the user's credit card holder name at the payment page");
		
		//Initialize Variable(s)
		List<WebElement> iframeList = eDriver.findElements(By.tagName("iframe"));
		
		//Switch to the correct iframe
		System.out.println(iframeList.get(2).getAttribute("id"));
		eDriver.switchTo().frame(iframeList.get(2).getAttribute("id"));
		
		//Enter the Credit Card Number
		cardHolderNameField.sendKeys(cardHolderName);
		
		//Switch back to the main html page
		eDriver.switchTo().defaultContent();
	}
	
	public void enterExpirationDate(String expiryDate) {
		//Output a message to the report & system
		System.out.println("Entering the user's credit card expiry date at the payment page");
		reportLogger.log(LogStatus.INFO, "Entering the user's credit card expiry date at the payment page");
		
		//Initialize Variable(s)
		List<WebElement> iframeList = eDriver.findElements(By.tagName("iframe"));
		
		//Switch to the correct iframe
		System.out.println(iframeList.get(3).getAttribute("id"));
		eDriver.switchTo().frame(iframeList.get(3).getAttribute("id"));
		
		//Enter the Credit Card Number
		expirationDateField.sendKeys(expiryDate.substring(0, 2));
		expirationDateField.sendKeys(expiryDate.substring(2, 4));
		
		//Switch back to the main html page
		eDriver.switchTo().defaultContent();
	}
	
	public void enterSecurityCode(String securityCode) {
		//Output a message to the report & system
		System.out.println("Entering the user's credit card security code at the payment page");
		reportLogger.log(LogStatus.INFO, "Entering the user's credit card security code at the payment page");
		
		//Initialize Variable(s)
		List<WebElement> iframeList = eDriver.findElements(By.tagName("iframe"));
		
		//Switch to the correct iframe
		System.out.println(iframeList.get(4).getAttribute("id"));
		eDriver.switchTo().frame(iframeList.get(4).getAttribute("id"));
		
		//Enter the Credit Card Number
		securityCodeField.sendKeys(securityCode);
		
		//Switch back to the main html page
		eDriver.switchTo().defaultContent();
	}
	
	public void submitPayment() {
		//Output a message to the report & system
		System.out.println("Submitting the user's credit card info for payment");
		reportLogger.log(LogStatus.INFO, "Submitting the user's credit card info for payment");
		
		//Click to pay for the products
		payNowButton.click();
	}
	
	public void enterCreditCardInfo(String creditCardNumber, String cardHolderName, String expiryDate, String securityCode) {
		enterCreditCardNumber(creditCardNumber);
		enterCardHolderName(cardHolderName);
		enterExpirationDate(expiryDate);
		enterSecurityCode(securityCode);
	}
	
	public SoftAssert verifyPayment(SoftAssert softAssert, String errorMessage) {
//		//Initialize Variable(s)
//		System.out.println(paymentErrorMessage.getText());
//		System.out.println(paymentErrorMessage.getAttribute("class"));
		
		//Check if the payment's error message matches expectation
		softAssert.assertEquals(paymentErrorMessage.getText(), errorMessage);
//		softAssert.assertTrue(payNowButton.isDisplayed());
		
		//Return the status for the SoftAssert
		return softAssert;
	}
}