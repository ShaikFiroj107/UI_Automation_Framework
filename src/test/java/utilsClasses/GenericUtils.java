package utilsClasses;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Random;
import io.restassured.specification.RequestSpecification;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GenericUtils {

	private static final Logger log = LogManager.getLogger(GenericUtils.class);
	public static RequestSpecification request;
	public WebDriver driver;
	public static String bearerToken;
	public String requestMethod;
	public String networkUrls;
	public int vehicleRequestId;
	public String responseBody;
	
	public GenericUtils(WebDriver driver) {
		this.driver = driver;
	}
	
	public void click(By xpathToClick) {
		driver.findElement(xpathToClick).click();
	}
	
	public String getValue(By xpathToGetText) {
		String value = driver.findElement(xpathToGetText).getText();
		System.out.println("text value is  :" + value);
		return value;
	}
	
	public void clear(By xpathToClear) {
		driver.findElement(xpathToClear).sendKeys(Keys.CONTROL + "a");
		driver.findElement(xpathToClear).sendKeys(Keys.DELETE);

	}
	
	 public void sendKeysToElement(By locator, String value) {
	        WebElement element = driver.findElement(locator);
	        element.clear();
	        element.sendKeys(value);
	    }
	
	public String getFutureOrPastDate(String dateformat, int numberOfDays) {
		DateFormat dateFormat = new SimpleDateFormat(dateformat);
		Calendar cal = Calendar.getInstance();
		log.info("adding " + numberOfDays + " to current date");
		cal.add(Calendar.DATE, numberOfDays);
		String updatedDate = dateFormat.format(cal.getTime());
		log.info("updated date is =" + " " + updatedDate);
		return updatedDate;
	}

	public static String getGlobalValue(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/configFile/global.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}
	
	public String storeCurrentDateAndTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String currentDate = df.format(new Date());
		return currentDate;
	}
	
	public String getRandomText() {
		String textChar = "abcdefghijklmnopqrstwxyz1234567890";
		StringBuilder text = new StringBuilder();
		Random rnd = new Random();
		while (text.length() < 15) { // length of the random string.
			int index = (int) (rnd.nextFloat() * textChar.length());
			text.append(textChar.charAt(index));
		}
		String randomText = text.toString();
		return randomText;
	}
	
	public void compareTwoIntegerValues(int actualInteger, int expectedInteger) {
		System.out.println("Verifying two integer values matching or not, actual value is = " + actualInteger + ",  API value is = " + expectedInteger);
		Assert.assertEquals(actualInteger, expectedInteger);
	}
	
	public void compareTwoStringValues(String actualString, String expectedString) {
		System.out.println("Verifying two String values matching or not, actual value is = " + actualString + ",  API value is = " + expectedString);
		Assert.assertEquals(actualString, expectedString);
	}

}
