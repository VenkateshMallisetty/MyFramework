package TestScripts.SmokeTest;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Gmail.BusinessLogic.GmailLoginBusinessLogic;
import com.Gmail.Utilities.Xls_Reader;
import com.Gmail.accelerators.TestEngine;

public class TC_ID_01 extends TestEngine
{
	private String sActulResult = null;
	private String sTestStepName  = null;
	WebDriver driver = null;
	boolean bflag = false;
	Xls_Reader datatable = null;
	int iRowNo;
	String sheetName = null;
	private String sExpectedResult = null;
	GmailLoginBusinessLogic Login = new GmailLoginBusinessLogic();
	@BeforeClass
	public void beforeClass() throws Exception
	{
		datatable=new Xls_Reader(getTestDataPath());
		sheetName = getSheetName();
		iRowNo = datatable.getCellRowNum(sheetName, "tTestCaseID", "TC01");
		String eExecute = datatable.getCellData(sheetName, "tExecute", iRowNo);
		//String url=datatable.getCellData(eExecute, "tURL", iRowNo);
		report.setTestCaseName(datatable.getCellData(sheetName, "tTestCaseID", iRowNo) +"-"+datatable.getCellData(sheetName, "tTestCaseDescription", iRowNo));
		//report.setTestCaseName(datatable.getCellData(sheetName, "tTestCaseID", iRowNo));
		report.stepReset();
		if(eExecute.equalsIgnoreCase("Yes"))
		{
			driver = getBrowser();
			if(driver !=null)
			{
				report.setStepName("Launch Browser");
				report.setExpectedResult("Browser should be opened");
				reportLogger(true, "Browser Open", "Browser is open");
			}
		}
		else
		{
			throw new SkipException("Test Case Skipped");
		}
			
	}
	@Test(priority = 1)
	public  void step1() throws Throwable
	{ 
		bflag = true;
		sTestStepName="Open the Browser and Enter the Google Login Page URL";		
		report.setStepName(sTestStepName);
		sExpectedResult = "The Google Login Page should be displayed";
		report.setExpectedResult(sExpectedResult);
		
		try
		{
			bflag=Login.enterGmailURL(driver,datatable.getCellData(sheetName, "tPageTitle", iRowNo));
		} 
		catch (Throwable e) 
		{			 
			e.printStackTrace(); 
		}
		
		if (bflag)
			sActulResult = "The Gmail Login  Page is displayed";		
		else	
			sActulResult = "The Gmail Login Page is NOT displayed";			
			
		reportLogger(bflag, sTestStepName, sActulResult );
	}	
	@Test(priority = 2)
	public void step2() throws Throwable
	{
		bflag=true;
		sTestStepName="Click on 'SIGNIN' button";		
		report.setStepName(sTestStepName);
		sExpectedResult = "SIGNIN button is should be clicked";
		report.setExpectedResult(sExpectedResult);
		
		try
		{			 
			Login.ClickSignInbutton(driver);	
		}
		catch (Throwable e) 
		{			
			e.printStackTrace();
		}
		
		if (bflag)
			sActulResult = "SignIn button is clicked";		
		else	
			sActulResult = "SignIn button NOT clicked";			
			
		reportLogger(bflag, sTestStepName, sActulResult );		
	}
	@Test(priority = 3)
	public void step3() throws Throwable
	{
		bflag=true;
		sTestStepName="Enter The login credentials and click on SININ Button";		
		report.setStepName(sTestStepName);
		sExpectedResult = "Enter The login credentials  and click on SININ Button should be displayed";
		report.setExpectedResult(sExpectedResult);
		
		try
		{	
			String username = datatable.getCellData(sheetName, "tUserName",iRowNo);
			String password = datatable.getCellData(sheetName, "tPassword",iRowNo);
			Login.enterloginCredentials(driver, username, password);
		}
		catch (Throwable e) 
		{			
			e.printStackTrace();
		}
		
		if (bflag)
			sActulResult = "Enter The login credentials and click on SININ Button operation is performed";		
		else	
			sActulResult = "Enter The login credentials and click on SININ Button operation  is not performed";			
			
		reportLogger(bflag, sTestStepName, sActulResult );		
	}
	
	@AfterClass
	public void afterClass()
	{
		if(driver!=null)
		{
			driver.close();
			driver.quit();
		}
			
	
	
		
	}
}
	


