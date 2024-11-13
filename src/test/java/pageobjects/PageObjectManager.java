package pageobjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import utilsClasses.TestSetup;

public class PageObjectManager {

	public WebDriver driver;
	public OnboardingDashboardPage onboardingDashboardPage;
	private static final Logger log = LogManager.getLogger(PageObjectManager.class);

	public PageObjectManager(WebDriver driver) {
		this.driver = driver;
	}

	public OnboardingDashboardPage getOnboardingDashboardPage(TestSetup testSetup) {
		if (onboardingDashboardPage == null) {
			onboardingDashboardPage = new OnboardingDashboardPage(driver, testSetup);
		}
		return onboardingDashboardPage;
	}

}
