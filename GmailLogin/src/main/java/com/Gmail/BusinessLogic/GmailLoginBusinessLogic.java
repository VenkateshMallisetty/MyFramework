package com.Gmail.BusinessLogic;

import org.openqa.selenium.WebDriver;

import com.Gmail.accelerators.ActionEngine;

import PageObjects.Gmail.GmailLognPageObjects;

public class GmailLoginBusinessLogic extends ActionEngine
{
	private static final String xpath = "xpath";
	public boolean enterGmailURL(WebDriver driver,String pageTitle) throws Throwable
	{		
		boolean ispagedisplayed = true;	

		//driver.get(URL);
		String title=driver.getTitle();
		
		ispagedisplayed = title.equals(pageTitle);
				
		if (ispagedisplayed)
			SuccessReport("Login successfull","Logged in successfully." );	
		else
		{
			failureReport(driver,"Login Successfull", "Failed to login." );
			ispagedisplayed =  false;
		}
		return ispagedisplayed;
	}
	public void ClickSignInbutton(WebDriver driver)
	{
		try
		{
			click(driver, xpath, GmailLognPageObjects.sSignIn, "Sign in");
		}
		catch (Throwable e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void enterloginCredentials(WebDriver driver,String username,String password)
	{
		try
		{
			sendKeys(driver, xpath, GmailLognPageObjects.SUserName,username);
			clickButton(driver, xpath, GmailLognPageObjects.Snext, "Next");
			sendKeys(driver, xpath, GmailLognPageObjects.Spassword,password);
			click(driver, xpath, GmailLognPageObjects.Ssignin, "Sign in");
			selectCheckbox(driver, xpath, GmailLognPageObjects.SstaySignIn,"ON");
		}
		catch (Throwable e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
