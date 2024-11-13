package utilsClasses;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {
	private static final Logger log = LogManager.getLogger(TestBase.class);
	public  WebDriver driver;
	public GenericUtils genericUtils;
	public static String downloadPath;

	public  WebDriver WebDriverManager() throws IOException, InterruptedException {

		String browser = (genericUtils.getGlobalValue("browser"));
		downloadPath = System.getProperty("user.dir");
		if (driver == null) {
			if (browser.equalsIgnoreCase("chrome")) {
//				WebDriverManager.chromedriver().setup();
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", downloadPath);
				ChromeOptions options = new ChromeOptions();
				chromePrefs.put("profile.default_content_setting_values.media_stream_mic", 2);
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setCapability(ChromeOptions.CAPABILITY, options);
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION			
//				options.addArguments("--headless");
				options.addArguments("--window-size=1920,1080");
				options.addArguments("--disable-gpu");
				options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--disable-extensions");
				options.setExperimentalOption("useAutomationExtension", false);
				options.addArguments("start-maximized"); // open Browser in maximized mode
				options.addArguments("disable-infobars"); // disabling infobars
				options.addArguments("disable-media-stream");
				options.addArguments("--proxy-server='direct://'");
				options.addArguments("--proxy-bypass-list=*");
				options.addArguments("--window-size=1920,1080");
				options.addArguments("--start-maximized");
				options.addArguments("--incognito");
				options.addArguments("--remote-allow-origins=*");
				options.merge(cap);

				driver = new ChromeDriver(options);
				driver.manage().window().maximize();
				driver.manage().deleteAllCookies();
				driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(50));
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
			}
			else if (browser.equalsIgnoreCase("firefox")) {
				WebDriverManager.firefoxdriver().setup();
			}
		}

		return driver;
	}
}