package com.Gmail.accelerators;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

/**
 *  ActionEngine is a wrapper class of Selenium actions
 *  Rajender Cheekoti, Senior Test Architect, IBM GBS India/Philippines
 */
@SuppressWarnings("all")
public class ActionEngine extends TestEngine
{

	public WebDriverWait wait;
	public static boolean execFlag = true;
	String bool = configProps.getProperty("OnSuccessReports");
	public static WebDriver driver = webDriver;
	boolean b = true; // /Boolean.parseBoolean(bool);
	//public HTMLReporting result = new HTMLReporting();

	public static By getLocator(String identifyBy, String locator)
			throws Exception {

		if (identifyBy.equalsIgnoreCase("xpath")) {
			return By.xpath(locator);
		} else if (identifyBy.equalsIgnoreCase("id")) {
			return By.id(locator);
		} else if (identifyBy.equalsIgnoreCase("name")) {
			return By.name(locator);
		} else if (identifyBy.equalsIgnoreCase("linkText")) {
			return By.linkText(locator);
		} else if (identifyBy.equals("partialLinkText")) {
			return By.partialLinkText(locator);
		} else if (identifyBy.equals("cssSelector")) {
			return By.cssSelector(locator);
		} else if (identifyBy.equals("className")) {
			return By.className(locator);
		} else {
			throw new Exception("No such locator: " + locator);
		}

		// Commented below code to remove JDK dependency for Switch - VP@0524
		/*switch (identifyBy) {
		case "xpath":
			return By.xpath(locator);
		case "id":
			return By.id(locator);
		case "name":
			return By.name(locator);
		case "cssSelector":
			return By.cssSelector(locator);
		case "className":
			return By.className(locator);
		case "linkText":
			return By.linkText(locator);
		case "partialLinkText":
			return By.partialLinkText(locator);
		default:
			throw new Exception("No such locator: " + locator);
		}*/
	}

	/**
	 * @FunctionName click
	 * @Description Function to click a button or link,This function takes the
	 *              identifyBy and locator values as parameters and perform
	 *              click action on the locator which is passed as a parameter
	 * @param identifyBy
	 * @param locator
	 * @return void
	 * @throws Throwable
	 * 
	 */
	public static  void clickButton(WebDriver driver,String identifyBy, String locator, String ElementName)
			throws Throwable {

		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy,locator)));
		
	    
		By by = getLocator(identifyBy, locator);
		driver.findElement(by).click();
		SuccessReport("Click on element " + ElementName, "Successfully clicked on " + ElementName);

	}

	public static void click(WebDriver driver, String identifyBy, String locator, String ElementName) throws Throwable {
		try {
			//
			if (isElementPresent(driver, identifyBy, locator)) {
				WebDriverWait wait = new WebDriverWait(driver, 15);
				wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy, locator)));

				
				if (browser.startsWith("IE") || browser.equalsIgnoreCase("chrome")) {
					
					boolean result = false;
					int attempts = 0;
					while (attempts < 2) {
						try {
							WebElement element = driver.findElement(getLocator(identifyBy, locator));
							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("arguments[0].click();", element);
							Thread.sleep(1000);
							break;
						} catch (StaleElementReferenceException e) {
							e.printStackTrace();
						}
						attempts++;
					}

				} else {
					By by = getLocator(identifyBy, locator);
					driver.findElement(by).click();
				}

			}
			// HandleIEException();
			SuccessReport("Click on element " + ElementName, "Successfully clicked on " + ElementName);

		} catch (Exception e) {
			failureReport(driver, "Click Exception Info :",
					"Unable to click the element " + ElementName + " with locator : " + locator);
			logger.error("Unable to click the element with locator : " + locator);

		}
	}
	
	public static  void doubleClick(WebDriver driver,String identifyBy, String locator, String ElementName)
			throws Throwable {
		try {
			if (isElementPresent(driver,identifyBy, locator)) {				
				
				Actions action = new Actions(driver);
				By by = getLocator(identifyBy, locator);
				
				WebElement element=driver.findElement(by);

				//Double click
				action.doubleClick(element).perform();
				

			}
			//HandleIEException();
			SuccessReport("Click on element " + ElementName, "Successfully clicked on " + ElementName);

		} catch (Exception e) {
			failureReport(driver,"Click Exception Info :",
					"Unable to click the element " + ElementName + " with locator : " + locator);
			logger.error("Unable to click the element with locator : "
					+ locator);

		}
	}



	public void HandleIEException(){
		if(browser.equalsIgnoreCase("IE")){

			try{
				Thread.sleep(2000);
				driver.switchTo().alert().accept();
				System.out.println("Clicked on Modal Dialog");
			}catch(Exception e){			
				//logger.error("Unable to select the window :");
			}
		}
	}

	public static  WebElement fluentWaitElementVisibility(WebDriver driver,final By byLocator, int timeout)
			throws Throwable {

		WebElement webElement = null;
		// webElement =
		// getWaitObject().until(ExpectedConditions.visibilityOf(driver.findElement(byLocator)));
		if (threadWaitForElementVisibility(driver,byLocator, timeout)) {
			webElement = driver.findElement(byLocator);
		}
		return webElement;
	}

	public static  boolean threadWaitForElementVisibility(WebDriver driver,By by, int timeout)
			throws Throwable {
		boolean isPresent = false;
		//int timeout = Integer.parseInt(configProps.getProperty("ImplicitWait").trim());
		try {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

			int x = 0;
			do {
				try{
					if (driver.findElement(by).isDisplayed()) {
						highlight(driver.findElement(by));
						isPresent = true;
						break;


					} else {
						Thread.sleep(1000);
						x = x + 1;
						isPresent = false;
					}
				}catch(Exception e){  
					x = x + 1;
					continue;
				}


			} while (x < timeout && isPresent == false);

		} catch (Exception e) {
			System.out.println("driver -->"+driver);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			return isPresent;

		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return isPresent;
	}	/**
	 * @FunctionName isElementPresent
	 * @Description Function to validate the existence of an element,This
	 *              function takes the identifyBy and locator values as
	 *              parameters and validate the locator which is passed as a
	 *              parameter It add a failure report if that location is not
	 *              available
	 * @param identifyBy
	 * @param locator
	 * @return boolean
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */
	public static boolean isElementPresent(WebDriver driver,String identifyBy, String locator)
			throws Throwable {
		boolean isPresent = false;
		
		try {

			WebDriverWait wait = new WebDriverWait(driver, 20);
		    wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy,locator)));

			
			if(driver.findElement(getLocator(identifyBy,locator)).isDisplayed())
			{
				highlight(driver.findElement(getLocator(identifyBy,locator)));
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)",driver.findElement(getLocator(identifyBy,locator)));
				isPresent = true;				
			}
			return isPresent;
		} catch (Exception e) {
			//		failureReport(
			//				"Element Identification Exception Info :",
			//				"Unable to identify the element with locator :"
			//						+ locator);
			//		logger.error("Unable to identify the element with locator : "
			//				+ locator);
			return isPresent;
		}




	}


	/**
	 * @FunctionName verifyElementPresent
	 * @Description Function to validate the existence of an element,This
	 *              function takes the identifyBy and locator values as
	 *              parameters and validate the locator which is passed as a
	 *              parameter
	 * @param identifyBy
	 * @param locator
	 * @return boolean
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public  boolean verifyElementPresent(WebDriver driver, String identifyBy, String locator) {

		Boolean isPresent = false;
		try {

			if (driver.findElement(getLocator(identifyBy, locator))
					.isDisplayed()) {
				highlight(driver.findElement(getLocator(identifyBy, locator)));
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)",
						driver.findElement(getLocator(identifyBy, locator)));
				isPresent = true;
			}

			return isPresent;
		} catch (Exception e) {
			/*logger.error("Unable to identify the element with locator : "
					+ locator);*/
			return isPresent;
		}
	}

	/**
	 * @FunctionName isAlertPresent
	 * @Description Function to validate the existence of a popup,This function
	 *              return true if pop up exist otherwise return false
	 * @return boolean
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */
	public  boolean isAlertPresent() throws Throwable {

		try {
			driver.switchTo().alert();
			return true;

		}

		catch (NoAlertPresentException e) {
			logger.error("No Alert Present");
			return false;
		}

	}

	/**
	 * @FunctionName acceptAlert
	 * @Description Function to accept the popup,This function accept the popup
	 *              if exist
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public  void acceptAlert() throws Throwable {
		try {

			if (isAlertPresent()) {
				driver.switchTo().alert().accept();
			}

		} catch (Exception e) {
			logger.error("No Alert Present");
			return;
		}

	}

	/**
	 * @FunctionName dismissAlert
	 * @Description Function to accept the popup,This function cancel the popup
	 *              if exist
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public  void dismissAlert() throws Throwable {
		try {

			if (isAlertPresent()) {
				driver.switchTo().alert().dismiss();
			}

		} catch (Exception e) {
			logger.error("No Alert Present");
			return;
		}

	}

	/**
	 * @FunctionName type
	 * @Description Function to type in text box,This function takes the
	 *              identifyBy and locator values as parameters and enter some
	 *              value in text box present in the location which is passed as
	 *              a parameter
	 * @param identifyBy
	 * @param locator
	 * @param valuetoType
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public   void type(String identifyBy, String locator,
			String valuetoType, String element) throws Throwable {
		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				By by = getLocator(identifyBy, locator);
				driver.findElement(by).clear();
				driver.findElement(by).sendKeys(valuetoType);
				SuccessReport("Entering Data in "+element+" Field", "Data " + valuetoType
						+ "  entered successfully");
			}

		} catch (Exception e) {
			failureReport(driver,"Entering Data Exception Info :",
					"Unable to enter the data " + valuetoType
					+ " into an element with locator :" + locator);

			logger.error("Unable to enter the data " + valuetoType
					+ " into an element with locator :" + locator);
		}

	}

	/**
	 * @FunctionName sendKeys
	 * @Description Function to append the text in text box,This function takes
	 *              the identifyBy and locator values as parameters and append
	 *              some value in text box present in the location which is
	 *              passed as a parameter
	 * @param identifyBy
	 * @param locator
	 * @param valuetoType
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */
	public static void sendKeys(WebDriver driver,String identifyBy, String locator,String valuetoType) throws Throwable {
		try {
			//result.initializeHtmlReport();

			if (isElementPresent(driver,identifyBy, locator)) {
				driver.findElement(getLocator(identifyBy, locator)).clear();
				driver.findElement(getLocator(identifyBy, locator)).sendKeys(valuetoType);
			}
			flag = true;
			SuccessReport("Append Data", "Data " + valuetoType
					+ "  appended successfully");
		} catch (Exception e) {
			failureReport(driver,"Entering Data Exception Info :",
					"Unable to enter the data " + valuetoType
					+ " into an element with locator :" + locator);
			logger.error("Unable to enter the data " + valuetoType
					+ " into an element with locator :" + locator);
		}

	}

	public static void sendKeys(WebDriver driver,String identifyBy, String locator,Keys key) throws Throwable {
		try {
			//result.initializeHtmlReport();

			if (isElementPresent(driver,identifyBy, locator)) 
			{
				driver.findElement(getLocator(identifyBy, locator)).sendKeys(key);
			}
		}
		catch(Exception e)
		{

		}

	}


	/**
	 * @FunctionName mouseOver
	 * @Description Function to move the mouse on to specific element,This
	 *              function takes the identifyBy and locator values as
	 *              parameters and perform mouse over action on the location
	 *              which is passed as a parameter
	 * @param identifyBy
	 * @param locator
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static void mouseOver(WebDriver driver,String identifyBy, String locator)
			throws Throwable {
		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				Actions action = new Actions(driver);
				WebElement Element = driver.findElement(getLocator(identifyBy,
						locator));
				action.moveToElement(Element).build().perform();
				Thread.sleep(2000);

			}

		} catch (Exception e) {
			failureReport(driver,"Mouse Over To Exception Info :",
					"Unable to moveover on element with locator :" + locator);
			logger.error("Unable to moveover on element with locator :"
					+ locator);
		}

	}

	/**
	 * @FunctionName draggable
	 * @Description Function to drag the mouse,This function takes the
	 *              identifyBy ,locator,X and Y co-ordinate values as parameters
	 *              and drag element present in the specified location to the
	 *              specified co-ordinates position
	 * @param identifyBy
	 * @param locator
	 * @param x
	 * @param y
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public void draggable(String identifyBy, String locator, int x, int y)
			throws Throwable {
		try {
			if (isElementPresent(driver,identifyBy, locator)) {
				Actions action = new Actions(driver);
				WebElement Element = driver.findElement(getLocator(identifyBy,
						locator));
				action.dragAndDropBy(Element, x, y).build().perform();
			}

		} catch (Exception e) {
			failureReport(driver,"draggable Exception Info :",
					"Unable to drag the element with locator :" + locator);
			logger.error("Unable to drag the element with locator :" + locator);
		}

	}

	/**
	 * @FunctionName draganddrop
	 * @Description Function to drag the mouse,This function takes the
	 *              srcIdentify ,srcLocation,dstIdentify and dstLocation values
	 *              as parameters and drag element present in the specified
	 *              srcLocation to the specified dstLocation
	 * @param srcIdentify
	 * @param srcLocation
	 * @param dstIdentify
	 * @param dstLocation
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public  void draganddrop(String srcIdentify, String srcLocation,
			String dstIdentify, String dstLocation) throws Throwable {
		try {
			WebElement from = null;
			WebElement to = null;

			if (isElementPresent(driver,srcIdentify, srcLocation)) {
				from = driver.findElement(getLocator(srcIdentify, srcLocation));
			}

			if (isElementPresent(driver,dstIdentify, dstLocation)) {
				to = driver.findElement(getLocator(dstIdentify, dstLocation));
			}

			Actions action = new Actions(driver);
			action.dragAndDrop(from, to).build().perform();
		} catch (Exception e) {
			failureReport(driver,"draggable Exception Info :",
					"Unable to drag the element with locator :" + srcLocation);

			logger.error("Unable to drag the element with locator :"
					+ srcLocation);
		}

	}

	/**
	 * @FunctionName selectByVisibleText
	 * @Description Function to Select a value from the DropDown using
	 *              visibleText,This function takes the identifyBy ,locator,
	 *              values as parameters and select value from the drop down
	 *              present in the specified location using visibleText
	 * @param identifyBy
	 * @param locator
	 * @param visibleText
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */
	public static void selectByVisibleText(WebDriver driver,String identifyBy, String locator,String visibleText, String element) throws Throwable {

		try {
			if (isElementPresent(driver,identifyBy, locator)) {
				Select s = new Select(driver.findElement(getLocator(identifyBy,
						locator)));
				s.selectByVisibleText(visibleText);
				SuccessReport("Select value from "+element+" Field", "Data " + visibleText
						+ " is selected successfully");

				s.getOptions().size();

			}

		} catch (Exception e) {
			failureReport(driver,"Select Value Exception Info :",
					"Unable to select the value with text " + visibleText
					+ " for element with locator " + locator);
			logger.error("Unable to select the value with text " + visibleText
					+ " for element with locator " + locator);

		}
	}

	/**
	 * @FunctionName selectByIndex
	 * @Description Function to Select a value from the DropDown using
	 *              index,This function takes the identifyBy ,locator, values as
	 *              parameters and select value from the drop down present in
	 *              the specified location using index
	 * @param identifyBy
	 * @param locator
	 * @param index
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static void selectByIndex(WebDriver driver,String identifyBy, String locator,int index) throws Throwable {
		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				Select s = new Select(driver.findElement(getLocator(identifyBy,locator)));
				s.selectByIndex(index);

			}

		} catch (Exception e) {
			failureReport(driver,"Select Value Exception Info :",
					"Unable to select the value with index " + index
					+ " for element with locator " + locator);
			logger.error("Unable to select the value with index " + index
					+ " for element with locator " + locator);
		}

	}

	/**
	 * @FunctionName selectByValue
	 * @Description Function to Select a value from the DropDown using
	 *              value,This function takes the identifyBy ,locator, values as
	 *              parameters and select value from the drop down present in
	 *              the specified location using value
	 * @param identifyBy
	 * @param locator
	 * @param value
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static void selectByValue(WebDriver driver,String identifyBy, String locator,String value,String element) throws Throwable {
		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				System.out.println("1");
				Select s = new Select(driver.findElement(getLocator(identifyBy,locator)));
				System.out.println("2");
				s.selectByValue(value);
				System.out.println("3");

				System.out.println("3"+s.getFirstSelectedOption().getText());

				SuccessReport("Select value from "+element+" Field", "Data " + value
						+ " is selected successfully");
			}

		} catch (Exception e) {
			failureReport(driver,"Select Value Exception Info :",
					"Unable to select the value with value " + value
					+ " for element with locator " + locator);
			logger.error("Unable to select the value with value " + value
					+ " for element with locator " + locator);
		}

	}

	/**
	 * @FunctionName switchWindowByTitle
	 * @Description Function to switch to a specific window,This function is
	 *              used to select the window with title which is passed as a
	 *              parameter
	 * @param windowTitle
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static void pressTAB(WebDriver driver,String identifyBy, String locator) throws Throwable
	{
		if (isElementPresent(driver,identifyBy,locator)) {

		}
	}

	public  void switchWindowByTitle(String windowTitle) throws Throwable {

		try {
			Set<String> windowList = driver.getWindowHandles();
			for (String window : windowList) {
				driver.switchTo().window(window);
				if (driver.getTitle().contains(windowTitle))

					break;
			}

		} catch (Exception e) {
			failureReport(driver,"Window Selection Exception:",
					"Unable to select the window with the title :"
							+ windowTitle);
			logger.error("Unable to select the window with the title :"
					+ windowTitle);

		}

	}

	/**
	 * @FunctionName switchToParentWindow
	 * @Description Function to switch to a parent window
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public  void switchToParentWindow() throws Throwable {

		try {
			Set<String> windowList = driver.getWindowHandles();
			List<Object> Windows = Arrays.asList(windowList.toArray());
			driver.switchTo().window(Windows.get(0).toString());
			//waituntilpageload();
		} catch (Exception e) {
			failureReport(driver,"Window Selection Exception:",
					"Unable to select the parent window :");
			logger.error("Unable to select the parent window :");

		}

	}

	/**
	 * @FunctionName switchToLastWindow
	 * @Description Function to switch to a specific window,This function is
	 *              used to select a window which is opened last during the
	 *              script execution
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */
	public void switchToLastWindow() throws Throwable {

		try {
			Set<String> windowList = driver.getWindowHandles();
			for (String window : windowList) {
				driver.switchTo().window(window);

			}
			driver.manage().window().maximize();
			//waituntilpageload();

		} catch (Exception e) {
			failureReport(driver,"Window Selection Exception:",
					"Unable to select the window");
			logger.error("Unable to select the window :");

		}
	}

	/**
	 * @FunctionName getTableColumncount
	 * @Description Function to Find the Number of columns in a table,This
	 *              function is used to find a count of columns present in a
	 *              table at a specified location which is passed as a parameter
	 * @param identifyBy
	 * @param locator
	 * @return int
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */

	public  int getTableColumncount(WebDriver driver, String identifyBy, String locator)
			throws Throwable {

		int count = 0;
		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				WebElement tr = driver.findElement(getLocator(identifyBy,
						locator));
				List<WebElement> columns = tr.findElements(By.tagName("td"));
				count = columns.size();
			}

			return count;
		} catch (Exception e) {
			failureReport(driver,"Table Column Count Exception Info :",
					"Unable to get the Column count in a table with locator :"
							+ locator);
			logger.error("Unable to get the Column count in a table with locator :"
					+ locator);
			return count;
		}
	}

	/**
	 * @FunctionName getTableRowCount
	 * @Description Function to Find the Number of rows in a table,This function
	 *              is used to find a count of rows present in a table at a
	 *              specified location which is passed as a parameter
	 * @param identifyBy
	 * @param locator
	 * @return int
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public int getTableRowCount(WebDriver driver,String identifyBy, String locator)
			throws Throwable {
		int count = 0;
		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				WebElement table = driver.findElement(getLocator(identifyBy,
						locator));
				List<WebElement> rows = table.findElements(By.tagName("tr"));
				count = rows.size();
			}

			return count;
		} catch (Exception e) {
			failureReport(driver,"Table Row Count Exception Info :",
					"Unable to get the Row count in a table with locator :"
							+ locator);
			logger.error("Unable to get the Row count in a table with locator :"
					+ locator);
			return count;
		}
	}

	/**
	 * @FunctionName isChecked
	 * @Description Function to Verify Weather The checkbox is Selected or
	 *              Not,This function is used to check the checkbox or radio
	 *              button present in a specified location is selected or not
	 * @param identifyBy
	 * @param locator
	 * @return boolean
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static  boolean isChecked(WebDriver driver, String identifyBy, String locator)
			throws Throwable {
		boolean isPresent = false;
		try {

			WebDriverWait wait = new WebDriverWait(driver, 15);
		    wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy,locator)));
			
			if (isElementPresent(driver,identifyBy, locator)) {
				if (driver.findElement(getLocator(identifyBy, locator))
						.isSelected()) {
					isPresent = true;
				}
			}

			return isPresent;
		} catch (Exception e) {
			// logger.error("Unable to check the element with locator "+locator);
			logger.error(e.toString());
			return isPresent;
		}

	}

	public static  boolean isChecked(WebDriver driver, String identifyBy, String locator,String Element)
			throws Throwable {
		boolean isPresent = false;
		try {

/*			WebDriverWait wait = new WebDriverWait(driver, 15);
		    wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy,locator)));*/
			
		//	if (isElementPresent(driver,identifyBy, locator)) {
				if (driver.findElement(getLocator(identifyBy, locator))
						.getAttribute("value").equals("Y")) {
					isPresent = true;
				}
			//}

			return isPresent;
		} catch (Exception e) {
			// logger.error("Unable to check the element with locator "+locator);
			logger.error(e.toString());
			return isPresent;
		}

	}
	
	/**
	 * @FunctionName selectRadiobutton
	 * @Description Function to Select a radio button,This function is used to
	 *              select/deselect a radio button present at specified location
	 * @param identifyBy
	 * @param locator
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static void selectRadiobutton(WebDriver driver, String identifyBy, String locator)
			throws Throwable {
		try {

			WebDriverWait wait = new WebDriverWait(driver, 15);
		    wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy,locator)));
			
			if (isElementPresent(driver,identifyBy, locator)) {
				driver.findElement(getLocator(identifyBy, locator)).click();
				
				flag = true;
			}

		} catch (Exception e) {
			failureReport(driver,"Radio button Exception Info :",
					"Unable to select the element with locator : " + locator);

			logger.error("Unable to select the element with locator : "
					+ locator);
		}

	}

	/**
	 * @FunctionName SelectCheckbox
	 * @Description Function to Select a checkbox
	 * @param identifyBy
	 * @param locator
	 * @param checkFlag
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */
	public static  void selectCheckbox(WebDriver driver, String identifyBy, String locator,
			String checkFlag) throws Throwable {

		try {
			
			WebDriverWait wait = new WebDriverWait(driver, 15);
		    wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy,locator)));
			
			By by = getLocator(identifyBy, locator);

			if (isElementPresent(driver,identifyBy, locator)) {
				if ((checkFlag).equalsIgnoreCase("ON")) {
					if (!(driver.findElement(by).isSelected())) {
						driver.findElement(by).click();
					}
				} else if ((checkFlag).equalsIgnoreCase("OFF")) {
					if ((driver.findElement(by).isSelected()))
						driver.findElement(by).click();
				}
			}

		} catch (Exception e) {
			failureReport(driver,"Checkbox Exception Info :",
					"Unable to check the element with locator :" + locator);
			logger.error("Unable to check the element with locator :" + locator);
		}
	}

	/**
	 * @FunctionName getCssValue
	 * @Description Function to get the Css value,This function is useed to get
	 *              the css aatribute values like color,size,font present at a
	 *              specified location
	 * @param identifyBy
	 * @param locator
	 * @param cssattribute
	 * @return String
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */

	public  String getCssValue(String identifyBy, String locator,
			String cssattribute) throws Throwable {
		String value = "";
		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				value = driver.findElement(getLocator(identifyBy, locator))
						.getCssValue(cssattribute);
			}

			return value;
		} catch (Exception e) {
			logger.error(e.toString());
			return value;
		}

	}

	/**
	 * @FunctionName getText
	 * @Description Function to get the text from the webpage,This function is
	 *              used
	 * @param identifyBy
	 * @param locator
	 * @return String
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static String getText(WebDriver driver, String identifyBy, String locator)
			throws Throwable {
		String value = "";

		try {
			
			WebDriverWait wait = new WebDriverWait(driver, 15);
		    wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy,locator)));
			
		    
			if (isElementPresent(driver,identifyBy, locator)) {
				value = driver.findElement(getLocator(identifyBy, locator)).getText().trim();
				System.out.println("Text Value of Object :-" +value);
			}

			return value;
		} catch (Exception e) {

			System.out.println("Object not found" +value);
			return value;
		}
	}

	public static boolean compareText(WebDriver driver, String identifyBy, String locator, String valToCompare)
			throws Throwable {
		
		flag = false;
		
		String value = "";

		try {
			
			value = getText(driver, identifyBy, locator);

			if (value.equals(valToCompare)) {
				flag = true;
			}
			
			
		} catch (Exception e) {

			System.out.println("Object not found" +value);
			
		}
		
		return flag;
	}

	

	/**
	 * @FunctionName getText
	 * @Description Function to get the value attribute of an input field from the webpage,This function is
	 *              used
	 * @param identifyBy
	 * @param locator
	 * @return String
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */

	public  String getValue(WebDriver driver,String identifyBy, String locator)
			throws Throwable {
		String value = "";

		try {

			WebDriverWait wait = new WebDriverWait(driver, 15);
		    wait.until(ExpectedConditions.elementToBeClickable(getLocator(identifyBy,locator)));
			
			if (isElementPresent(driver,identifyBy, locator)) {
				value = driver.findElement(getLocator(identifyBy, locator)).getAttribute("value").trim();
			}

			return value;
		} catch (Exception e) {

			return value;
		}
	}


	/**
	 * @FunctionName screenShot
	 * @Description Function to take screenShot
	 * @param fileName
	 * @return void
	 * @author: Rajender Cheekoti 
	 */

	public static  void screenShot(WebDriver driver,String fileName) {
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		try {
			// Now you can do whatever you need to do with it, for example copy
			// somewhere
			FileUtils.copyFile(scrFile, new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static  void screenShotWithWindow(String fileName) {

		try {

			Robot robot = new Robot();

			BufferedImage screenShot = robot.createScreenCapture(new Rectangle(
					Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "JPG", new File(fileName));
		} catch (Exception e) {

			logger.error(e.toString());
		}
	}

	/**
	 * @FunctionName mouseHoverByJavaScript
	 * @Description Function to move the mouse on to specific element
	 * @param identifyBy
	 * @param locator
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public  void mouseHoverByJavaScript(String identifyBy, String locator)
			throws Throwable {

		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				WebElement mo = driver.findElement(getLocator(identifyBy,
						locator));
				String javaScript = "var evObj = document.createEvent('MouseEvents');"
						+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
						+ "arguments[0].dispatchEvent(evObj);";
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript(javaScript, mo);
			}

		}

		catch (Exception e) {
			failureReport(driver,"Mouse Over To Exception Info :",
					"Unable to moveover on element with locator :" + locator);
			logger.error("Unable to moveover on element with locator :"
					+ locator);
		}
	}

	/**
	 * @FunctionName JSClick
	 * @Description Function to click a button or link
	 * @param identifyBy
	 * @param locator
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */

	public  void JSClick(String identifyBy, String locator, String elmName)
			throws Throwable {

		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				WebElement element = driver.findElement(getLocator(identifyBy,
						locator));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				SuccessReport("Click on : " + elmName, "Click action performed.");
			}

		}

		catch (Exception e) {
			failureReport(driver,"Click Exception Info :",
					"Unable to click the element with locator :" + locator + " With given name : " + elmName);
			logger.error("Unable to click the element with locator :" + elmName);

		}
	}

	/**
	 * @FunctionName switchToFrameByIndex
	 * @Description Function to switch to a frame
	 * @param index
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */
	public void switchToFrameByIndex(int index) throws Throwable {

		try {
			driver.switchTo().frame(index);
		} catch (Exception e) {
			failureReport(driver,"Frame Selection Exception Info :",
					"Unable to select the frame with index :" + index);
			logger.error("Unable to select the frame with index :" + index);
		}
	}

	/**
	 * @FunctionName switchToFrameByName
	 * @Description Function to switch to a frame
	 * @param name
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */
	public void switchToFrameByName(String name) throws Throwable {

		try {
			driver.switchTo().frame(name);
		} catch (Exception e) {
			failureReport(driver,"Frame Selection Exception Info :",
					"Unable to select the frame with name :" + name);
			logger.error("Unable to select the frame with name :" + name);
		}
	}

	/**
	 * @FunctionName switchToDefaultFrame
	 * @Description Function to switch to a default frame
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 * 
	 */

	public void switchToDefaultFrame() throws Throwable {

		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			failureReport(driver,"Frame Selection Exception Info :",
					"Unable to select the Defaultframe");
			logger.error("Unable to select the Defaultframe");
		}
	}

	/**
	 * @FunctionName switchToFrameByLocator
	 * @Description Function to switch to a frame
	 * @param identifyBy
	 * @param locator
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */
	public void switchToFrameByLocator(String identifyBy, String locator)
			throws Throwable {

		try {
			if (isElementPresent(driver,identifyBy, locator)) {
				driver.switchTo().frame(
						driver.findElement(getLocator(identifyBy, locator)));
			}

		} catch (Exception e) {
			failureReport(driver,"Frame Selection Exception Info :",
					"Unable to select the frame with locator :" + locator);
			logger.error("Unable to select the frame with locator :" + locator);

		}
	}

	/**
	 * @FunctionName implicitWait
	 * @Description Function to wait selenium until element present on web page
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static void implicitWait() {

		driver.manage()
		.timeouts()
		.implicitlyWait(
				Long.parseLong(configProps.getProperty("ImplicitWait")),
				TimeUnit.SECONDS);
	}

	/**
	 * @FunctionName waitForElementPresent
	 * @Description Function to wait selenium until element present on web page
	 * @param identifyBy
	 * @param locator
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public static  boolean waitForElementPresent(WebDriver driver,String identifyBy, String locator, int timeout)
			throws Throwable {
		//int timeout = Integer.parseInt(configProps.getProperty("ImplicitWait").trim());
		WebElement wel = fluentWaitElementVisibility(driver,getLocator(identifyBy, locator),timeout);
		if(wel!=null){
			return true;
		}else{
			failureReport(driver,
					"Element Identification Exception Info :",
					"Unable to identify the element with locator :"
							+ locator.toString());
			logger.error("Unable to identify the element with locator : "
					+ locator.toString());
			return false;
		}
	}

	/**
	 * @FunctionName highlight
	 * @Description Function to highlight the element
	 * @param element
	 * @return void
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */
	public static void highlight(WebElement element) {

		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].style.border='4px solid green'", element);

		}
	}

	/**
	 * @FunctionName getListCount
	 * @Description Function to find a Count of WebElements
	 * @param identifyBy
	 * @param locator
	 * @return int
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */
	public int getListCount(String identifyBy, String locator) throws Throwable {
		int count = 0;

		try {
			if (isElementPresent(driver,identifyBy, locator)) {
				count = driver.findElements(getLocator(identifyBy, locator))
						.size();
			}

			return count;

		} catch (Exception e) {
			failureReport(driver,"Frame Selection Exception Info :",
					"Unable to select the frame with locator :" + locator);
			logger.error("Unable to select the frame with locator :" + locator);
			return count;

		}
	}

	/**
	 * @FunctionName VerifyTextPresent
	 * @Description This Function will get the Actual text from the webpage and
	 *              it verifies whether the expected text(which can be taken
	 *              from excel sheet) is matching with Actual Text or not
	 * @param identifyBy
	 * @param locator
	 * @param value
	 * @return int
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */

	public static boolean verifyTextPresent(WebDriver driver,String identifyBy, String locator,String value) throws Throwable {
		String Expvalue = value;
		System.out.println("Exp value is " + Expvalue);
		String Actvalue = "";
		boolean TextContains = false;
		try {

			Actvalue = getText(driver,identifyBy, locator);			
			System.out.println("Act value is " + Actvalue);
			if (Actvalue.trim().equalsIgnoreCase(Expvalue.trim())) {
				TextContains = true;
			}
			return TextContains;

		} catch (Exception e) {
			logger.error(e.toString());

			return TextContains;
		}

	}

	/**
	 * @FunctionName VerifyValuePresent
	 * @Description This Function will get the Actual value from the edit fields from the webpage and
	 *              it verifies whether the expected text(which can be taken
	 *              from excel sheet) is matching with Actual Text or not
	 * @param identifyBy
	 * @param locator
	 * @param value
	 * @return int
	 * @throws Throwable
	 * @author: Rajender Cheekoti 
	 */

	public  boolean verifyValuePresent(WebDriver driver,String identifyBy, String locator,
			String value) throws Throwable {
		String Expvalue = value;
		System.out.println("Expvalue is " + Expvalue);
		String Actvalue = "";
		boolean TextContains = false;
		try {

			Actvalue = getValue(driver,identifyBy, locator);			
			System.out.println("Actvalue is " + Actvalue);
			if (Actvalue.equalsIgnoreCase(Expvalue)) {
				TextContains = true;
			}
			return TextContains;

		} catch (Exception e) {
			logger.error(e.toString());

			return TextContains;
		}

	}



	/**
	 * @FunctionName verifyTextContains
	 * @Description This Function will get the Actual text from the webpage and
	 *              it verifies whether the expected text(which can be taken
	 *              from excel sheet) is contained in Actual text or not
	 * @param identifyBy
	 * @param locator
	 * @param value
	 * @return boolean
	 * @throws Throwable
	 * @author: Rajender Cheekoti
	 */

	public static boolean verifyTextContains(WebDriver driver,String identifyBy, String locator,
			String value) throws Throwable {
		String Expvalue = value;
		System.out.println("Expvalue is " + Expvalue);
		String Actvalue = "";
		boolean verifyText = false;
		try {

			Actvalue = getText(driver, identifyBy, locator);
			System.out.println("Actvalue is " + Actvalue);
			if (Actvalue.contains(Expvalue)) {
				verifyText = true;
			}
			return verifyText;

		} catch (Exception e) {
			logger.error(e.toString());

			return verifyText;
		}

	}

	/**
	 * Retrieves the WebElements for the locator.
	 * 
	 * @param identifyBy
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public static List<WebElement> getWebElements(WebDriver driver, String identifyBy, String locator)
			throws Throwable {
		List<WebElement> webElements = null;

		try {
			if (isElementPresent(driver,identifyBy, locator)) {
				webElements = driver.findElements(getLocator(identifyBy,
						locator));
			}

			return webElements;

		} catch (Exception e) {
			failureReport(driver,"Exception Info :", "Unable to find with locator :"
					+ locator);
			logger.error("Unable to find the locator :" + locator);
			return null;

		}
	}

	/*public static String getText(WebDriver driver,String identifyBy, String locator) throws Throwable
	{
		String result="" ;
		System.out.println();
		result = driver.findElement(getLocator(identifyBy,locator)).getText();
		return result;
	}*/
	public String getText(WebDriver driver,WebElement webElement) throws Throwable 
	{
		String value = null;
		boolean isPresent = false;
		try {

			if (webElement.isDisplayed()) {
				highlight(webElement);
				isPresent = true;
			}

			if (isPresent) {
				value = webElement.getText();
			}

			return value;
		} catch (Exception e) {
			failureReport(driver,"Exception Info :", "Unable to find with locator :"
					+ webElement);
			logger.error("Unable to find the locator :" + webElement);
			return null;
		}
	}

	public static void SuccessReport(String strStepName, String strStepDes) throws Throwable {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy hh mm ss SSS");
		String formattedDate = sdf.format(date);
		String time = formattedDate.replace(" ", "_");
		boolean bool = false;

		int intReporterType = Integer.parseInt(configProps.getProperty("reportsType"));

		if (intReporterType == 1) {
			System.out.println("Exit from if");
		} else if (intReporterType == 2 && intReporterType != 1) {
			if (configProps.getProperty("CaptureScreenShotOnSuccess").equalsIgnoreCase("True") && execFlag) {
				screenShot(driver,TestEngine.filePath() + "\\Screenshots\\"
						+ time/*
						 * + "_" + TestEngine.timeStamp
						 */ + ".jpeg");
				bool = true;
			} else if (configProps.getProperty("CaptureScreenShotOnSuccessWithBrowser").equalsIgnoreCase("True")) {
				screenShotWithWindow(TestEngine.filePath() + "\\Screenshots\\"
						+ time/*
						 * + "_" + TestEngine.timeStamp
						 */ + ".jpeg");
				bool = true;
			}
			onSuccess(strStepName, strStepDes, time, bool);

		} else {
			if (intReporterType != 1) {
				if (configProps.getProperty("CaptureScreenShotOnSuccess").equalsIgnoreCase("True") && execFlag) {
					screenShot(driver,TestEngine.filePath() + "\\Screenshots\\"
							+ time/*
							 * + "_" + TestEngine.timeStamp
							 */ + ".jpeg");
					bool = true;
				} else if (configProps.getProperty("CaptureScreenShotOnSuccessWithBrowser").equalsIgnoreCase("True")) {
					screenShotWithWindow(TestEngine.filePath() + "\\Screenshots\\"
							+ time/*
							 * + "_" + TestEngine.timeStamp
							 */ + ".jpeg");
					bool = true;
				}
				onSuccess(strStepName, strStepDes, time, bool);

			}

		}
		//Commented below code to remove JDK dependency for Switch - VP@0524
		/*			switch (intReporterType) {
		case 1:

			break;
		case 2:
			if (configProps.getProperty("CaptureScreenShotOnSuccess")
					.equalsIgnoreCase("True")&& execFlag) {
				screenShot(TestEngine.filePath()+"\\Screenshots\\"
						+ time+ "_"
						+ TestEngine.timeStamp + ".jpeg");
				bool=true;
			}
			else if(configProps.getProperty("CaptureScreenShotOnSuccessWithBrowser")
					.equalsIgnoreCase("True")) {
			screenShotWithWindow(TestEngine.filePath()+"\\Screenshots\\"
						+ time+ "_"
						+ TestEngine.timeStamp + ".jpeg");
				bool=true;
			}
			onSuccess(strStepName, strStepDes,time,bool);

			break;

		default:
			if (configProps.getProperty("CaptureScreenShotOnSuccess")
					.equalsIgnoreCase("True") && execFlag) {
				screenShot(TestEngine.filePath()+"\\Screenshots\\"
						+ time+ "_"
						+ TestEngine.timeStamp + ".jpeg");
				bool=true;
			}
			else if(configProps.getProperty("CaptureScreenShotOnSuccessWithBrowser")
					.equalsIgnoreCase("True")) {
				screenShotWithWindow(TestEngine.filePath()+"\\Screenshots\\"
						+ time+ "_"
						+ TestEngine.timeStamp + ".jpeg");
				bool=true;
			}
			onSuccess(strStepName, strStepDes,time,bool);

			break;
		}*/
	}

	public static void failureReport(WebDriver driver,String strStepName, String strStepDes) throws Throwable {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy hh mm ss SSS");
		String formattedDate = sdf.format(date);
		String time = formattedDate.replace(" ", "_");

		int intReporterType = Integer.parseInt(configProps.getProperty("reportsType"));

		if (intReporterType == 1) {
			flag = true;
			System.out.println("Exit from If");
		} else if (intReporterType == 2 && intReporterType != 1) {
			if (configProps.getProperty("CaptureScreenShotOnFailureWithBrowser").equalsIgnoreCase("True")) {
				screenShotWithWindow(TestEngine.filePath() + "\\Screenshots\\"
						+ time/*
						 * + "_" + TestEngine.timeStamp
						 */ + ".jpeg");
			} else {
				screenShot(driver,TestEngine.filePath() + "\\Screenshots\\"
						+ time /*
						 * + "_" + TestEngine.timeStamp
						 */ + ".jpeg");
			}
			flag = true;
			onFailure(strStepName, strStepDes, time);


		} else {

			if (intReporterType != 1) {
				flag = true;
				if (configProps.getProperty("CaptureScreenShotOnFailureWithBrowser").equalsIgnoreCase("True")) {
					screenShotWithWindow(TestEngine.filePath() + "\\Screenshots\\"
							+ time/*
							 * + "_" + TestEngine.timeStamp
							 */ + ".jpeg");
				} else {
					screenShot(driver,TestEngine.filePath() + "\\Screenshots\\"
							+ time /*
							 * + "_" + TestEngine.timeStamp
							 */ + ".jpeg");
				}
				onFailure(strStepName, strStepDes, time);
			}

		}
		// Commented below code to remove JDK dependency for Switch - VP@0524
		/*
		 * switch (intReporterType) { case 1: flag = true; break; case 2: if
		 * (configProps.getProperty("CaptureScreenShotOnFailureWithBrowser")
		 * .equalsIgnoreCase("True")) {
		 * screenShotWithWindow(TestEngine.filePath()+"\\Screenshots\\" + time+
		 * "_" + TestEngine.timeStamp + ".jpeg"); } else {
		 * screenShot(TestEngine.filePath()+"\\Screenshots\\" + time + "_" +
		 * TestEngine.timeStamp + ".jpeg"); } flag = true;
		 * onFailure(strStepName, strStepDes,time); break;
		 * 
		 * default: flag = true; if
		 * (configProps.getProperty("CaptureScreenShotOnFailureWithBrowser")
		 * .equalsIgnoreCase("True")) {
		 * screenShotWithWindow(TestEngine.filePath()+"\\Screenshots\\" + time+
		 * "_" + TestEngine.timeStamp + ".jpeg"); } else {
		 * screenShot(TestEngine.filePath()+"\\Screenshots\\" + time + "_" +
		 * TestEngine.timeStamp + ".jpeg"); } onFailure(strStepName,
		 * strStepDes,time); break; }
		 */
	}

	public void warningReport(String strStepName, String strStepDes) throws Throwable {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy hh mm ss SSS");
		String formattedDate = sdf.format(date);
		String time = formattedDate.replace(" ", "_");

		int intReporterType = Integer.parseInt(configProps.getProperty("reportsType"));

		if (intReporterType == 1) {
			flag = true;
			System.out.println("Exit from If ..");
		} else if (intReporterType == 2 && intReporterType != 1) {
			if (configProps.getProperty("CaptureScreenShotOnFailureWithBrowser").equalsIgnoreCase("True")) {
				screenShotWithWindow(TestEngine.filePath() + "\\Screenshots\\"
						+ time/*
						 * + "_" + TestEngine.timeStamp
						 */ + ".jpeg");
			} else {
				screenShot(driver,TestEngine.filePath() + "\\Screenshots\\"
						+ time /*
						 * + "_" + TestEngine.timeStamp
						 */ + ".jpeg");
			}
			flag = true;
			onWarning(strStepName, strStepDes, time);

		} else {
			if (intReporterType != 1) {
				flag = true;
				if (configProps.getProperty("CaptureScreenShotOnFailureWithBrowser").equalsIgnoreCase("True")) {
					screenShotWithWindow(TestEngine.filePath() + "\\Screenshots\\"
							+ time/*
							 * + "_" + TestEngine.timeStamp
							 */ + ".jpeg");
				} else {
					screenShot(driver,TestEngine.filePath() + "\\Screenshots\\"
							+ time /*
							 * + "_" + TestEngine.timeStamp
							 */ + ".jpeg");
				}
				onWarning(strStepName, strStepDes, time);
			}

		}

		// Commented below code to remove JDK dependency for Switch - VP@0524

		/*
		 * switch (intReporterType) { case 1: flag = true; break; case 2: if
		 * (configProps.getProperty("CaptureScreenShotOnFailureWithBrowser")
		 * .equalsIgnoreCase("True")) {
		 * screenShotWithWindow(TestEngine.filePath()+"\\Screenshots\\" + time+
		 * "_" + TestEngine.timeStamp + ".jpeg"); } else {
		 * screenShot(TestEngine.filePath()+"\\Screenshots\\" + time + "_" +
		 * TestEngine.timeStamp + ".jpeg"); } flag = true;
		 * onWarning(strStepName, strStepDes,time); break;
		 * 
		 * default: flag = true; if
		 * (configProps.getProperty("CaptureScreenShotOnFailureWithBrowser")
		 * .equalsIgnoreCase("True")) {
		 * screenShotWithWindow(TestEngine.filePath()+"\\Screenshots\\" + time+
		 * "_" + TestEngine.timeStamp + ".jpeg"); } else {
		 * screenShot(TestEngine.filePath()+"\\Screenshots\\" + time + "_" +
		 * TestEngine.timeStamp + ".jpeg"); } onWarning(strStepName,
		 * strStepDes,time); break; }
		 */
	}

	public String getCurrentUrl(){
		return driver.getCurrentUrl();
	}

	/* public void scrollToWebElement(String identifyBy, String locator){
	    if(browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("chrome")){
	     WebElement we = null;
	     try {
	      we = driver.findElement(getLocator(identifyBy, locator));
	     } catch (Exception e) {
	      e.printStackTrace();
	     }
	     we.sendKeys(Keys.PAGE_DOWN);   
	    }else{
	     WebElement we;
	     try {
	      JavascriptExecutor jse = (JavascriptExecutor)driver;
	      jse.executeScript("scroll(0, 250)");
	      jse.executeScript("scroll(0, 550)");
	      jse.executeScript("scroll(0, 550)");
	     } catch (Exception e) {
	      e.printStackTrace();
	     }

	    }
	   }*/
	public  void fileUpload(String identifyBy, String locator,
			String fileName) throws Throwable {
		String Uploadedfile="";
		try {

			String file[] =fileName.split("\\\\");
			for(String upload:file){
				Uploadedfile=upload;

			}

			if (isElementPresent(driver,identifyBy, locator)) {
				By by = getLocator(identifyBy, locator);
				//driver.findElement(by).clear();
				driver.findElement(by).sendKeys(fileName);


				SuccessReport("Uploading File", "File   "
						+ Uploadedfile + "  Uploaded successfully");
			}

		} catch (Exception e) {
			failureReport(driver,"Uploading File Exception Info :",
					"Unable to upload the File " + Uploadedfile
					+ " into an element with locator :" + locator);

			logger.error("Unable to Upload the file " + Uploadedfile
					+ " into an element with locator :" + locator);
		}

	}
	public  void refresh(){
		driver.navigate().refresh();
	}


	public void waituntilpageload(){
		wait.until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver) {
				return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
			}
		}
				);
	}

	/**
	 * @FunctionName getSizeOfListbox
	 * @Description This function will return size of Listbox
	 * @param identifyBy
	 * @param locator
	 * @param element
	 * @return iSizeOfListBox -- size of list box
	 * @throws Throwable
	 * @author Vinayak Patil
	 */
	public static int getSizeOfListbox(WebDriver driver,String identifyBy, String locator, String element) throws Throwable 
	{

		int iSizeOfListBox = 0;

		try {

			List<WebElement> list = driver.findElements(getLocator(identifyBy, locator));
			iSizeOfListBox = driver.findElements(getLocator(identifyBy, locator)).size();

			SuccessReport("Size of Listbox "+element+" Field", "Data is " + iSizeOfListBox);



		} catch (Exception e) 
		{
			failureReport(driver,"Select Value Exception Info :",
					"Unable to find size for element with locator " + locator);
			logger.error("Unable to find size for element with locator " +locator);

		}
		return iSizeOfListBox;
	}

	public static void clickPolicyNo(WebDriver driver,String identifyBy, String locator, String sPolicyNo) throws Exception
	{
		String sTemp=null;
		List<WebElement> list = driver.findElements(getLocator(identifyBy, locator));
		if(list.size()>0)
		{
			for(WebElement ele:list)
			{
				if(ele.getText().equalsIgnoreCase(sPolicyNo))
				{
					ele.click();
				}
			}
		}

	}

	public static boolean verifyFields(WebDriver driver, String identifyBy, String locator, String element, String sExpectedValue) throws Throwable
	{
		report.setStepName("Summary Field Validation with ID3 for Field "+ element +" -> %s".replace("%s", sExpectedValue)+" started.");
		boolean flag = false;
		try {
			flag = verifyTextPresent(driver, identifyBy, locator, sExpectedValue);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reportLogger(flag, "Summary Field Validation with ID3 for   "+ element +" -> %s".replace("%s", sExpectedValue)+" completed with Result -->"+flag, "");
		return flag;
	}

	public static String getSelectedValue(WebDriver driver,String identifyBy, String locator) throws Throwable{
		
		String sSelected = null;
		
		try {

			if (isElementPresent(driver,identifyBy, locator)) {
				
				Select s = new Select(driver.findElement(getLocator(identifyBy,locator)));
				
				sSelected = s.getFirstSelectedOption().getText();
				
				
			}

		} catch (Exception e) {
			
		}

		return sSelected;
		
	}
	
	public void menuClick(WebDriver driver, String identifyBy, String locator1, String locator2) throws Throwable {
		try {

			if (isElementPresent(driver, identifyBy, locator1)) {
				Actions action = new Actions(driver);
				WebElement Element = driver.findElement(getLocator(identifyBy, locator1));
				action.click(Element).build().perform();
				Thread.sleep(2000);
				action.click(driver.findElement(getLocator(identifyBy, locator2))).build().perform();
				Thread.sleep(2000);

			}

		} catch (Exception e) {
			failureReport(driver, "Mouse Over To Exception Info :",
					"Unable to moveover on element with locator :" + locator2);
			logger.error("Unable to moveover on element with locator :" + locator2);
		}

}

	public static boolean menuVerify(WebDriver driver, String identifyBy, String locator1, String locator2) throws Throwable {
		flag = false;
		try {

			if (isElementPresent(driver, identifyBy, locator1)) {
				Actions action = new Actions(driver);
				WebElement Element = driver.findElement(getLocator(identifyBy, locator1));
				action.click(Element).build().perform();
				Thread.sleep(2000);
				//action.click(driver.findElement(getLocator(identifyBy, locator2))).build().perform();
				WebElement Element2 = driver.findElement(getLocator(identifyBy, locator2));
				
				if (Element2.isDisplayed()) {
					flag = true;
				}
				

			}

		} catch (Exception e) {
			failureReport(driver, "Mouse Over To Exception Info :",
					"Unable to moveover on element with locator :" + locator2);
			logger.error("Unable to moveover on element with locator :" + locator2);
		}

		return flag;
	}

	public void selectMenu(WebDriver driver,String menuTitle,String subMenu) throws InterruptedException
	{
		boolean flag=true;
		List<WebElement> mainMenu = driver.findElements(By.xpath("//*[@id='item1']/a"));
		
		for(WebElement mMenu:mainMenu)
		{
			System.out.println(""+mMenu.getText().split("\n")[0]);
			if(mMenu.getText().split("\n")[0].trim().equalsIgnoreCase(menuTitle))
			{
				Actions action = new Actions(driver);
				action.moveToElement(mMenu).perform();
				Thread.sleep(5000);
				List<WebElement> subMenuSelect = driver.findElements(By.xpath(".//*[@id='item1']/ul/li/a"));
				for(WebElement sMenu:subMenuSelect)
				{
					System.out.println(sMenu.getText().trim());
					if(sMenu.getText().trim().equalsIgnoreCase(subMenu))
					{
						sMenu.click();
						flag=false;
						break;
					}
				}
				if(flag)
					mMenu.click();
				break;
			}
				
		}
	} 
	
	public static boolean menuVerify(WebDriver driver,String menuTitle,String subMenu) throws InterruptedException
	{
		flag=false;
		List<WebElement> mainMenu = driver.findElements(By.xpath("//*[@id='item1']/a"));
		
		for(WebElement mMenu:mainMenu)
		{
			System.out.println(""+mMenu.getText().split("\n")[0]);
			if(mMenu.getText().split("\n")[0].trim().equalsIgnoreCase(menuTitle))
			{
				Actions action = new Actions(driver);
				action.moveToElement(mMenu).perform();
				Thread.sleep(5000);
				List<WebElement> subMenuSelect = driver.findElements(By.xpath(".//*[@id='item1']/ul/li/a"));
				for(WebElement sMenu:subMenuSelect)
				{
					System.out.println(sMenu.getText().trim());
					if(sMenu.getText().trim().equalsIgnoreCase(subMenu))
					{
						
						flag=true;
						break;
					}
				}
				
			}
				
		}
		
		return flag;
	} 
	
	
	public static boolean dateSelector(WebDriver driver,String identifyBy, String locator1, String locator2 ) throws Throwable, Throwable, Throwable{
		
		if (isElementPresent(driver, identifyBy, locator1)) {
			Actions action = new Actions(driver);
			WebElement Element = driver.findElement(getLocator(identifyBy, locator1));
			action.clickAndHold().build().perform();
			Thread.sleep(2000);
			
			WebElement Element2 = driver.findElement(getLocator(identifyBy, locator2));
			
			action.click(Element2).build().perform();
			
			flag = true;
			
		}
		
		return flag;
		
	}
	
	
	public static boolean verifyToolTip(WebDriver driver, String identifyBy, String locator1, String tooltipText) throws Throwable {
		flag = false;
		try {

			if (isElementPresent(driver, identifyBy, locator1)) {
				Actions action = new Actions(driver);
				WebElement Element = driver.findElement(getLocator(identifyBy, locator1));
				action.moveToElement(Element).build().perform();
				Thread.sleep(5000);
				
				List<WebElement> Tooltip = driver.findElements(By.xpath(".//*[@class='plugin_tooltip']"));
				
				for(WebElement ttip:Tooltip)
				{
					System.out.println("TipValue -->" + ttip.getText());
					
					if (tooltipText.equals(ttip.getText())) {
						flag = true;
						break;
					}
					
					
				}
				
				
				

			}

		} catch (Exception e) {
			/*failureReport(driver, "Mouse Over To Exception Info :",
					"Unable to moveover on element with locator :" + locator2);
			logger.error("Unable to moveover on element with locator :" + locator2);*/
		}

		return flag;
	}

	public static  void openLinkFromElement(WebDriver driver,String identifyBy, String locator,String element) throws Throwable {
        try {
               //waitElementbeClickable(driver, driver.findElement(getLocator(identifyBy, locator)));
               WebElement el = driver.findElement(getLocator(identifyBy, locator));
               if (isElementPresent(driver,identifyBy, locator)) {
                     if(el.isEnabled()){
                            String link= el.getAttribute("href");
                            new Actions(driver)
                                   .moveToElement(el).build().perform();
                            driver.get(link);
                     }
               }
        } catch (Exception e) {
               failureReport(driver,"Open Link form Element Exception Info :",
                            "Unable to unable to open link from element with selector" + identifyBy
                            + " for element with locator " + locator);
               logger.error("Unable to unable to open link from element with selector" + identifyBy
                            + " for element with locator " + locator);

        }

 }

	public static  boolean verifyElementEnabled(WebDriver driver, String identifyBy, String locator) {


		Boolean isPresent = false;
		try {

			if (driver.findElement(getLocator(identifyBy, locator))
					.isDisplayed()) {
				highlight(driver.findElement(getLocator(identifyBy, locator)));
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)",
						driver.findElement(getLocator(identifyBy, locator)));
				
				isPresent = driver.findElement(getLocator(identifyBy, locator)).isEnabled();
			}

			return isPresent;
		} catch (Exception e) {
			/*logger.error("Unable to identify the element with locator : "
					+ locator);*/
			return isPresent;
		}
	}
 





}

	



