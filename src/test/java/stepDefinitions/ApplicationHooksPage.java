package stepDefinitions;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import utilsClasses.TestSetup;

public class ApplicationHooksPage {
	public TestSetup testSetup;
	public WebDriver driver;

	private static final Logger log = LogManager.getLogger(ApplicationHooksPage.class);

	public ApplicationHooksPage(TestSetup testSetup) throws IOException, InterruptedException {
		this.driver = testSetup.testBase.WebDriverManager();
		this.testSetup = testSetup;
	}

	@After()
	public void AfterScenario(Scenario scenario) {
		driver.quit();
	}

	@AfterStep
	public void addScreenShot(Scenario scenario) throws IOException, InterruptedException {
		if (scenario.isFailed()) {
			log.debug("Taking failure page screenshots");
			File sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			byte[] fileContent = FileUtils.readFileToByteArray(sourcePath);
			scenario.attach(fileContent, "image/png", "image");
		}
	}
}
