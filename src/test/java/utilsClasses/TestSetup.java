package utilsClasses;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import pageobjects.PageObjectManager;

public class TestSetup {
	public WebDriver driver;

	public PageObjectManager pageObjectManager;
	public TestBase testBase;
	public GenericUtils genericUtils;
	private static final Logger log = LogManager.getLogger(TestSetup.class);

	public TestSetup() throws IOException, InterruptedException {
		testBase = new TestBase();
		pageObjectManager = new PageObjectManager(testBase.WebDriverManager());
		genericUtils = new GenericUtils(testBase.WebDriverManager());
	}

}
