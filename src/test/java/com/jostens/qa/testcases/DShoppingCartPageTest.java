package com.jostens.qa.testcases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jostens.qa.base.TestBase;
import com.jostens.qa.pages.ShoppingCartPage;
import com.jostens.qa.util.ExcelUtil;
import com.jostens.qa.util.ExtentFactory;
import com.relevantcodes.extentreports.LogStatus;

public class DShoppingCartPageTest extends TestBase {
	//Define Variable(s)
	SoftAssert checkpoint;
	
	//Constructor
	public DShoppingCartPageTest() {
		super();
	}
	
	//Setup variable(s) and other info for the class
	@BeforeClass
	@Parameters({"dataTable"})
	public void beforeClass(String excelPath) {
		//Initialize Variable(s)
		excelMethods = new ExcelUtil();
		excelMethods.setDataTablePath(excelPath);
		excelMethods.setSheetName("Shopping Cart");
		column = 7;
		
		//Setup the Report
		report = ExtentFactory.getInstance();
		reportLogger = report.startTest("ShoppingCartPageTest Script");
		
		//Initialize PageFactories
		System.out.println("Initializing the script's PageFactories");
		reportLogger.log(LogStatus.INFO, "Initializing the script's PageFactories");
		
		shoppingCartPage = new ShoppingCartPage(eDriver, reportLogger);
	}
	
	@Test(dataProvider="inputs", dataProviderClass=ExcelUtil.class)
	public void addProductToCartTest(String productName, String shoppingCartTitle, String pricePerItem, String itemCount, String itemPriceTotal, String itemSubtotal, String finalResult, String dataRow) throws InterruptedException {
		System.out.println("@Test - ShoppingCartPageTest()");
		
		//Initialize Variable(s)
		checkpoint = new SoftAssert(); //SoftAssert Setup (for identifying checkpoints)
		iteration = Integer.valueOf(dataRow); //Indicates which row of Excel data the @Test is reading & which row to output the results
		
		//Proceed to the Shopping Cart - only need to do this the one/first time
		if (dataRow.equals("1")) {
			shoppingCartPage.proceedToShoppingCart();
		}
		
		//Check if the Shopping Cart Page successfully loaded the relevant product's page
		checkpoint = shoppingCartPage.verifyShoppingCartPage(checkpoint, shoppingCartTitle);
		
		//Check if the Shopping Cart contains the relevant product info
		checkpoint = shoppingCartPage.verifyShoppingCart(checkpoint, pricePerItem, itemCount, itemPriceTotal, itemSubtotal, dataRow);
		
		//Assert all checkpoints
		checkpoint.assertAll();
	}
}