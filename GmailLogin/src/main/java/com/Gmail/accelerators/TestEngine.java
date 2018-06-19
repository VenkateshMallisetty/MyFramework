package com.Gmail.accelerators;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
//import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
//import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.Gmail.Utilities.SendMail;

import com.Gmail.reports.HTMLReporting;
import com.Gmail.reports.HTMLReporting.StepStatus;
import com.Gmail.Support.ConfiguratorSupport;
import com.Gmail.Support.ExcelReader;
import com.Gmail.Support.HTMLReportSupport;
import com.Gmail.Support.MyListener;
import com.Gmail.Support.ReportStampSupport;
import com.Gmail.Utilities.SendMail;
import com.Gmail.Utilities.Zip;

@SuppressWarnings("all")

public class TestEngine extends SendMail
{
	public static Logger logger = Logger.getLogger(TestEngine.class.getName());
	public static HTMLReporting report = new HTMLReporting();
	public static ConfiguratorSupport configProps = new ConfiguratorSupport(
			"config.properties");

	public static ConfiguratorSupport counterProp = new ConfiguratorSupport(
			configProps.getProperty("counterPath"));

	public String currentSuite = "";
	public String method = "";
	//public static String timeStamp = ReportStampSupport.timeStamp().replace(" ", "_").replace(":", "_").replace(".", "_");
	public static boolean flag = false;
	public static WebDriver webDriver = null;	
	public RemoteWebDriver RwebDriver = null;	
	public EventFiringWebDriver driver=null;
	public DesiredCapabilities capabilities,cp2;
	public static String suiteStartTime = "";
	public static  int stepNum = 0;
	public static  int PassNum =0;
	public  int FailNum =0;

	//public   failCounter =0;
	public String testName = "";
	public String testCaseExecutionTime = "";
	public StringBuffer x=new StringBuffer();
	public String finalTime = "";
	public boolean isSuiteRunning=false;
	public static Map<String, String> testDescription = new LinkedHashMap<String, String>();
	public static Map<String, String> testResults = new LinkedHashMap<String, String>();
	public String url=null;
	public static ExcelReader xlsrdr;
	//public static ExcelReader xlsrdr = new ExcelReader(configProps.getProperty("TestData"),configProps.getProperty("sheetName0"));
	public static String firefoxVersion="";
	public static String chromeVersion="";
	public static String operaVersion="";
	public static String safariVersion="";
	public static String ieVersion="";
	public static String allbrowser="";
	public static int firefoxfailstepcount=0;
	public static int chromefailstepcount=0;
	public static int iefailstepcount=0;
	public static int safarifailstepcount=0;
	public static int firefoxpassstepcount=0;
	public static int chromepassstepcount=0;
	public static int iepassstepcount=0;
	public static int safaripassstepcount=0;
	public static int operapassstepcount=0;
	public static int operafailstepcount=0;
	public  static int firefoxpassCounter =0;
	public  static int firefoxfailCounter =0;
	public  static int chromepassCounter =0;
	public  static int chromefailCounter =0;
	public  static int iepassCounter =0;
	public  static int iefailCounter =0;
	public  static int safaripassCounter =0;
	public  static int safarifailCounter =0;
	public  static int operafailCounter = 0;
	public  static int operapassCounter = 0;
	public static int driverCount=0;
	public static String executionType="";
	public static String suiteExecution="";
	public  String className="";
	public static String myClass="";
	public static ChromeDriverService service;
	public static String zipresultpath = "";


	/*
	 * public static Screen s; public static String url =
	 * "jdbc:mysql://172.16.6.121/"; public static String dbName = "test";
	 * public static String userName = "root"; public static Connection conn =
	 * null; public static Statement stmt = null; public static
	 * PreparedStatement pstmt = null; public static ResultSet rs = null;
	 */
	public int countcompare = 0;

	public static  String browser = "chrome";
	public String language = null;
	static int len = 0;
	static int i = 0;
	public static ITestContext itc;
	public static String groupNames =null;

	/**
	 * Initializing browser requirements, Test Results file path and Database
	 * requirements from the configuration file
	 * 
	 * @Date 19/05/2016
	 * @Revision History
	 * 
	 */
	//@Parameters({"TestSheet","lang","TESTURL"})ITestContext ctx,String TestSheet,String lang, String TESTURL
	@BeforeSuite(alwaysRun=true)
	public void beforeSuite() throws Throwable{
		report.reset();
		executionType="Url: " + configProps.getProperty("URL");
		suiteExecution="Test suite for " + configProps.getProperty("lang") + " language";	
		xlsrdr = new ExcelReader(configProps.getProperty("TestData"),configProps.getProperty("TestData"));
		//PropertyConfigurator.configure(System.getProperty("user.dir")+"\\Log.properties");
		ReportStampSupport.calculateSuiteStartTime();
		//	ObjectRepository.storeIdentification();
		//ObjectRepository.storeValue();

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy hh mm ss SSS");
		String formattedDate = sdf.format(date);
		suiteStartTime = formattedDate.replace(":","_").replace(" ","_");
		this.language = configProps.getProperty("lang");
	//	report.initializeHtmlReport();



	}


	/*@Parameters({"browser","TESTURL","HubURL"})
	@BeforeClass(alwaysRun=true)*/
	public void first(ITestContext ctx, String browser,String TESTURL, String HubURL) throws Throwable{
		String ClassName=this.getClass().getName().replace('.','-');
		//System.out.println("ClassName : " + ClassName);
		//String s="com.sugarcrm.scripts.Assignment1".replace('.', '-');
		String[] Names=ClassName.split("-");
		//System.out.println(st.length);
		for(String Name:Names)
		{
			className=Name;
		}
		itc=ctx;
		this.browser=browser;
		if(browser.equalsIgnoreCase("firefox"))
		{
			if(configProps.getProperty("Remote").trim().equalsIgnoreCase("False")){

				String folderName = "temp";
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("browser.download.manager.showWhenStarting",
						false);
				profile.setPreference("browser.download.dir", folderName);
				profile.setPreference("browser.download.downloadDir", folderName);
				profile.setPreference("browser.download.defaultFolder", folderName);
				profile.setPreference(
						"browser.helperApps.neverAsk.saveToDisk",
						"application/msword,application/x-rar-compressed,application/octet-stream,application/zip, application/octet-stream,application/csv,text/csv,application/java-archive,application/all");
				webDriver = new FirefoxDriver(profile);
			}else{
				DesiredCapabilities dr = DesiredCapabilities.firefox();	
				dr.setCapability("requireWindowFocus", true);						
				dr.setPlatform(Platform.VISTA);		            	   
				webDriver=new RemoteWebDriver(new URL(HubURL), dr);
			}
			String s = (String) ((JavascriptExecutor) webDriver).executeScript("return navigator.userAgent;");
			String data=s.split("Firefox/")[1];
			firefoxVersion=data.split(" ")[0];


		}
		else if(browser.startsWith("IE"))
		{
			if(configProps.getProperty("Remote").trim().equalsIgnoreCase("False")){					

				File file = new File("Drivers/IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				webDriver= new InternetExplorerDriver();
				i=i+1;	
			}else{
				DesiredCapabilities dr = DesiredCapabilities.internetExplorer();										
				if(browser.equalsIgnoreCase("IE7")){						
					dr.setCapability(CapabilityType.PLATFORM, Platform.XP);
					dr.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);									
					webDriver=new RemoteWebDriver(new URL(HubURL), dr);
					i=i+1;						
				}else{
					dr.setCapability(CapabilityType.PLATFORM, Platform.VISTA);
					dr.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);									
					webDriver=new RemoteWebDriver(new URL(HubURL), dr);
					i=i+1;						
				}

			}

			//String IEVersioncmd = "reg query \"HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Internet Explorer\" /v svcVersion";


			//java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(IEVersioncmd).getInputStream()).useDelimiter("\\A");
			// String data= s.hasNext() ? s.next() : "";
			//ieVersion=data.split("REG_SZ")[1].trim();			
		}
		else if(browser.equalsIgnoreCase("chrome"))
		{
			if(configProps.getProperty("Remote").trim().equalsIgnoreCase("False")){
				System.setProperty("webdriver.chrome.driver", "Drivers/chromedriver.exe");				
				webDriver=new ChromeDriver();
			}else{
				DesiredCapabilities dr = DesiredCapabilities.chrome();					
				dr.setPlatform(Platform.VISTA); 	              
				webDriver=new RemoteWebDriver(new URL(HubURL), dr);
			}
			i=i+1;
			String s = (String) ((JavascriptExecutor) webDriver).executeScript("return navigator.userAgent;");
			String data=s.split("Chrome/")[1];
			chromeVersion=data.split(" ")[0];

		}
		else if(browser.equalsIgnoreCase("Safari"))
		{
			for(int i=1;i<=10;i++){

				try{
					webDriver=new SafariDriver();

					break;
				}catch(Exception e1){
					Runtime.getRuntime().exec("taskkill /F /IM Safari.exe");
					Thread.sleep(3000);
					Runtime.getRuntime().exec("taskkill /F /IM plugin-container.exe");
					Runtime.getRuntime().exec("taskkill /F /IM WerFault.exe"); 

					continue;   


				}

			}

			i=i+1;

			String s = (String) ((JavascriptExecutor) webDriver).executeScript("return navigator.userAgent;");
			String data=s.split("Version/")[1];
			safariVersion=data.split(" ")[0];

		}




		driver = new EventFiringWebDriver(webDriver);

		driver.manage().window().maximize();

		//url = (configProps.getProperty("URL"));

		url = TESTURL; 
		//Chenge the url for scenario 3
		if(ClassName.contains("Sc3_GenNav_ICBCS")){
			url = (configProps.getProperty("SC3URL"));
		}
		MyListener myListener = new MyListener();
		driver.register(myListener);
		driverCount++;
		//	s = new Screen();
		try {
			driver.get(url);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			if(browser.toUpperCase().startsWith("IE")){
				driver.manage().deleteAllCookies();
				//driver.navigate().refresh();
				driver.get(url);
				//Thread.sleep(10000);
			}
			if(browser.equalsIgnoreCase("IE7")){
				@SuppressWarnings("resource")
				String s = (String) ((JavascriptExecutor) webDriver).executeScript("return navigator.userAgent;");
				String data=s.split("MSIE")[1].trim();
				operaVersion= " " + data.split(";")[0]; 			
			}else{
				@SuppressWarnings("resource")
				String s = (String) ((JavascriptExecutor) webDriver).executeScript("return navigator.userAgent;");
				String data=s.split("rv:")[1];
				ieVersion=" " + data.split("\\)")[0];
			}
			reportCreater();
			currentSuit = ctx.getCurrentXmlTest().getSuite().getName();
		} catch (Exception e1) {
			System.out.println(e1);
		}

	}

	private void SetCapability(String version, String string) {
		// TODO Auto-generated method stub

	}


	/**
	 * De-Initializing and closing all the connections
	 * 
	 * @throws Throwable
	 */

	//@Parameters({"browser"})
	@AfterSuite(alwaysRun = true)
	public void tearDownAfterSuite() throws Throwable {
		report.initializeHtmlReport();
		HTMLReportSupport.copyLogos();
		
		
		String str = configProps.getProperty("screenShotPath") +  "AutomationTestResults"+ ".html";
		String strPath = System.getProperty("user.dir")+str.substring(1, str.length());
		
		File htmlFile = new File(strPath);
		
		String strZipFilePath = TestEngine.filePath().replaceAll("\\s+", "_") + ".Zip";		
		zipresultpath = new File(strZipFilePath).getCanonicalPath();
		
		//VP - Modified code as Zip issue resolution 
		File dir = new File(".\\Results\\AttachmentResults");
		
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		Zip.zipFolder(TestEngine.filePath(), configProps.getProperty("tempZipPath")+"HTML"+suiteStartTime+".Zip");
		
		Zip.zipFolder(configProps.getProperty("testResulsScreensFail"), configProps.getProperty("zipScreenShotDest"));
		
		File source = new File(str);
	    File dest = new File(configProps.getProperty("tempZipPath")+"AutomationTestResults"+ ".html");
	    copyFileUsingStream(source, dest);
		
		Zip.zipFolder(configProps.getProperty("tempZipPath"), configProps.getProperty("zipDestination"));
		
		Desktop.getDesktop().browse(htmlFile.toURI());
		report.finish();
		
		
		if(configProps.getProperty("SendMail").equalsIgnoreCase("True")){			
		
				try {
				 dir = new File(".\\Results\\toShare");
				
				if (!dir.exists())
				{
					dir.mkdirs();
				}
				Zip.zipFolder(configProps.getProperty("testResulsScreens"), configProps.getProperty("testShare"));
				
				String desttoShare = configProps.getProperty("sharedPath").trim()+"\\ScreenShots"+".Zip";		
				//zipresultpath = new File(desttoShare).getCanonicalPath();
				
				
				File srcFolder = new File(configProps.getProperty("toCopyScreens").trim()+"\\ScreenShots"+".Zip");
		    	File destFolder = new File(new File(desttoShare).getCanonicalPath());
		    	
		    	/*dir = new File(configProps.getProperty("desttoShare"));
		    	
		    	if (!dir.getCanonicalPath().exists())
				{
					dir.mkdirs();
				}
		    	*/
		    	
			    copyFileUsingStream(srcFolder, destFolder);
			    
			}catch (Exception e) {
				e.printStackTrace();
			}
			try {
				SendMail.attachReportsToEmail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		try {
			//driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	/**
	 * Write results to Browser specific path
	 * 
	 * @Date 19/05/2016
	 * @Revision History
	 * 
	 */
	// @Parameters({"browserType"})
	public static String filePath() {

		String strDirectoy = "";

		strDirectoy = "HTML"+suiteStartTime;



		if (strDirectoy != "") {
			new File(configProps.getProperty("screenShotPath") + strDirectoy
					).mkdirs();
		}

		File results = new File(configProps.getProperty("screenShotPath") + strDirectoy+"/"+"Screenshots");
		if(!results.exists())
		{
			results.mkdir();

		}

		return configProps.getProperty("screenShotPath") + strDirectoy;

	}

	/**
	 * Browser type prefix for Run ID
	 * 
	 * @Date 19/05/2016
	 * @Revision History
	 * 
	 */
	public String result_browser() {
		if (groupNames.equals("")) {
			if (configProps.getProperty("browserType").equals("ie")) {
				return "IE";
			} else if (configProps.getProperty("browserType").equals("firefox")) {
				return "Firefox";
			} else if (configProps.getProperty("browserType").equals("chrome")) {
				return "Chrome";
			} else{
				return "Opera";
			}
		} else {
			if (browser.equalsIgnoreCase("ie")) {
				return "IE";

			} else if (browser.equalsIgnoreCase("firefox")) {
				return "Firefox";

			} else if (browser.equalsIgnoreCase("chrome")) {
				return "Chrome";

			} else{
				return "Opera";
			}
		}
	}

	/**
	 * Related to Xpath
	 * 
	 * @Date 19/05/2016
	 * @Revision History
	 * 
	 */
	public String methodName() {
		if (groupNames.equals("")) {
			if (configProps.getProperty("browserType").equals("ie")) {
				return "post";
			} else {
				return "POST";
			}
		} else {
			if (browser.equals("ie")) {
				return "post";
			} else {
				return "POST";
			}
		}
	}

	/*@Parameters({"browser"})
	@BeforeMethod(alwaysRun = true)*/
	public void reportHeader(Method method, ITestContext ctx, String browser) throws Throwable {
		if(!allbrowser.contains(browser.toUpperCase()))
			allbrowser=allbrowser+browser.toUpperCase()+" ";

		itc = ctx;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy hh mm ss SSS");
		@SuppressWarnings("unused")
		String formattedDate = sdf.format(date);
		calculateTestCaseStartTime();

		flag = false;

		//tc_name = method.getName().toString() + "-" + browser;
		String[] ts_Name = this.getClass().getName().toString().split("\\.");
		packageName = ts_Name[0] + "." + ts_Name[1] + "."+ ts_Name[2];

		myClass=className;
		className=className+"-" + browser;

		testHeader(className);

		stepNum = 0;
		PassNum = 0;
		FailNum = 0;
		testName = method.getName();
		/*	String desc="";
		if(testName.contains("_"))
		{
		String[] tmp=testName.split("_");
		desc= tmp[0]+" "+tmp[1]+" Demo";
		}
		else
			desc=testName+" Demo";*/
		testDescription.put(className, xlsrdr.getData(myClass, "Description"));

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown()
	{
		calculateTestCaseExecutionTime();
		closeDetailedReport(browser);

	}

	/*@Parameters({"browser"})
	@AfterClass(alwaysRun=true)*/
	public void close(String browser) throws Exception{

		if(FailNum!=0)
		{

			testResults.put(className, "FAIL");

			if (browser.equalsIgnoreCase("firefox")) {
				firefoxfailCounter=firefoxfailCounter+1;

			} else if(browser.equalsIgnoreCase("chrome")){
				chromefailCounter=chromefailCounter+1;
			} else if(browser.equalsIgnoreCase("ie11")){
				iefailCounter=iefailCounter+1;
			} else if(browser.equalsIgnoreCase("safari")){
				safarifailCounter=safarifailCounter+1;
			} else if(browser.equalsIgnoreCase("opera")){
				operafailCounter=operafailCounter+1;

			}	

			// Commented below code to remove JDK dependency for Switch - VP@0524
			/*switch(browser.toLowerCase())
	            {
	            case "firefox":
	            	firefoxfailCounter=firefoxfailCounter+1;
	                              break;
	            case "chrome":chromefailCounter=chromefailCounter+1;
	                             break;
	            case "ie11":iefailCounter=iefailCounter+1;
	                             break;
	            case "safari":safarifailCounter=safarifailCounter+1;
	                             break;
	            case "ie7":operafailCounter=operafailCounter+1;
                break;
	            }
			 */
		}
		else
		{
			testResults.put(className, "PASS");

			if (browser.equalsIgnoreCase("firefox")) {
				firefoxpassCounter=firefoxpassCounter+1;

			} else if(browser.equalsIgnoreCase("chrome")){
				chromepassCounter=chromepassCounter+1;
			} else if(browser.equalsIgnoreCase("ie11")){
				iepassCounter=iepassCounter+1;
			} else if(browser.equalsIgnoreCase("safari")){
				safaripassCounter=safaripassCounter+1;
			} else if(browser.equalsIgnoreCase("opera")){
				operapassCounter=operapassCounter+1;

			}

			// Commented below code to remove JDK dependency for Switch - VP@0524
			/*switch(browser.toLowerCase())
            {
            case "firefox":firefoxpassCounter=firefoxpassCounter+1;
                              break;
            case "chrome":chromepassCounter=chromepassCounter+1;
                             break;
            case "ie11":iepassCounter=iepassCounter+1;
                             break;
            case "safari":safaripassCounter=safaripassCounter+1;
                             break;
            case "ie7":operapassCounter=operapassCounter+1;
            break;
            }*/

		}
		PassNum=0;
		FailNum=0;
		if(browser.equalsIgnoreCase("Firefox"))
		{
			Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
			Thread.sleep(3000);
			Runtime.getRuntime().exec("taskkill /F /IM plugin-container.exe");
			Runtime.getRuntime().exec("taskkill /F /IM WerFault.exe"); 

		}
		else
		{
			driver.quit();
		}
	}


	public void reportCreater() throws Throwable {
		int intReporterType = Integer.parseInt(configProps
				.getProperty("reportsType"));

		if (intReporterType == 1) {

		} else if (intReporterType == 2){
			htmlCreateReport();

		} else {
			htmlCreateReport();

		}

		// Commented below code to remove JDK dependency for Switch - VP@0524
		/*switch (intReporterType) {
		case 1:

			break;
		case 2:

			htmlCreateReport();
			//HtmlReportSupport.createDetailedReport();

			break;
		default:

			htmlCreateReport();
			break;
		}
		 */	}

	public void calculateTestCaseStartTime(){			
		iStartTime = System.currentTimeMillis();
	}


	/***
	 * 		This method is supposed to be used in the @AfterMethod to calculate the total test case execution time 
	 * to show in Reports by taking the start time from the calculateTestCaseStartTime method.
	 */
	public void calculateTestCaseExecutionTime(){	
		iEndTime = System.currentTimeMillis();
		iExecutionTime=(iEndTime-iStartTime);
		TimeUnit.MILLISECONDS.toSeconds(iExecutionTime);
		HTMLReportSupport.executionTime.put(className,String.valueOf(TimeUnit.MILLISECONDS.toSeconds(iExecutionTime)));
		//System.out.println(tc_name+";Time :"+String.valueOf(TimeUnit.MILLISECONDS.toSeconds(iExecutionTime)));
	}



	public static  void onSuccess(String strStepName, String strStepDes,String time,boolean bool) {


		File file = new File(TestEngine.filePath() + "/" +strTestName+"_Results"
				/*+ TestEngine.timeStamp*/ + ".html");// "SummaryReport.html"
		Writer writer = null;
		stepNum = stepNum + 1;

		try {

			writer = new FileWriter(file, true);
			writer.write("<tr class='content2' >");
			writer.write("<td>" +stepNum + "</td> ");
			writer.write("<td class='justified'>" + strStepName + "</td>");
			writer.write("<td class='justified'>" + strStepDes + "</td> ");
			if(bool)
			{
				writer.write("<td class='Fail' align='center'><a  href='"+"./Screenshots"+"/"
						+ time/*+ "_" + TestEngine.timeStamp*/
						+ ".jpeg'"+" alt= Screenshot  width= 15 height=15 style='text-decoration:none;'><img  src='./Screenshots/passed.ico' width='18' height='18'/></a></td>");
			}
			else
				writer.write("<td class='Fail' align='center'><img  src='./Screenshots/passed.ico' width='18' height='18'/></a></td>");

			if (browser.equalsIgnoreCase("firefox")) {
				firefoxpassstepcount=firefoxpassstepcount+1;

			} else if (browser.equalsIgnoreCase("chrome")) {
				chromepassstepcount=chromepassstepcount+1;

			} else if (browser.equalsIgnoreCase("ie11")) {
				iepassstepcount=iepassstepcount+1;

			} else if (browser.equalsIgnoreCase("safari")) {
				safaripassstepcount=safaripassstepcount+1;

			} else if (browser.equalsIgnoreCase("opera")) {
				operapassstepcount=operapassstepcount+1;

			}
			// Commented below code to remove JDK dependency for Switch - VP@0524
			/*

            switch(browser.toLowerCase())
            {
            case "firefox":firefoxpassstepcount=firefoxpassstepcount+1;
                              break;
            case "chrome":chromepassstepcount=chromepassstepcount+1;
                             break;
            case "ie11":iepassstepcount=iepassstepcount+1;
                             break;
            case "safari":safaripassstepcount=safaripassstepcount+1;
                             break;
            case "ie7":operapassstepcount=operapassstepcount+1;
            break;
            }*/
			PassNum = PassNum + 1;
			String strPassTime = ReportStampSupport.getTime();
			writer.write("<td><small>" + strPassTime + "</small></td> ");
			writer.write("</tr> ");
			writer.close();

		}catch (Exception e) {
			TestEngine.logger.error(e.toString());
			//e.printStackTrace();
		}

	}
	public  void onWarning(String strStepName, String strStepDes,String time) {
		Writer writer = null;
		try {
			File file = new File(TestEngine.filePath() + "/" + strTestName+"_Results"
					/*+ TestEngine.timeStamp*/ + ".html");// "SummaryReport.html"

			writer = new FileWriter(file, true);
			stepNum = stepNum + 1;

			writer.write("<tr class='content2' >");
			writer.write("<td>" + stepNum + "</td> ");
			writer.write("<td class='justified'>" + strStepName + "</td>");
			writer.write("<td class='justified'>" + strStepDes + "</td> ");
			//			TestEngine.FailNum = TestEngine.FailNum + 1;


			writer.write("<td class='Fail'  align='center'><a  href='"+"./Screenshots"+"/"
					+ time /*+ "_" + TestEngine.timeStamp*/
					+ ".jpeg'"+" alt= Screenshot  width= 15 height=15 style='text-decoration:none;'><img src='./Screenshots/warning.ico' width='18' height='18'/></a></td>");

			String strFailTime = ReportStampSupport.getTime();
			writer.write("<td><small>" + strFailTime + "</small></td> ");
			writer.write("</tr> ");
			writer.close();

		} catch (Exception e) {
			TestEngine.logger.error(e.toString());
		}

	}


	/*
	 * 
	 * 
	 */
	public static  void onFailure(String strStepName, String strStepDes,String time) {
		Writer writer = null;
		try {
			File file = new File(TestEngine.filePath() + "/" + strTestName+"_Results"+
					/*	+ TestEngine.timeStamp + */".html");// "SummaryReport.html"

			writer = new FileWriter(file, true);
			stepNum = stepNum + 1;

			writer.write("<tr class='content2' >");
			writer.write("<td>" +stepNum + "</td> ");
			writer.write("<td class='justified'>" + strStepName + "</td>");
			writer.write("<td class='justified'>" + strStepDes + "</td> ");

			if (browser.equalsIgnoreCase("firefox")) {
				firefoxfailstepcount=firefoxfailstepcount+1;

			} else if (browser.equalsIgnoreCase("chrome")) {
				chromefailstepcount=chromefailstepcount+1;


			} else if (browser.equalsIgnoreCase("ie11")) {
				iefailstepcount=iefailstepcount+1;


			} else if (browser.equalsIgnoreCase("safari")) {
				safarifailstepcount=safarifailstepcount+1;


			} else if (browser.equalsIgnoreCase("opera")) {
				operafailstepcount=operafailstepcount+1;


			}

			// Commented below code to remove JDK dependency for Switch - VP@0524

			/*switch(browser.toLowerCase())
	            {
	            case "firefox":firefoxfailstepcount=firefoxfailstepcount+1;
	                            break;
	            case "chrome":chromefailstepcount=chromefailstepcount+1;
	                           break;
	            case "ie11":iefailstepcount=iefailstepcount+1;
	                         break;
	            case "safari":safarifailstepcount=safarifailstepcount+1;
	                           break;
	            case "ie7":operafailstepcount=operafailstepcount+1;
                break;
	            }

			 */

			writer.write("<td class='Fail' align='center'><a  href='"+"./Screenshots"+"/"
					+ time/*+ "_" + TestEngine.timeStamp*/
					+ ".jpeg'"+" alt= Screenshot  width= 15 height=15 style='text-decoration:none;'><img  src='./Screenshots/failed.ico' width='18' height='18'/></a></td>");

			String strFailTime = ReportStampSupport.getTime();
			writer.write("<td><small>" + strFailTime + "</small></td> ");
			writer.write("</tr> ");
			writer.close();
			//		if (!map.get(packageName + ":" + tc_name).equals("PASS")) {
			//				map.put(packageName + ":" + tc_name+":"+ tc_name, "FAIL");
			//			}
		} catch (Exception e) {
			TestEngine.logger.error(e.toString());
		}

	}

	public void closeDetailedReport(String browser) {

		if (browser.equalsIgnoreCase("firefox")) {
			PassNum=firefoxpassstepcount;
			FailNum=firefoxfailstepcount;
			firefoxpassstepcount=0;
			firefoxfailstepcount=0;

		} else if (browser.equalsIgnoreCase("chrome")) {
			PassNum=chromepassstepcount;
			FailNum=chromefailstepcount;
			chromepassstepcount=0;
			chromefailstepcount=0;

		} else if (browser.equalsIgnoreCase("ie11")) {
			PassNum=iepassstepcount;
			FailNum=iefailstepcount;
			iepassstepcount=0;
			iefailstepcount=0;

		} else if (browser.equalsIgnoreCase("safari")) {
			PassNum=safaripassstepcount;
			FailNum=safarifailstepcount;
			safaripassstepcount=0;
			safarifailstepcount=0;


		} else if (browser.equalsIgnoreCase("opera")) {
			PassNum=operapassstepcount;
			FailNum=operafailstepcount;
			operapassstepcount=0;
			operafailstepcount=0;


		}
		// Commented below code to remove JDK dependency for Switch - VP@0524
		/*switch(browser.toLowerCase())
        {
        case "firefox":PassNum=firefoxpassstepcount;
                        FailNum=firefoxfailstepcount;
                        firefoxpassstepcount=0;
                        firefoxfailstepcount=0;
                        break;
        case "chrome":PassNum=chromepassstepcount;
                      FailNum=chromefailstepcount;
                      chromepassstepcount=0;
                      chromefailstepcount=0;
                       break;
        case "ie11":PassNum=iepassstepcount;
                  FailNum=iefailstepcount;
                  iepassstepcount=0;
                  iefailstepcount=0;
                  break;
        case "safari":PassNum=safaripassstepcount;
                      FailNum=safarifailstepcount;
                      safaripassstepcount=0;
                      safarifailstepcount=0;
                      break;
        case "ie7":PassNum=operapassstepcount;
				        FailNum=operafailstepcount;
				        operapassstepcount=0;
				        operafailstepcount=0;
				        break;
        }
		 */		

		File file = new File(TestEngine.filePath() + "/" + strTestName+"_Results"
				+ ".html");// "SummaryReport.html"
		Writer writer = null;

		try {
			writer = new FileWriter(file, true);
			writer.write("</table>");
			writer.write("<table id='footer'>");
			writer.write("<colgroup>");
			writer.write("<col style='width: 25%' />");
			writer.write("<col style='width: 25%' />");
			writer.write("<col style='width: 25%' />");
			writer.write("<col style='width: 25%' />");
			writer.write("</colgroup>");
			writer.write("<tfoot>");
			writer.write("<tr class='heading'> ");
			writer.write("<th colspan='4'>Execution Time In Seconds (Includes Report Creation Time) : "
					+ executionTime.get(className)+ "&nbsp;</th> ");
			writer.write("</tr> ");
			writer.write("<tr class='content'>");
			writer.write("<td class='pass'>&nbsp;Steps Passed&nbsp;:</td>");
			writer.write("<td class='pass'> " + PassNum
					+ "</td>");
			writer.write("<td class='fail'>&nbsp;Steps Failed&nbsp;: </td>");
			writer.write("<td class='fail'>" + FailNum
					+ "</td>");
			writer.write("</tr>");
			writer.close();
		} catch (Exception e) {

		}
	}

	public void closeSummaryReport(String browser) {
		File file = new File(TestEngine.filePath() + "/" + "SummaryResults"+browser
				+ ".html");// "SummaryReport.html"
		Writer writer = null;
		try {
			writer = new FileWriter(file, true);

			writer.write("<table id='footer'>");
			writer.write("<colgroup>");
			writer.write("<col style='width: 25%' />");
			writer.write("<col style='width: 25%' />");
			writer.write("<col style='width: 25%' />");
			writer.write("<col style='width: 25%' /> ");
			writer.write("</colgroup> ");
			writer.write("<tfoot>");
			writer.write("<tr class='heading'>");
			writer.write("<th colspan='4'>Total Duration  In Seconds (Including Report Creation) : "
					+ ((int)iSuiteExecutionTime) + "</th>");
			writer.write("</tr>");
			writer.write("<tr class='content'>");

			if (browser.equalsIgnoreCase("firefox")) {
				writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
				writer.write("<td class='pass'> " +firefoxpassCounter+ "</td> ");
				writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
				writer.write("<td class='fail'> " + firefoxfailCounter
						+ "</td> ");

			} else if (browser.equalsIgnoreCase("chrome")) {
				writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
				writer.write("<td class='pass'> " +chromepassCounter+ "</td> ");
				writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
				writer.write("<td class='fail'> " + chromefailCounter
						+ "</td> ");

			} else if (browser.equalsIgnoreCase("ie11")) {
				writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
				writer.write("<td class='pass'> " +iepassCounter+ "</td> ");
				writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
				writer.write("<td class='fail'> " + iefailCounter
						+ "</td> ");

			} else if (browser.equalsIgnoreCase("safari")) {
				writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
				writer.write("<td class='pass'> " +safaripassCounter+ "</td> ");
				writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
				writer.write("<td class='fail'> " + safarifailCounter
						+ "</td> ");

			} else if (browser.equalsIgnoreCase("opera")) {
				writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
				writer.write("<td class='pass'> " + operapassCounter+ "</td> ");
				writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
				writer.write("<td class='fail'> " + operafailCounter + "</td> ");

			}

			// Commented below code to remove JDK dependency for Switch - VP@0524
			/* switch(browser.toLowerCase())
	            {
	            case "firefox":writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
	            	           writer.write("<td class='pass'> " +firefoxpassCounter+ "</td> ");
	            	           writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
	            				writer.write("<td class='fail'> " + firefoxfailCounter
	            						+ "</td> ");
	                              break;
	            case "chrome":writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
 	                           writer.write("<td class='pass'> " +chromepassCounter+ "</td> ");
 	                           writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
 				               writer.write("<td class='fail'> " + chromefailCounter
 						     + "</td> ");
                               break;
	            case "ie11":writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
                               writer.write("<td class='pass'> " +iepassCounter+ "</td> ");
                              writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
	                           writer.write("<td class='fail'> " + iefailCounter
			                 + "</td> ");
                                break;
	            case "safari":writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
                              writer.write("<td class='pass'> " +safaripassCounter+ "</td> ");
                              writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
                              writer.write("<td class='fail'> " + safarifailCounter
                              + "</td> ");
                             break;
	            case "ie7":writer.write("<td class='pass'>&nbsp;Tests Passed&nbsp;:</td>");
			                writer.write("<td class='pass'> " + operapassCounter+ "</td> ");
			                writer.write("<td class='fail'>&nbsp;Tests Failed&nbsp;:</td>");
			                writer.write("<td class='fail'> " + operafailCounter + "</td> ");
			               break;
	            }*/


			writer.write("</tr>");
			writer.write("</tfoot>");
			writer.write("</table> ");

			writer.close();
		} catch (Exception e) {

		}
	}

	public void reportStep(String StepDesc) {
		StepDesc = StepDesc.replaceAll(" ", "_");
		File file = new File(TestEngine.filePath() + "/" + strTestName+"_Results"
				+ ".html");// "SummaryReport.html"
		Writer writer = null;

		try {
			writer = new FileWriter(file, true);
			if (BFunctionNo > 0) {
				writer.write("</tbody>");
			}
			writer.write("<tbody>");
			writer.write("<tr class='section'> ");
			writer.write("<td colspan='5' onclick=toggleMenu('" + StepDesc+stepNum+ "')>+ " + StepDesc + "</td>");
			writer.write("</tr> ");
			writer.write("</tbody>");
			writer.write("<tbody id='" + StepDesc+stepNum + "' style='display:table-row-group'>");
			writer.close();
			BFunctionNo = BFunctionNo + 1;
		} catch (Exception e) {

		}
	}
	


	public WebDriver getBrowser()
	{
		if(configProps.getProperty("browserType").equalsIgnoreCase("firefox"))
		{
			 
			
			webDriver = new FirefoxDriver();
			webDriver.get(configProps.getProperty("URL"));
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		else if(configProps.getProperty("browserType").equalsIgnoreCase("Chrome"))
		{
			System.setProperty("webdriver.chrome.driver", configProps.getProperty("Browser_Chorme"));
			webDriver = new ChromeDriver();
			webDriver.get(configProps.getProperty("URL"));
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		else if(configProps.getProperty("browserType").equalsIgnoreCase("IE"))
		{
			DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			cap.setJavascriptEnabled(true);			
			//File file = new File(".//lib//IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", configProps.getProperty("Browser_IE"));
			
			//DesiredCapabilities cap=DesiredCapabilities.internetExplorer();
			 
			// Set ACCEPT_SSL_CERTS  variable to true
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
			/*cap.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
			cap.setCapability(InternetExplorerDriver.IE_SWITCHES, private);*/
			 
			File file = new File("Drivers/IEDriverServer.exe");
			
			
			// Set the driver path
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			 
			// Open browser with capability
			

			webDriver = new InternetExplorerDriver(cap);
			webDriver.get(configProps.getProperty("URL"));
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			try {
				if(webDriver.findElement(By.xpath("//*[@id='overridelink']")).isDisplayed())
					webDriver.findElement(By.xpath("//*[@id='overridelink']")).click();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		report.setDriver(webDriver);
		return webDriver;

	}

	public WebDriver getBrowser(String EXTURL)
	{
		if(configProps.getProperty("browserType").equalsIgnoreCase("firefox"))
		{
			
			webDriver = new FirefoxDriver();
			webDriver.get("ExtURL");
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		else if(configProps.getProperty("browserType").equalsIgnoreCase("Chrome"))
		{
			System.setProperty("webdriver.chrome.driver", configProps.getProperty("Browser_Chorme"));
			webDriver = new ChromeDriver();
			webDriver.get(("ExtURL"));
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		else if(configProps.getProperty("browserType").equalsIgnoreCase("IE"))
		{
			DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			cap.setJavascriptEnabled(true);			
			//File file = new File(".//lib//IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", configProps.getProperty("Browser_IE"));
			
			//DesiredCapabilities cap=DesiredCapabilities.internetExplorer();
			 
			// Set ACCEPT_SSL_CERTS  variable to true
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
			/*cap.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
			cap.setCapability(InternetExplorerDriver.IE_SWITCHES, private);*/
			 
			File file = new File("Drivers/IEDriverServer.exe");
			
			
			// Set the driver path
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			 
			// Open browser with capability
			

			webDriver = new InternetExplorerDriver(cap);
			webDriver.get(("ExtURL"));
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			try {
				if(webDriver.findElement(By.xpath("//*[@id='overridelink']")).isDisplayed())
					webDriver.findElement(By.xpath("//*[@id='overridelink']")).click();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		report.setDriver(webDriver);
		return webDriver;

	}

	public WebDriver getBrowserRC() throws MalformedURLException
	{
		System.setProperty("webdriver.ie.driver", configProps.getProperty("Browser_IE"));
		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
		capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		
		webDriver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"),capability);
		webDriver.get(configProps.getProperty("URL"));
		webDriver.manage().window().maximize();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			if(webDriver.findElement(By.xpath("//*[@id='overridelink']")).isDisplayed())
				webDriver.findElement(By.xpath("//*[@id='overridelink']")).click();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		report.setDriver(webDriver);
		return webDriver;

	}
	
	public static void reportLogger(boolean flag,String msg, String result) throws Exception
	{
		if (flag) {
			report.reportExecutionStatus(webDriver, StepStatus.Pass, msg, result, true);
			System.out.println(StepStatus.Pass + "-" + msg + "-" + result);
		} else {
			report.reportExecutionStatus(webDriver, StepStatus.Fail, msg, result, true);
			System.err.println(StepStatus.Fail + "-" + msg + "-" + result);
		}
	}
	
	public static void reportLogger(String flag,String msg, String result) throws Exception
	{
		if (flag.startsWith("K")) {
			report.reportExecutionStatus(webDriver, StepStatus.KnownBug, msg, result, true);
			System.err.println(StepStatus.KnownBug + "-" + msg + "-" + result);
			
		} else {
			report.reportExecutionStatus(webDriver, StepStatus.Pass, msg, result, true);
			System.out.println(StepStatus.Pass + "-" + msg + "-" + result);
		}
	}

	public static String getClassName(String sTestCase)throws Exception{
		String value = sTestCase;
		try{
			int posi = value.indexOf("@");
			value = value.substring(0, posi);
			posi = value.lastIndexOf(".");	
			value = value.substring(posi + 1);
			return value;
		}catch (Exception e){

		}
		return value;
	}

	public static String getTestDataPath()
	{
		return configProps.getProperty("TestData");
	}
	
	public static String getSheetName()
	{
		return configProps.getProperty("sheetName");
	}
	
	private static final String TASKLIST = "tasklist";
	private static final String KILL = "taskkill /F /IM ";

	public static boolean isProcessRunning(String serviceName) throws Exception {

		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {

			System.out.println(line);
			if (line.contains(serviceName)) {
				return true;
			}
		}
		return false;
	}
	public static void killProcesss(String serviceName) throws Exception 
	{

		Runtime.getRuntime().exec(KILL + serviceName);

	}
	public WebDriver getBrowserParrelal(String browser)
	{
		if(browser.equalsIgnoreCase("firefox"))
		{
			
			webDriver = new FirefoxDriver();
			//webDriver.get(configProps.getProperty("URL"));
			//webDriver.get(configProps.getProperty("ExtURL"));
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		else if(browser.equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", configProps.getProperty("Browser_Chorme"));
			
			webDriver = new ChromeDriver();
			//webDriver.get(configProps.getProperty("URL"));
			//webDriver.get(configProps.getProperty("ExtURL"));
			webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		 
		
		report.setDriver(webDriver);
		return webDriver;

	}

}



