package com.jostens.qa.testcases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jostens.qa.base.TestBase;
import com.jostens.qa.pages.SearchSchoolPage;
import com.jostens.qa.util.ExcelUtil;
import com.jostens.qa.util.ExtentFactory;
import com.relevantcodes.extentreports.LogStatus;

public class BSearchSchoolPageTest extends TestBase {
	
	//Define Variable(s)
	SoftAssert checkpoint;
	
	//Constructor
	public BSearchSchoolPageTest() {
		super();
	}
	
	//Setup variable(s) and other info for the class
	@BeforeClass
	@Parameters({"dataTable"})
	public void beforeClass(String excelPath) {
		//Initialize Variable(s)
		excelMethods = new ExcelUtil();
		excelMethods.setDataTablePath(excelPath);
		excelMethods.setSheetName("School Search");
		column = 3;
		
		//Setup the Report
		report = ExtentFactory.getInstance();
		reportLogger = report.startTest("SearchSchoolPageTest Script");
		
		//Initialize PageFactories
		System.out.println("Initializing the script's PageFactories");
		reportLogger.log(LogStatus.INFO, "Initializing the script's PageFactories");
		
		searchSchoolPage = new SearchSchoolPage(eDriver, reportLogger);
	}
	
	//Test the school search functionality
	@Test(dataProvider="inputs", dataProviderClass=ExcelUtil.class)
	public void searchSchoolTest(String schoolName, String schoolStoreTitle, String finalStatus, String dataRow) {
		System.out.println("@Test - SearchSchoolPageTest()");
		
		//Initialize Variable(s)
		checkpoint = new SoftAssert(); //SoftAssert Setup (for identifying checkpoints)
		iteration = Integer.valueOf(dataRow); //Indicates which row of Excel data the @Test is reading & which row to output the results
		
		//Go to the 'Shop Products' page of the Jostens.com website
		if (iteration == 1) {
			searchSchoolPage.goToSchoolSearch();
		} else {
			searchSchoolPage.goToChangeSchool();
		}
		
		//Search for the desired school
		searchSchoolPage.searchSchool(schoolName);
		
		//Check if the school search was successful
		checkpoint = searchSchoolPage.verifySuccessfulSchoolSearch(checkpoint, schoolStoreTitle);
		
		//Assert all checkpoints
		checkpoint.assertAll();
	}
}