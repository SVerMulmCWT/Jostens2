package com.jostens.qa.testcases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jostens.qa.base.TestBase;
import com.jostens.qa.pages.ProductDetailPage;
import com.jostens.qa.util.ExcelUtil;
import com.jostens.qa.util.ExtentFactory;
import com.relevantcodes.extentreports.LogStatus;

public class CProductDetailPageTest extends TestBase {
	
	//Define Variable(s)
	SoftAssert checkpoint;
	
	//Constructor
	public CProductDetailPageTest() {
		super();
	}
	
	//Setup variable(s) and other info for the class
	@BeforeClass
	@Parameters({"dataTable"})
	public void beforeClass(String excelPath) {
		//Initialize Variable(s)
		excelMethods = new ExcelUtil();
		excelMethods.setDataTablePath(excelPath);
		excelMethods.setSheetName("Product Search");
		column = 11;
		
		//Setup the Report
		report = ExtentFactory.getInstance();
		reportLogger = report.startTest("ProductDetailPageTest Script");
		
		//Initialize PageFactories
		System.out.println("Initializing the script's PageFactories");
		reportLogger.log(LogStatus.INFO, "Initializing the script's PageFactories");
		
		productDetailPage = new ProductDetailPage(eDriver, reportLogger);
	}
	
	//Test the product search functionality
	@Test(dataProvider="inputs", dataProviderClass=ExcelUtil.class)
	public void productSearchTest(String product, String productPageTitle, String productColor, String productSize, String logoNumber, String logoPosition, String logoLeftValue, String logoTopValue, String logoWidthValue, String logoHeightValue, String finalResult, String dataRow) throws InterruptedException {
		System.out.println("@Test - ProductDetailPageTest()");
		
		//Initialize Variable(s)
		checkpoint = new SoftAssert(); //SoftAssert Setup (for identifying checkpoints)
		int logoNum = Integer.valueOf(logoNumber);
		
		if (dataRow.equals("1")) {
			//Go to the School's Store Page
			productDetailPage.clickSchoolStoreButton();
			
			//Accept the cookies on the School's Store Page
			productDetailPage.acceptCookies();
		}
		
		//Select the desired product
		productDetailPage.selectProduct(product);
		
		//Check if the product search successfully loaded the relevant product's page
		checkpoint = productDetailPage.verifySuccessfulProductSearch(checkpoint, productPageTitle);
		
		//Select the product's color
		productDetailPage.selectProductColor(productColor);
		
		//Select the product's size
		productDetailPage.selectProductSize(productSize);
		
		//Select the product's design
		productDetailPage.selectProductLogo(logoNum);
		
		//Select the product's design position
		productDetailPage.selectProductLogoPosition(logoPosition);
		
		//Check that the correct design & design position were selected correctly
		checkpoint = productDetailPage.verifyProductLogoCoordinates(checkpoint, logoLeftValue, logoTopValue, logoWidthValue, logoHeightValue);
		
		Thread.sleep(3000);
		
		//Add the product into the cart
		productDetailPage.addToCart();
		
		Thread.sleep(12000);
		
		//Go back to the store
		productDetailPage.returnToSchoolStore();
		
		//Assert all checkpoints
		checkpoint.assertAll();
	}
}