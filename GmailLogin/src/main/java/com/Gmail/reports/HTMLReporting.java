package com.Gmail.reports;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.Gmail.accelerators.TestEngine;
import com.Gmail.Support.HTMLReportSupport;

@SuppressWarnings("all")

public class HTMLReporting 

{

	public static String WorkingDir= System.getProperty("user.dir");

	//public static enum StepStatus {Pass,Fail,Warning};
	private String testCaseName="TestResults";

	public String resultFileFolder= WorkingDir+"/Results/";
	public WebDriver driver=TestEngine.webDriver;
	private String htmlFileName="AutomationTestResults.html";
	//private String resultFilePath="D:/SICAutomation/Results/" + testCaseName + ".log";
	private String resultFilePath;
	private Integer currStepNo=0;
	private String htmlReportSuiteName="GMAIL SMOKE TEST AUTOMATION SUITE ";
	private String testStepName="";
	private String testExpectedResult="";
	private String testActualResult="";
	public static String logoDir;

	//		public enum StepStatus{Pass,Fail,Warning};

	public enum StepStatus{
		Pass ("Pass"),
		Fail ("Fail"),
		Warning ("Warning"),
		KnownBug("KnownBug");
		private final String name;

		private StepStatus(String stepStatus) {
			name = stepStatus;
		}
	}

	BufferedImage screen;

	private class ModuleSummary
	{
		String ModuleName="";
		Integer TotalTCs=0;
		Integer TCPassed=0;
		Integer TCKnownBug=0;
		Integer TCFailed=0;
		Integer TCWarned=0;
		String StartTime="";
		String EndTime="";
		int noOfSteps=0;
		long TotalExecutionInSeconds=0;
		public void init()
		{
			String ModuleName="";
			Integer TotalTCs=0;
			Integer TCPassed=0;
			Integer TCKnownBug=0;
			Integer TCFailed=0;
			String StartTime="";
			String EndTime="";
			long TotalExecutionInSeconds=0;		
		}
	}
	private class TestCaseSummary{
		String ModuleName="";
		String TestCaseName="";
		String ActionName="";
		String Status="";
		Integer StepsPassed=0;
		Integer StepsKnown=0;
		Integer StepsKnownBug=0;
		Integer StepsFailed=0;
		Integer StepsWarned=0;
		String StartTime="";
		String EndTime="";
		public void init()
		{
			String ModuleName="";
			String TestCaseName="";
			String ActionName="";
			String Status="";
			Integer StepsPassed=0;
			Integer StepsKnown=0;
			Integer StepsKnownBug=0;
			Integer StepsFailed=0;
			Integer StepsWarned=0;
			String StartTime="";
			String EndTime="";  		
		}
	}
	private class TestCaseDetails
	{
		String ModuleName="";
		String TestCaseName="";
		String StepNo="";
		String Status="";
		String ExpectedResult="";
		String ActualResult="";
		String StepName="";
		String Description="";
		String StepStartTime="";
		String StepEndTime="";
		String ScreenShot="";
		public void init()
		{
			String ModuleName="";
			String TestCaseName="";
			Integer StepNo=0;
			String Status="";
			String StepName="";
			String Description="";
			String StepStartTime="";
			String StepEndTime="";
		}
	}
	TreeMap<String,ModuleSummary> oModuleSummaryMap = new TreeMap<String,  ModuleSummary>();
	TreeMap<String, TestCaseSummary> oTestCasesSummaryMap = new TreeMap<String,  TestCaseSummary>();
	TreeMap<String, TestCaseDetails> oTestCaseDetailsMap = new TreeMap<String,  TestCaseDetails>();


	public void Reporting()
	{

	}
	/*----	public void initialize() throws Exception {


			//browser.launch();
			this.setTestCaseName("AP_TestCaseNameNew");
			//this.setStepName("Step 1: Login");

			this.reportExecutionStatus(StepStatus.Pass, "Step 1: Login to the application","Successfully Logged into the application", true);		
			this.reportExecutionStatus(StepStatus.Pass, "Clicked successfully", true);		
			terminateReport();
			System.out.println();
		} */

	public void stepReset()
	{
		currStepNo = 0;
	}

	public void reset() {
		currStepNo = 0;
		File resultDir = new File(resultFileFolder);
		for (File file : resultDir.listFiles()) {
			//deleteFile(file);
			//purgeDirectory(file.toPath());
		}
	}
	
	void purgeDirectory(Path dir) {
		try {
		    Files.delete(dir);
		} catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", dir);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", dir);
		} catch (IOException x) {
		    // File permission problems are caught here.
		    System.err.println(x);
		}
	}

	private boolean deleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return true;
		} else if (file.isDirectory()) {
			for (File fileInside : file.listFiles()) {
				return deleteFile(fileInside);
			}
			
			file.delete();
			return true;
		}
		else
			return false;
		
	}

	public void setDriver(WebDriver driverCurrent) {
		driver = driverCurrent;
	}
	public void setStepName(String strStepName) throws Exception
	{
		testStepName=strStepName;
	}
	public void setExpectedResult(String strExpectedResult) throws Exception
	{
		testExpectedResult=strExpectedResult;
	}
	public void setActualResult(String strActualResult) throws Exception
	{
		testActualResult=strActualResult;
	}
	public void setTestCaseName(String currTestCaseName)throws Exception 
	{
		testCaseName = currTestCaseName;
		resultFilePath = resultFileFolder + "/" + testCaseName + ".log";//Appending File to Results Folder Path
		reportExecutionStatus(driver,StepStatus.Pass, "", "", "", "", false);
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws Exception {

		initializeHtmlReport();
		//reportExecutionStatus(StepStatus.Pass, "", false);

		/*
			ResultSet rs = getExcelResultSet("D:/CanDelete/Results/TestCaseName.log", "TestCaseName", " Status='Pass'",true);
			while( rs.next())
			{
				//String newstring = new SimpleDateFormat("yyyy-MMM-dd").format(rs.getDate("Date1"));

				String strEmpName = rs.getString("Status");
				System.out.print(strEmpName);

			}		
		 */
		/*
			this.setTestCaseName("AP_TestCaseNameNew");
			//this.setTestCaseName("TestCaseName");
			reportExecutionStatus(StepStatus.Fail, "Step Name", "Testing", false);
			screenCapture("d:/test.jpg");
			System.out.print("testing");
			this.reportExecutionStatus(StepStatus.Fail,"Step 1: Login", "Login Successfull", true);
			this.reportExecutionStatus(StepStatus.Pass, "Step 2: Click Link","Clicked Link", true);		
		 */
		System.out.print("Report Created");
	}

	public void finish() throws Exception {
		//this.reportExecutionStatus(StepStatus.Pass, "","", false);
	}
	/*
	'###################################################################################################################
	'# Procedure:initializeHtmlReport(ByVal strTxtFilePath,ByVal strHtmlFilePath)
	'# Procedure is used to Generate InitializeHtml Report
	'#
	'# Parameters:
	'# Input Parameters:
	'# strTxtFilePath-Path of the Text file
	'# strHtmlFilePath - Path of the HTML file
	'#
	'# OutPut Parameters: N/A
	'#
	'# Remarks:N/A
	'#
	'# Usage:
	'# The usage of this Procedure is
	'# > Call lpInitializeHtmlReport(strTxtFilePath,strHtmlFilePath)
	'#####################################################################################################################
	 */
	public void initializeHtmlReport()throws Exception//ByVal strTxtFilePath, ByVal strHtmlFilePath
	{//String strTxtFilePath, String strHtmlFilePath

		try
		{

			HTMLReportSupport.copyLogos();
			logoDir = logoDir.replace("\\.\\", "\\");
			System.out.println("Logo Dir---->"+logoDir);
			//Generates Required recordsets using TreeMaps
			generateReportRecordSets();

			//Deleting existing html and creating new file
			File objHtmlFile = new File(resultFileFolder + htmlFileName);
			if(objHtmlFile.exists())
			{
				objHtmlFile.delete();
				objHtmlFile.createNewFile();
			}

			//Writing content to file
			FileWriter currHTMLFileWriter = new FileWriter(objHtmlFile.getAbsoluteFile(),true);
			BufferedWriter htmlbufferWrite = new BufferedWriter(currHTMLFileWriter);

			//Generate CSS
			this.generateCSS(htmlbufferWrite);

			//Generate Html Header
			this.generateHtmlHeader(htmlbufferWrite);


			//Build Record set reading from Txt Log file
			//Call generateReportRecordSets(strTxtFilePath, rsModuleSummary, rsTestCaseSummary, rsTestCaseDetails)

			//Generate the Module Summay
			this.generateReportModuleSummary(htmlbufferWrite);

			//Generate the TestCase  Summary
			this.generateReportTestCaseSummary(htmlbufferWrite);

			//Generate the TestCase Details
			this.generateReportTestCaseDetails(htmlbufferWrite);

			//Generate HTML Footer
			this.generateReportHTMLFooter(htmlbufferWrite);


			htmlbufferWrite.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally
		{

		}
	}

	/*
		'###################################################################################################################
		'# Procedure: generateCSS
		'# Procedure is used to initialize header style in HTML Report
		'#
		'# Parameters:
		'# Input Parameters:
		'# objHtmlFile - HtmlFileName object
		'#
		'# OutPut Parameters: N/A
		'#
		'# Remarks:N/A
		'#
		'# Usage:
		'# The usage of this Procedure is
		'# > Call generateCSS(objHtmlFile)
		'#####################################################################################################################
		Code Updated for color code - rohit
	 */
	private void generateCSS(BufferedWriter htmlbufferWrite) throws Exception
	{
		//htmlReportSuiteName
		writeLineToFile(htmlbufferWrite, "<html>\n");
		writeLineToFile(htmlbufferWrite, "<title>" + htmlReportSuiteName + " Automation Execution Summary Report </title>\n");
		writeLineToFile(htmlbufferWrite, "<head>\n");
		writeLineToFile(htmlbufferWrite, "<img src=\""+logoDir+"\\Standard.jpg\" style=width:125px;height:81px;>");
		writeLineToFile(htmlbufferWrite, "<style>.MainHead { font-family: Arial; font-size: 12pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow; color: white; background:#003399;}</style>"); //#003399 #990000

		writeLineToFile(htmlbufferWrite, "<style> .SubHead { font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow; color: #black; background:white;}</style>\n");
		writeLineToFile(htmlbufferWrite, "<style> .ColumnHead {font-family: Arial; font-size: 10pt ; text-align: center; border-bottom: solid lightyellow 1.0pt; border-top: solid black 1.5pt; color: black; background:#D6D6C2;}</style>");
		writeLineToFile(htmlbufferWrite, "<style> .FailStatus { font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow; color: Red; background:#FFFFFF;}</style>");
		writeLineToFile(htmlbufferWrite, "<style> .PassStatus { font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow;color: green; background:#FFFFFF;} </style>");
		writeLineToFile(htmlbufferWrite, "<style> .KnownBugStatus { font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow;color: #DF7401; background:#FFFFFF;} </style>");
		writeLineToFile(htmlbufferWrite, "<style> .GeneralHead { font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow;"
				+ " color: black; background:#FFFFFF;} </style>");//
		writeLineToFile(htmlbufferWrite, "<style> .RequirementsHead { font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow;"
				+ "color: white; background:#0B47BF;} </style>"); //CC3300
		writeLineToFile(htmlbufferWrite, "<style> .TestSummaryHead { font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow; "
				+ "color: white; background:#5176BF;} </style>"); //CC6600
		writeLineToFile(htmlbufferWrite, "<style> .TestDetailsHead {font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow;"
				+ "color: white; background:#93B5F7;} </style>"); //FF9900
		writeLineToFile(htmlbufferWrite, "<style> .WarnedStatus { font-family: Arial; font-size: 10pt ; border-bottom: thin groove lightyellow; border-top: thin groove lightyellow;color: #7030A0; background:#FFFFFF;} </style>");

		writeLineToFile(htmlbufferWrite, "</head>");
		writeLineToFile(htmlbufferWrite, "<body>"); 

	}   

	/*
		'###################################################################################################################
		'# Procedure: generateReportModuleSummary
		'# Procedure is used to Generate Module Summary
		'#
		'# Parameters:
		'# Input Parameters:
		'# objFile - HTMLFileObject
		'# rsReqSum - Record Set Module Summary
		'#
		'# OutPut Parameters: N/A
		'#
		'# Remarks:N/A
		'#
		'# Usage:
		'# The usage of this Procedure is
		'# Call generateReportModuleSummary(objFile,rsReqSum)
		'#####################################################################################################################
	 */
	private void generateReportModuleSummary(BufferedWriter htmlbufferWrite) throws Exception
	{
		Integer TotalTCCount=0;
		Integer TotalPASSTCCount=0;
		Integer TotalKnownBugCCount=0;
		Integer TotalFAILTCCount=0;
		Integer TotalWARNTCCount=0;
		long TotalExecutionTime=0;
		long lngDays=0;
		long lngHours=0;
		long lngMinutes=0;
		long lngSeconds=0;
		long lngTotalTime=0;
		long lngCurrExecutionTime=0;

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		//Table for Module Test Execution summary
		writeLineToFile(htmlbufferWrite, "<table border='1' cellpadding='3' cellspacing='0' width='100%'>");
		writeLineToFile(htmlbufferWrite, "<tr>");
		writeLineToFile(htmlbufferWrite, "<td align=center Class=RequirementsHead><font face=Arial size='3'><b><u><a name = ISST_highLevel>Overall Test Execution Summary</a></u></b></font></td>");
		writeLineToFile(htmlbufferWrite, "</tr>");
		writeLineToFile(htmlbufferWrite, "</table>");

		writeLineToFile(htmlbufferWrite, "<table border='2' cellpadding='0' cellspacing='0' width='100%'>");
		writeLineToFile(htmlbufferWrite, "<tr>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>Module Name</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>#Tests</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>#No of Steps</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>#Passed</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>#Failed</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>#Scripts Failed</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>#Warned</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>Start Time</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>End Time</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>Total Execution Time</b></td>");
		writeLineToFile(htmlbufferWrite, "</tr>");

		for(Map.Entry<String, ModuleSummary> currObjModuleSummaryMap: oModuleSummaryMap.entrySet())
		{
			//Get Test Current Module Summary Row
			ModuleSummary currModuleSummary = currObjModuleSummaryMap.getValue();  
			//System.out.println(newTestCaseSummary.ModuleName + "\t" + newTestCaseSummary.TestCaseName + "\t" + "ActionName" + "\t" + newTestCaseSummary.Status + "\t" + newTestCaseSummary.StepsPassed + "\t" + newTestCaseSummary.StepsKnownBug + "\t" + newTestCaseSummary.StepsFailed + "\t" + newTestCaseSummary.StepsWarned + "\t" + newTestCaseSummary.StartTime + "\t" + newTestCaseSummary.EndTime);

			writeLineToFile(htmlbufferWrite, "<tr>");
			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead><a name=" + "_rq href=#" + currModuleSummary.ModuleName + ">" + currModuleSummary.ModuleName + "</a> </td>");
			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead align=center>" + currModuleSummary.TotalTCs + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead align=center>" + currModuleSummary.noOfSteps + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=PassStatus align=center>" + currModuleSummary.TCPassed + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=KnownBugStatus align=center>" + currModuleSummary.TCKnownBug + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=FailStatus align=center>" + currModuleSummary.TCFailed + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=WarnedStatus align=center>" + currModuleSummary.TCWarned + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead align=left>" + currModuleSummary.StartTime + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead align=left>" + currModuleSummary.EndTime + "</td>");
			/*
		        lngDays = (((currModuleSummary.TotalExecutionInSeconds / 60) / 60) / 24);
		        lngHours = (((((currModuleSummary.TotalExecutionInSeconds / 60) / 60) / 24) - lngDays) * 24);
		        lngMinutes = ((((((currModuleSummary.TotalExecutionInSeconds / 60) / 60) / 24) - lngDays) * 24 - lngHours) * 60);
		        lngSeconds = ((((((((currModuleSummary.TotalExecutionInSeconds / 60) / 60) / 24) - lngDays) * 24 - lngHours) * 60) - lngMinutes) * 60);
			 */
			lngCurrExecutionTime= currModuleSummary.TotalExecutionInSeconds * 1000;
			long elapsedDays = lngCurrExecutionTime / daysInMilli;
			lngCurrExecutionTime = lngCurrExecutionTime % daysInMilli;

			long elapsedHours = lngCurrExecutionTime / hoursInMilli;
			lngCurrExecutionTime = lngCurrExecutionTime % hoursInMilli;

			long elapsedMinutes = lngCurrExecutionTime / minutesInMilli;
			lngCurrExecutionTime = lngCurrExecutionTime % minutesInMilli;

			long elapsedSeconds = lngCurrExecutionTime / secondsInMilli;

			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead align=left>" + elapsedDays + "days:" + elapsedHours + "hh:" + elapsedMinutes + "min:" + elapsedSeconds + "secs" + "</td>");

			writeLineToFile(htmlbufferWrite, "</tr>");
			TotalTCCount = TotalTCCount + currModuleSummary.TotalTCs;
			TotalPASSTCCount = TotalPASSTCCount + currModuleSummary.TCPassed;
			TotalKnownBugCCount = TotalKnownBugCCount + currModuleSummary.TCKnownBug;
			TotalFAILTCCount = TotalFAILTCCount + currModuleSummary.TCFailed;
			TotalWARNTCCount = TotalWARNTCCount + currModuleSummary.TCWarned;
			lngTotalTime = lngTotalTime + currModuleSummary.TotalExecutionInSeconds;

		}
		//Total

		/*
		    lngDays = (((lngTotalTime / 60) / 60) / 24);
		    lngHours = (((((lngTotalTime / 60) / 60) / 24) - lngDays) * 24);
		    lngMinutes = ((((((lngTotalTime / 60) / 60) / 24) - lngDays) * 24 - lngHours) * 60);
		    lngSeconds = ((((((((lngTotalTime / 60) / 60) / 24) - lngDays) * 24 - lngHours) * 60) - lngMinutes) * 60);
		 */
		lngTotalTime= lngTotalTime * 1000;
		long elapsedDays = lngTotalTime / daysInMilli;
		lngTotalTime = lngTotalTime % daysInMilli;

		long elapsedHours = lngTotalTime / hoursInMilli;
		lngTotalTime = lngTotalTime % hoursInMilli;

		long elapsedMinutes = lngTotalTime / minutesInMilli;
		lngTotalTime = lngTotalTime % minutesInMilli;

		long elapsedSeconds = lngTotalTime / secondsInMilli;

		System.out.println("");
		System.out.printf(
				"Total Modules Execution Time: %d days, %d hours, %d minutes, %d seconds%n", 
				elapsedDays,
				elapsedHours, elapsedMinutes, elapsedSeconds);

		writeLineToFile(htmlbufferWrite, "<tr>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>Total</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>" + TotalTCCount.toString() + "</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>" + "" + "</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>" + TotalPASSTCCount.toString() + "</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>" + TotalKnownBugCCount.toString() + "</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>" + TotalFAILTCCount.toString() + "</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>" + TotalWARNTCCount.toString() + "</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>-" + "</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>-" + "</b></td>");
		writeLineToFile(htmlbufferWrite, "<td class=ColumnHead><b>" + elapsedDays + "days:" + elapsedHours + "hh:" + elapsedMinutes + "min:" + elapsedSeconds + "secs" + "</b></td>");
		writeLineToFile(htmlbufferWrite, "</tr>");
		writeLineToFile(htmlbufferWrite, "</table>");
	}
	/*	'###################################################################################################################
		'# Procedure: generateReportTestCaseSummary
		'# This Procedure is used to Generate TestCase Summary
		'#
		'# Parameters:
		'# Input Parameters:
		'# objFile - HTMLFileObject
		'# rsTestSum- Record Set TestCase Summary
		'#
		'# OutPut Parameters: N/A
		'#
		'# Remarks:N/A
		'#
		'# Usage:
		'# The usage of this Procedure is
		'# > Call generateReportTestCaseSummary(objFile,rsReqSum)
		'#####################################################################################################################
	 */
	public void generateReportTestCaseSummary(BufferedWriter htmlbufferWrite) throws Exception
	{

		String strPrevReq =""; //String variable to hold the previous requirement name

		//Table for TestCase Summary
		writeLineToFile(htmlbufferWrite, "<br><table border='1' cellpadding='3' cellspacing='0' width='100%'>");
		writeLineToFile(htmlbufferWrite, "<tr>");
		writeLineToFile(htmlbufferWrite, "<td align=center Class=TestSummaryHead><font face=Arial size='3'><b><u>TestCase Execution Summary</u></b></font></td>");
		writeLineToFile(htmlbufferWrite, "</tr>");
		writeLineToFile(htmlbufferWrite, "</table>");

		strPrevReq = "";
		//Retrive dta from RecordSet TestCase Summary
		for(Map.Entry<String, TestCaseSummary> currObjTestCasesSummaryMap: oTestCasesSummaryMap.entrySet())
		{
			//Get Test Current Test Case Summary Row
			TestCaseSummary currTestCaseSummary = currObjTestCasesSummaryMap.getValue();	       
			if(!strPrevReq.contentEquals(currTestCaseSummary.ModuleName))
			{
				if(strPrevReq.length() > 0)
				{
					writeLineToFile(htmlbufferWrite, "</table><br>");
				}
				strPrevReq = currTestCaseSummary.ModuleName;
				//rsTestCaseSummary.Filter = "ModuleName = '" & strPrevReq & "'"
				writeLineToFile(htmlbufferWrite, "<table border='1' cellpadding='0' cellspacing='0' width='100%'>");
				writeLineToFile(htmlbufferWrite, "<tr>");
				writeLineToFile(htmlbufferWrite, "<td align=left Class=TestSummaryHead><b>Module: <a name=" +
						strPrevReq + ">" + strPrevReq + "</a> Execution Summary</td>");
				writeLineToFile(htmlbufferWrite, "<td align=right Class=TestSummaryHead><b>GoTo</b>&nbsp;&nbsp;" +
						"<a href=#ISST_highLevel style='color: #C3C3C3'>Overall Summary</a></td>");
				writeLineToFile(htmlbufferWrite, "</tr>");
				writeLineToFile(htmlbufferWrite, "</table>");

				writeLineToFile(htmlbufferWrite, "<table border='2' cellpadding='0' cellspacing='0' width='100%'>");
				writeLineToFile(htmlbufferWrite, "<tr>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='36%' align='left'><b>TestCase Name</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='6%' align='left'><b>Status</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='6%'><b>#Passed</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='6%'><b>#KnownBug</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='6%'><b>#Failed</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='6%'><b>#Warned</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='17%'><b>Start Time</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='17%'><b>End Time</b></td>");
				writeLineToFile(htmlbufferWrite, "</tr>");
			}

			writeLineToFile(htmlbufferWrite, "<tr>");
			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead><a href='#" + currTestCaseSummary.TestCaseName + "_detail' title ='" + currTestCaseSummary.TestCaseName + "'>" + currTestCaseSummary.TestCaseName + "</a>  </td>");

			if(currTestCaseSummary.Status.startsWith("P")) //PASS
			{
				writeLineToFile(htmlbufferWrite, "<td Class=PassStatus align=center>" + currTestCaseSummary.Status + "</td>");
			}
			else if (currTestCaseSummary.Status.startsWith("K"))//KNOWNBUG
			{
				writeLineToFile(htmlbufferWrite, "<td Class=KnownBugStatus align=center>" + currTestCaseSummary.Status + "</td>");
			}
			else if (currTestCaseSummary.Status.startsWith("F"))//FAIL
			{
				writeLineToFile(htmlbufferWrite, "<td Class=FailStatus align=center>" + currTestCaseSummary.Status + "</td>");
			}
			else if (currTestCaseSummary.Status.startsWith("W"))//"WARNED"
			{
				writeLineToFile(htmlbufferWrite, "<td Class=WarnedStatus align=center>" + currTestCaseSummary.Status + "</td>");
			}	            

			writeLineToFile(htmlbufferWrite, "<td Class=PassStatus align=center>" + currTestCaseSummary.StepsPassed + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=KnownBugStatus align=center>" + currTestCaseSummary.StepsKnownBug + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=FailStatus align=center>" + currTestCaseSummary.StepsFailed + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=WarnedStatus align=center>" + currTestCaseSummary.StepsWarned + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead align=left>" + currTestCaseSummary.StartTime + "</td>");
			writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead align=left>" + currTestCaseSummary.EndTime + "</td>");
			writeLineToFile(htmlbufferWrite, "</tr>");
		}//End of For Loop

		writeLineToFile(htmlbufferWrite, "</table>");

	}
	/*
		'###################################################################################################################
		'# Procedure: lpGenerateReportTestCaseDetails
		'# Procedure is used to Generate TestDetails
		'#
		'# Parameters:
		'# Input Parameters:
		'# objHtmlFile - HtmlFileObject
		'# rsTestDetails - RecordSet TestDetails
		'#
		'# OutPut Parameters: N/A
		'#
		'# Remarks:N/A
		'#
		'# Usage:
		'# The usage of this Procedure is
		'# > Call lpGenerateReportTestCaseDetails(objFile,rsTestDetails)
		'#####################################################################################################################
	 */
	public void generateReportTestCaseDetails(BufferedWriter htmlbufferWrite) throws Exception
	{

		String strPrevScript = ""; //String variable to hold the previous script name
		String strRowStatus = ""; //String variable to represent the row pattern to be appended
		String strRequirementName =""; //'String variable to hold the Requirement Name
		String arrValue = "";
		String [] arrDescription;
		String [] arrPath;
		Integer intCnt=0;
		String strPath="";

		//Generate Individual TestCase  Execution Summary Table
		writeLineToFile(htmlbufferWrite, "<br><table border='1' cellpadding='3' cellspacing='0' width='100%' style='table {table-layout:fixed} td {word-wrap: break-word;}'>");
		writeLineToFile(htmlbufferWrite, "<tr>");
		writeLineToFile(htmlbufferWrite, "<td align=center Class=TestDetailsHead><font face=Arial size='3'><b><u>Individual TestCase Execution Details</u></b></font></td>");
		writeLineToFile(htmlbufferWrite, "</tr>");
		writeLineToFile(htmlbufferWrite, "</table>");

		strPrevScript = "";
		for(Map.Entry<String, TestCaseDetails> currObjTestDetailsMap: oTestCaseDetailsMap.entrySet())
		{
			//Get Test Current Test Details Row
			TestCaseDetails currTestCaseDetails = currObjTestDetailsMap.getValue();	    
			strRowStatus = "GeneralHead";
			if(! currTestCaseDetails.TestCaseName.contentEquals(strPrevScript))//Current Script Not Equal to Previous Script Name
			{		        	
				if(strPrevScript.length() > 0 )
				{
					writeLineToFile(htmlbufferWrite, "</table><br>");	                
				}
				strPrevScript = currTestCaseDetails.TestCaseName;
				strRequirementName = strPrevScript.substring(0,2);
				writeLineToFile(htmlbufferWrite, "<table border='0' cellpadding='0' cellspacing='0' width='100%'>");
				writeLineToFile(htmlbufferWrite, "<tr>");
				writeLineToFile(htmlbufferWrite, "<td align=left Class=TestDetailsHead><b>Test Case: <a name='" + strPrevScript + "_detail'>" + strPrevScript + "</a> Execution Details</td>");
				writeLineToFile(htmlbufferWrite, "<td align=right Class=TestDetailsHead><b>GoTo</b>&nbsp;&nbsp;" +
						"<a href=#" + currTestCaseDetails.ModuleName + ">Module Summary</a>" +
						" Or&nbsp;&nbsp;" + "<a href=#ISST_highLevel>Overall Summary</a></td>");
				writeLineToFile(htmlbufferWrite, "</tr>");
				writeLineToFile(htmlbufferWrite, "<tr>");
				//objHtmlFile.WriteLine( "<td align=left Class=TestDetailsHead><b>Script-Action: <a name=" & _
				//strPrevScript & ">" & strPrevScript & "</a> Execution Details</td>");
				writeLineToFile(htmlbufferWrite, "</tr>");
				writeLineToFile(htmlbufferWrite, "</table>");

				writeLineToFile(htmlbufferWrite, "<table border='2' cellpadding='0' cellspacing='0' width='100%'>");
				writeLineToFile(htmlbufferWrite, "<tr>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='4%'><b>Step #</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='4%'><b>Status</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='28%'><b>Step Description</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='28%'><b>Expected Result</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='28%'><b>Actual Result</b></td>");
				writeLineToFile(htmlbufferWrite, "<td class=ColumnHead width='8%'><b>Date Time</b></td>");
				writeLineToFile(htmlbufferWrite, "</tr>");
			}
			if(!(currTestCaseDetails.Status.startsWith("P") && currTestCaseDetails.StepName.equals("Completed")))
			{
				if(currTestCaseDetails.Status.startsWith("F"))// = "FAIL" Then
				{
					strRowStatus = "FailStatus";
				}
				else if(currTestCaseDetails.Status.startsWith("K"))// "KNOWNBUG" Then
				{
					strRowStatus = "KnownBugStatus";
				}

				writeLineToFile(htmlbufferWrite, "<tr>");
				writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead align=center>" + currTestCaseDetails.StepNo.toString() + "</td>");

				if(currTestCaseDetails.Status.startsWith("P"))//PASS" Then
				{
					// writeLineToFile(htmlbufferWrite, "<td Class=PassStatus align=center>" + currTestCaseDetails.Status + "</td>");
					writeLineToFile(htmlbufferWrite, "<td Class=PassStatus align=center > <img src=\""+logoDir+"\\passed.ico\"> </td>"); 
				}
				else if(currTestCaseDetails.Status.startsWith("F"))
				{
					writeLineToFile(htmlbufferWrite, "<td Class=FailStatus align=center > <img src=\""+logoDir+"\\failed.ico\"> </td>"); 
				}
				else if(currTestCaseDetails.Status.startsWith("K"))
				{
					writeLineToFile(htmlbufferWrite, "<td Class=FailStatus align=center > <img src=\""+logoDir+"\\warning.ico\"> </td>"); 
				}
				else
				{
					writeLineToFile(htmlbufferWrite, "<td Class=" + strRowStatus + " align=center>" + currTestCaseDetails.Status + "</td>");
				}

				writeLineToFile(htmlbufferWrite, "<td Class=" + strRowStatus + ">" + currTestCaseDetails.StepName + "</td>");		        
				writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead>" + currTestCaseDetails.ExpectedResult + "</td>");

				String [] currScreenShot = currTestCaseDetails.ScreenShot.split("ScreenShots/");
				if(currScreenShot.length>1)// "FAIL" Then
				{		
					writeLineToFile(htmlbufferWrite, "<td Class=" + strRowStatus + ">" + currTestCaseDetails.ActualResult + " Refer ScreenShot " + "<a href='.\\" + "ScreenShots\\" + currScreenShot[1] + "'  TARGET='_new'>  <img src='.\\" + "ScreenShots\\" + currScreenShot[1] + "' alt='Click to Maximize' border='no' height='15' width = '30' /></a> </td>");
				}
				else
				{
					writeLineToFile(htmlbufferWrite, "<td Class=GeneralHead>" + currTestCaseDetails.ActualResult + "</td>");
				}

				writeLineToFile(htmlbufferWrite, "<td Class=" + strRowStatus + ">" + currTestCaseDetails.StepStartTime + "</td>");
				writeLineToFile(htmlbufferWrite, "</tr>");
			}

		}//End of For Loop
		writeLineToFile(htmlbufferWrite, "</table>");
	}
	/*
		'###################################################################################################################
		'# Procedure: generateReportHTMLFooter
		'# This Procedure used to generate footer in the HTML Report
		'#
		'# Parameters:
		'# Input Parameters:
		'# objHtmlFile - HtmlFile Object
		'#
		'# OutPut Parameters: N/A
		'#
		'# Remarks:N/A
		'#
		'#
		'# Usage:
		'# The usage of this Procedure is
		'# > Call generateReportHTMLFooter(ByRef objHtmlFile)
		'#####################################################################################################################
	 */
	public void generateReportHTMLFooter(BufferedWriter htmlbufferWrite) throws Exception
	{

		Date currNow = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
		String currYear = new SimpleDateFormat("yyyy").format(currNow);

		writeLineToFile(htmlbufferWrite, "<br><table <table border='0' cellpadding='0' cellspacing='0' width='100%'>");
		writeLineToFile(htmlbufferWrite, "<td align=center><font face=arial size='2'>Report Developed by IBM © " + currYear + " </font></td></table><br>");
		writeLineToFile(htmlbufferWrite, "</body>");
		writeLineToFile(htmlbufferWrite, "</html>");

	}

	private void writeLineToFile(BufferedWriter filebufferWrite, String content) throws Exception
	{
		filebufferWrite.write(content);
		filebufferWrite.newLine();
	}
	public void terminateReport()throws Exception
	{
		this.reportExecutionStatus(driver,StepStatus.Pass, "Completed","", "", "", true);
	}
	private void reportExecutionStatus(WebDriver driver,StepStatus status,String stepName, String stepDetails, String stepExpected, String stepActual, Boolean screenShot)throws Exception
	{

		if(status.equals(StepStatus.Pass))
		{
			System.out.print("Pass");
		}else if(status.equals(StepStatus.Fail))
		{
			System.out.print("Fail");
		}else if(status.equals(StepStatus.Warning))
		{
			System.out.print("Warning");
		}else if(status.equals(StepStatus.KnownBug))
		{
			System.out.print(StepStatus.KnownBug);
		}
		if(stepName.isEmpty() || stepName.equalsIgnoreCase("") || stepName.equals(null))
		{
			stepName=testStepName;
		}
		generateTxtReport(status,stepName,stepDetails, testExpectedResult,testActualResult,screenShot,driver);
		currStepNo=currStepNo+1;//Incrementing Current Step # to +1
	}

	public void reportExecutionStatus(WebDriver driver,StepStatus status, String stepDetails, String result, Boolean screenShot)throws Exception
	{	
		testActualResult=result;
		reportExecutionStatus(driver,status,testStepName,stepDetails, testExpectedResult, testActualResult, screenShot);
	}

	private void screenCapture(WebDriver driver,String currImageTimePath) throws Exception
	{
		try{
			/*
				Robot robot = new Robot();
			    Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			    BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
			    ImageIO.write(bufferedImage, "jpg", new File(currImageTimePath));//"d:/test.jpg"));
			    /*/
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(currImageTimePath));
			//*/
		}
		catch(Exception e) 
		{
			System.err.println("Error : " + e.getMessage());
		}

	}

	public void generateTxtReport(StepStatus status,String stepName, String stepDetails, String stepExpected, String stepActual, Boolean screenShot,WebDriver driver)throws Exception
	{
		try{
			File currFile = new File(resultFilePath);			
			if(currStepNo==0)
			{
				if(currFile.exists()) currFile.delete();
				currFile.createNewFile();
			}

			File dirPass = null,dirFail=null;
			dirPass = new File(resultFileFolder + "ScreenShots\\Pass");
			dirFail = new File(resultFileFolder + "ScreenShots\\Fail");
			if (!dirPass.exists())
			{
				dirPass.mkdirs();
			}
			if (!dirFail.exists())
			{
				dirFail.mkdirs();
			}

			//Writing content to file
			FileWriter currFileWriter = new FileWriter(currFile.getAbsoluteFile(),true);
			BufferedWriter bufferWrite = new BufferedWriter(currFileWriter);

			if(currStepNo==0)//Write Header Line
			{
				bufferWrite.write("SlNo" + "\t" + "Status" + "\t" + "StepName" + "\t" + "StepDesc" + "\t" + "ExpectedResult" + "\t" + "ActualResult" + "\t" + "DateTime" + "\t" + "ScreenShot" + "\n");
			}
			//To get Current Date Time and converting to Expected date time format
			SimpleDateFormat currDateTime = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			Date currDate= new Date();
			String currTime = currDateTime.format(currDate);
			String currImage="";
			if(status.ordinal()==1 || screenShot==true)//If status is fail or ScreenShot=true
			{
				//Creating ScreenShots Folder if not exists
				//Rohit Added folder for Pass and Fail Screenshot


				if(status.equals(StepStatus.Pass))
					currImage = resultFileFolder + "ScreenShots/Pass/Img_" + currTime.replace(" ", "_").replace(":", "_").replace("-", "_") + ".jpg";
				else if(status.equals(StepStatus.Fail) || status.equals(StepStatus.KnownBug))
					currImage = resultFileFolder + "ScreenShots/Fail/Img_" + currTime.replace(" ", "_").replace(":", "_").replace("-", "_") + ".jpg";

				this.screenCapture(driver,currImage);
			}

			String CurrStep = currStepNo + "\t" + status.toString() + "\t" + stepName.replace("\t", "").replace("\n", " ") + "\t" + stepDetails + "\t" + stepExpected + "\t" + stepActual + "\t" + currTime + "\t" + currImage + "\n";
			bufferWrite.write(CurrStep);
			bufferWrite.close();
		}
		catch(Exception e) {
			System.err.println("Error Description: " + e.getMessage());
		}
	}

	private void generateReportRecordSets()throws Exception
	{
		String strHours="0";
		String strMinutes="0";
		String strSeconds="0";

		String strModuleName="Scion";
		String strActionName="";
		String strTestCaseName="";

		String strExpectedResult="";
		String strActualResult="";

		String strDays="";
		String strEndTime="";
		Boolean blnUpdate=false;

		String strFileContent="";

		//For Calculating Module Time
		String strTotalExecutionInSeconds="0";
		String strTestStartTime="";
		String strTestEndTime="";
		String strPrevModuleName="";
		String strPrevTestName="";
		String strPrevStartTime="";

		//ModuleSummary newModuleSummary = new ModuleSummary();


		String strModuleStep = "ModuleName" + "\t" + "TotalTCs" + "\t" + "TCPassed" + "\t" + "TCKnownBug" + "\t" + "TCFailed" + "\t" + "TCWarned" + "\t" + "StartTime" + "\t" + "EndTime" + "\t" + "TotalExecutionInSeconds";

		String strTestCaseSummary = "ModuleName" + "\t" + "TestCaseName" + "\t" + "ActionName" + "\t" + "Status" + "\t" + "StepsPassed" + "\t" + "StepsKnownBug" + "\t" + "StepsFailed" + "\t" + "StepsWarned" + "\t" + "StartTime" + "\t" + "EndTime";

		String strTestCaseDetails = "ModuleName" + "\t" + "TestCaseName" + "\t" + "StepNo" + "\t" + "Status" + "\t" + "StepName" + "\t" + "Description" + "\t" + "ExpectedResult" + "\t" + "ActualResult" + "\t" + "DateTime";

		String strTestName;
		File folder = new File(resultFileFolder);
		File[] listOfFiles = folder.listFiles(); 

		for (int currFileNo = 0; currFileNo < listOfFiles.length; currFileNo++) 
		{	   
			if (listOfFiles[currFileNo].isFile()) 
			{
				strTestName = listOfFiles[currFileNo].getName();
				if (strTestName.endsWith(".log") || strTestName.endsWith(".LOG"))
				{
					//Getting Module Name from Script Name

					strModuleName = strTestName.replace(".log", "");
					strTestCaseName =strTestName;//strTestName.substring(strTestName.indexOf("_", 0)+1);
					strTestCaseName = strTestCaseName.replace(".log", "");

					System.out.println("Module Name:" + strModuleName);
					System.out.println("Test Case Name:" + strTestCaseName);

					//Creating new Test Case Summary
					TestCaseSummary newTestCaseSummary = new TestCaseSummary();
					newTestCaseSummary.ModuleName = strModuleName;
					newTestCaseSummary.TestCaseName = strTestCaseName;  

					//Preparing to read the file	        	 
					BufferedReader inputStream = null;
					inputStream = new BufferedReader(new FileReader(listOfFiles[currFileNo]));
					String currLine="";
					strTestStartTime="";
					strTestEndTime="";
					Integer currLineNo = 0;
					System.out.println(strTestCaseDetails);

					//Read each line
					while ((currLine = inputStream.readLine()) != null) 
					{
						boolean scriptAborted=true;//To Verify if script aborted

						String [] arrLine=currLine.split("\t");	//Split Current Line and add it to array
						if(currLineNo==0 || currLineNo==1)
						{
							if(currLineNo==1)//copying start date time
							{
								strTestStartTime=arrLine[6];
								strPrevStartTime=strTestStartTime;
								newTestCaseSummary.StartTime=strTestStartTime;
							}//End if for Currline =1
						}//End If for Currline = 0 or 1
						else if(arrLine[2].length()==0 && arrLine[6].length()!=0)//End Time
						{
							strEndTime=arrLine[6];
							newTestCaseSummary.EndTime = arrLine[6];
						}
						else
						{	        		 
							TestCaseDetails newTestCaseDetails = new TestCaseDetails();
							newTestCaseDetails.ModuleName = strModuleName;
							newTestCaseDetails.TestCaseName = strTestCaseName;
							newTestCaseDetails.StepNo = arrLine[0];
							newTestCaseDetails.Status = arrLine[1];
							newTestCaseDetails.StepName = arrLine[2];
							newTestCaseDetails.Description= arrLine[3];
							newTestCaseDetails.ExpectedResult = arrLine[4];
							newTestCaseDetails.ActualResult = arrLine[5];
							newTestCaseDetails.StepStartTime = strPrevStartTime;
							newTestCaseDetails.StepEndTime = arrLine[6];
							if(arrLine.length > 7){
								newTestCaseDetails.ScreenShot = arrLine[7];
							}else{
								newTestCaseDetails.ScreenShot = "";
							}
							strPrevStartTime=arrLine[6];

							//Update Status count in TestCase Summary
							if (newTestCaseDetails.Status.startsWith("P")) //PASS
							{			      
								if(! newTestCaseDetails.StepName.equals("Completed"))
								{
									newTestCaseSummary.StepsPassed = newTestCaseSummary.StepsPassed + 1;
								}
							}else if (newTestCaseDetails.Status.startsWith("F"))//Fail
							{
								if (newTestCaseDetails.Description.contains("KNOWNBUG"))
								{
									newTestCaseSummary.StepsKnownBug = newTestCaseSummary.StepsKnownBug+ 1;

									newTestCaseDetails.Status = "KNOWNBUG";
								}
								else
								{
									newTestCaseSummary.StepsFailed = newTestCaseSummary.StepsFailed + 1;
								}
							}
							else if (newTestCaseDetails.Status.contains("K")){
								newTestCaseSummary.StepsKnownBug = newTestCaseSummary.StepsKnownBug+ 1;

								newTestCaseDetails.Status = "KNOWNBUG";

							}
							else if (newTestCaseDetails.Status.contains("W"))//Warning
							{
								newTestCaseSummary.StepsWarned = newTestCaseSummary.StepsWarned + 1;
							}
							newTestCaseSummary.EndTime = arrLine[6];
							strTestEndTime = arrLine[6];
							oTestCaseDetailsMap.put(newTestCaseDetails.TestCaseName+getTwoDigitNumber(newTestCaseDetails.StepNo.toString()), newTestCaseDetails);
							System.out.println(newTestCaseDetails.ModuleName + "\t" + newTestCaseDetails.TestCaseName + "\t" + newTestCaseDetails.StepNo + "\t" + newTestCaseDetails.Status + "\t" + newTestCaseDetails.StepName + "\t" + newTestCaseDetails.Description + "\t" + newTestCaseDetails.StepStartTime + "\t" + newTestCaseDetails.StepEndTime);
						}//end if for not line 0 & 1
						currLineNo=currLineNo+1;
					}//While reading line in current file
					newTestCaseSummary.StartTime=strTestStartTime;
					//Verify if strTestCaseName is not found in oTestCasesSummaryMap
					TestCaseSummary currTestCaseSummary = oTestCasesSummaryMap.get(strTestCaseName);
					if(currTestCaseSummary==null)
					{
						//As currTestCaseSummary is null creating new TestCaseSummary
						oTestCasesSummaryMap.put(strTestCaseName, newTestCaseSummary);
					}
					strTestStartTime = "";
					strTestEndTime = "";	        	
					oTestCasesSummaryMap.put(newTestCaseSummary.TestCaseName, newTestCaseSummary);

					/* DO NOT DELETE BELOW CODE
			         System.out.println("");
			         System.out.println("Test Summary");
			         System.out.println(strTestCaseSummary);
			         System.out.println(newTestCaseSummary.ModuleName + "\t" + newTestCaseSummary.TestCaseName + "\t" + "ActionName" + "\t" + newTestCaseSummary.Status + "\t" + newTestCaseSummary.StepsPassed + "\t" + newTestCaseSummary.StepsKnownBug + "\t" + newTestCaseSummary.StepsFailed + "\t" + newTestCaseSummary.StepsWarned + "\t" + newTestCaseSummary.StartTime + "\t" + newTestCaseSummary.EndTime);
					 */

				}//End of if new test case
			}//End of if test case or not
		}//End of files for loop


		/*DO NOT DELETE - Useful for debugging
		    System.out.println("");
	        System.out.println("Test Summary");
	        System.out.println(strTestCaseSummary);
		    for(Map.Entry<String, TestCaseSummary> currObjTestCasesSummaryMap: oTestCasesSummaryMap.entrySet())
		    {
		    	//Get Test Current Test Case Summary Row
		    	TestCaseSummary newTestCaseSummary = currObjTestCasesSummaryMap.getValue();	    	
		        System.out.println(newTestCaseSummary.ModuleName + "\t" + newTestCaseSummary.TestCaseName + "\t" + "ActionName" + "\t" + newTestCaseSummary.Status + "\t" + newTestCaseSummary.StepsPassed + "\t" + newTestCaseSummary.StepsKnownBug + "\t" + newTestCaseSummary.StepsFailed + "\t" + newTestCaseSummary.StepsWarned + "\t" + newTestCaseSummary.StartTime + "\t" + newTestCaseSummary.EndTime);
		    }
		 */

		//Generate TestCase Summary & Update Test Module Status
		for(Map.Entry<String, TestCaseSummary> currObjTestCasesSummaryMap: oTestCasesSummaryMap.entrySet())
		{
			//Get Test Current Test Case Summary Row
			TestCaseSummary currTestCaseSummary = currObjTestCasesSummaryMap.getValue();

			//Get Required Module Row
			ModuleSummary currModuleSummary = oModuleSummaryMap.get(currTestCaseSummary.ModuleName);
			if(currModuleSummary==null)
			{
				currModuleSummary = new ModuleSummary();
				currModuleSummary.ModuleName = currTestCaseSummary.ModuleName;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			Date startDate=null,endDate=null;
			//if(!currTestCaseSummary.StartTime.isEmpty())
			startDate = formatter.parse(currTestCaseSummary.StartTime);
			//	if(!currTestCaseSummary.EndTime.isEmpty())

			long elapsedSeconds = 0;
			if (!currTestCaseSummary.EndTime.trim().isEmpty()) {
				endDate = formatter.parse(currTestCaseSummary.EndTime);
				elapsedSeconds = (endDate.getTime()-startDate.getTime())/1000;
			}
			currModuleSummary.TotalExecutionInSeconds = currModuleSummary.TotalExecutionInSeconds + elapsedSeconds;

			//Updating Test Cases Status 
			if(currTestCaseSummary.StepsFailed > 0)
			{
				currTestCaseSummary.Status="FAIL";
				currModuleSummary.TCFailed =currModuleSummary.TCFailed+1; 
			}
			else if(currTestCaseSummary.StepsKnownBug > 0)
			{
				currTestCaseSummary.Status="KNOWNBUG";
				currModuleSummary.TCKnownBug = currModuleSummary.TCKnownBug + 1;
			}
			else if(currTestCaseSummary.StepsWarned > 0)
			{
				currTestCaseSummary.Status="WARNED";
				currModuleSummary.TCWarned = currModuleSummary.TCWarned + 1;
			}
			else if(currTestCaseSummary.StepsPassed > 0)
			{
				currTestCaseSummary.Status= "PASS";
				currModuleSummary.TCPassed = currModuleSummary.TCPassed + 1;
			}

			currModuleSummary.noOfSteps = currTestCaseSummary.StepsPassed 
					+ currTestCaseSummary.StepsFailed 
					+ currTestCaseSummary.StepsWarned;

			currObjTestCasesSummaryMap.setValue(currTestCaseSummary);
			//Updating back Module Summary in TreeMap
			oModuleSummaryMap.put(currTestCaseSummary.ModuleName, currModuleSummary);
		}//End For Loop Test Summary

		//Generate Module Summary
		for(Map.Entry<String, ModuleSummary> currObjTestModuleSummaryMap: oModuleSummaryMap.entrySet())
		{
			//Getting Module Summary Row
			ModuleSummary currModuleSummary = currObjTestModuleSummaryMap.getValue();
			for(Map.Entry<String, TestCaseSummary> currObjTestCasesSummaryMap: oTestCasesSummaryMap.entrySet())
			{
				//Getting TestCase Summary Row
				TestCaseSummary currTestCaseSummary = currObjTestCasesSummaryMap.getValue();
				if(currTestCaseSummary.ModuleName.contentEquals(currModuleSummary.ModuleName))
				{
					if(currModuleSummary.StartTime.contentEquals(""))
					{
						currModuleSummary.StartTime = currTestCaseSummary.StartTime;
						currModuleSummary.EndTime = currTestCaseSummary.EndTime;
					}
					else
					{
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
						Date TestSummaryStartTime = formatter.parse(currTestCaseSummary.StartTime);
						Date ModuleSummaryStartTime = formatter.parse(currModuleSummary.StartTime);
						long elapsedSeconds = (ModuleSummaryStartTime.getTime()-TestSummaryStartTime.getTime())/1000;
						if(elapsedSeconds>0)
						{
							currModuleSummary.StartTime = formatter.format(TestSummaryStartTime);
						}
						//Calculating Time Elapse in seconds
						Date TestSummaryEndTime = formatter.parse(currTestCaseSummary.EndTime);
						Date ModuleSummaryEndTime = formatter.parse(currModuleSummary.EndTime);
						elapsedSeconds = (ModuleSummaryEndTime.getTime()-TestSummaryEndTime.getTime())/1000;
						if(elapsedSeconds<0)
						{
							currModuleSummary.EndTime = formatter.format(TestSummaryEndTime);
						}
					}
				}
				currModuleSummary.TotalTCs = currModuleSummary.TCPassed + currModuleSummary.TCFailed + currModuleSummary.TCKnownBug;		        	    		
			}
			oModuleSummaryMap.put(currModuleSummary.ModuleName, currModuleSummary);
		}

		/*DO NOT DELETE Useful for debugging 
		    System.out.println("");
	        System.out.println("Test Summary");
	        System.out.println(strTestCaseSummary);
		    for(Map.Entry<String, TestCaseSummary> currObjTestCasesSummaryMap: oTestCasesSummaryMap.entrySet())
		    {
		    	//Get Test Current Test Case Summary Row
		    	TestCaseSummary newTestCaseSummary = currObjTestCasesSummaryMap.getValue();	    	
		        System.out.println(newTestCaseSummary.ModuleName + "\t" + newTestCaseSummary.TestCaseName + "\t" + "ActionName" + "\t" + newTestCaseSummary.Status + "\t" + newTestCaseSummary.StepsPassed + "\t" + newTestCaseSummary.StepsKnownBug + "\t" + newTestCaseSummary.StepsFailed + "\t" + newTestCaseSummary.StepsWarned + "\t" + newTestCaseSummary.StartTime + "\t" + newTestCaseSummary.EndTime);
		    }

		    System.out.println("");
	        System.out.println("Test Module Summary");
	        System.out.println(strModuleStep);
		    for(Map.Entry<String, ModuleSummary> currObjTestModuleSummaryMap: oModuleSummaryMap.entrySet())
		    {
		    	ModuleSummary currModuleSummary = currObjTestModuleSummaryMap.getValue();
		        System.out.println(currModuleSummary.ModuleName + "\t" + currModuleSummary.TotalTCs + "\t" + currModuleSummary.TCPassed + "\t" + currModuleSummary.TCKnownBug + "\t" + currModuleSummary.TCFailed + "\t" + currModuleSummary.TCWarned + "\t" + currModuleSummary.StartTime + "\t" + currModuleSummary.EndTime + "\t" + currModuleSummary.TotalExecutionInSeconds);

		    }
		    DO NOT DELETE*/
	}
	/*
		'###################################################################################################################
		'# Procedure: generateHtmlHeader
		'# Procedure is used to generate the header part in HTML report
		'#
		'# Parameters:
		'# Input Parameters:
		'# objHtmlFile - HtmlFile Object
		'#
		'# OutPut Parameters: N/A
		'#
		'# Remarks:N/A
		'#
		'# Usage:
		'# The usage of this Procedure is
		'# > Call lpGenerateHtmlHeader(objHtmlFile)
		'#####################################################################################################################
	 */
	private void generateHtmlHeader(BufferedWriter htmlbufferWrite) throws Exception
	{
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		Date tdate = new Date();

		// Generate Table  for  Automation Batch Execution Summary
		writeLineToFile(htmlbufferWrite, "<table border='0' cellpadding='5' cellspacing='0' width='100%' >");
		writeLineToFile(htmlbufferWrite, "<tr>");
		writeLineToFile(htmlbufferWrite, "<td align=center Class=MainHead><b>" +
				htmlReportSuiteName + " - Automation Batch Execution Summary" + "</b></td>");;
				writeLineToFile(htmlbufferWrite, "</tr>");
				writeLineToFile(htmlbufferWrite, "</table>");

				//Generate Table  for Report Generated On .*
				writeLineToFile(htmlbufferWrite, "<table border='0' cellpadding='3' cellspacing='0' width='100%'>");
				writeLineToFile(htmlbufferWrite, "<tr>");
				writeLineToFile(htmlbufferWrite, "<td align=center Class=SubHead>Report Generated On:" + dateFormat.format(tdate) + "</td>");
				writeLineToFile(htmlbufferWrite, "</tr>");
				writeLineToFile(htmlbufferWrite, "</table><br>");

	}

	/*
	    '#####################################################################################################################
	    '# Function: getExcelResultSet(String strExcelFileName, String strSheetName, String strWhereClause, Boolean ReadOnly)
	    '# Reads the data from excel and populates a Result set
	    '#
	    '# Parameters:
	    '# Input Parameters:
	    '# strExcelFileName - Excel file name with path or QTP relative path
	    '# strSheetName	- Excel Sheet name
	    '# strWhereClause - Where Clause with or without conditions
	    '# blnReadOnly - True/False (User True when you need to update the ResultSet else use False)
	    '#
	    '# Return Parameter: 
	    '# ResultSet - ResultSet Object is returned
	    '#
	    '# Remarks:
	    '# This Procedure will create a valid sql statement internally
	    '# "Where" clause need not be entered in the Where Condition, Procedure will interally append "Where" to the sql statement
	    '# This Procedure will return an ResultSet and avail the advantages of ResultSet
	    '#
	    '#
	    '# Usage:
	    '# The usage of this function is
	    '#
	    '# Example : Below code will get the recored set in read-only mode
	    '# ResultSet rs = getExcelResultSet("D:/Test.xls", "Sheet1", " EmpID='001'",true);
	    '# While (rs.next())
	    '# { 
	    '# 		System.out.print(rs.getString("EmpName"));
	    '# }
	    '#####################################################################################################################   */

	public ResultSet getExcelResultSet(String strExcelFileName,String strSheetName, String strWhereClause, Boolean ReadOnly) throws SQLException
	{	    	
		String strQuery="";
		ResultSet rs=null;
		java.sql.Statement stmt=null;
		Connection con=null;	

		strQuery = "SELECT * FROM [" + strSheetName + "$]";
		if (strWhereClause.length() > 0)
		{
			strQuery = strQuery + " WHERE " + strWhereClause;
		}
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			con = java.sql.DriverManager.getConnection( "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=" + strExcelFileName + ";readOnly=" + ReadOnly.toString());
			stmt = con.createStatement();
			rs = stmt.executeQuery(strQuery);
		} catch ( Exception e)
		{
			System.err.println("Caught FileNotFoundException: " + e.getMessage());

			if(con!=null)
			{
				con.close();
			}
			if(stmt!=null)
			{
				stmt.close();
			}

		}
		return rs;
	}

	private String getTwoDigitNumber(String number){
		int length = number.length();
		for(int i=0; i<2-length; i++){
			number = "0"+number;
		}
		return number;
	}

	public static void main(String[] args) throws Exception

	{
		HTMLReporting r = new HTMLReporting();
		r.initializeHtmlReport();
		//r.reset();
	}

}


