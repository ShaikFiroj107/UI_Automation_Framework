package pageobjects;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.junit.Assert;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.network.Network;
import org.openqa.selenium.devtools.v119.network.model.Request;
import org.openqa.selenium.devtools.v119.network.model.RequestId;
import org.openqa.selenium.devtools.v119.network.model.Response;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import utilsClasses.GenericUtils;
import utilsClasses.TestSetup;

public class OnboardingDashboardPage {
	
	RequestSpecification res;
	ResponseSpecification resspec;
	Response response;
	JsonPath jsonPath;
	public static RequestSpecification request;
	public static String storedBearerToken;
	static String place_id;
	private final Logger log = LogManager.getLogger(OnboardingDashboardPage.class);
	private static GenericUtils genericUtils;
	public WebDriver driver;
	private TestSetup testSetup;
	private String userID;
	private String password;
	public String networkUrl;
	public String requestMethod;
	private static Statement stmt;
	public static Connection con;
	public int vehicleRequestId;
	
	public OnboardingDashboardPage(WebDriver driver, TestSetup testSetup) {
		this.testSetup = testSetup;
		this.driver = driver;
		this.genericUtils = testSetup.genericUtils;
	}
	
	private By userNameField = By.xpath("//input[@id='username']");
	private By passwordField = By.xpath("//input[@id='password']");
	private By submitButton = By.xpath("//*[text()='Submit']");
	private By verifyLoginPage = By.xpath("//ul[@class='customCheckBox']");
	private By supplierDropdown = By.xpath("//span[text()='Select Supplier']/preceding::input[@class='ant-select-selection-search-input'][1]");
	private By vehicleTypeDropdown = By.xpath("//span[text()='Select Vehicle Type']/preceding::input[@class='ant-select-selection-search-input'][1]");
	private By modelNameField = By.xpath("//input[@placeholder='Enter Model Number']");
	private By selectDateFiled = By.xpath("//input[@placeholder='Select Date']");
	private By selectTodayOption = By.xpath("//a[text()='Today']");
	private By specificationField = By.xpath("//textarea[@placeholder='Any Special Requirements']");
	private By thankyouMessage = By.xpath("//h2[text()='Thank you!']");
	

	public void getNetworkResponse() {
		log.debug("Using CDP tools getting network request and response body");
		DevTools devTools = ((ChromeDriver)driver).getDevTools();
			devTools.createSession();
			log.debug("Enable network session");
			devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
			log.debug("Adding Listeners to send request");
			devTools.addListener(Network.requestWillBeSent(), requestConsumer ->{
				Request networkRequest = requestConsumer.getRequest();
			        requestMethod = networkRequest.getMethod();
			   });
			
			log.debug("Storing requestId value");
			final RequestId[] requestId = new RequestId[1];
			log.debug("Adding Listeners for getting response body");
			devTools.addListener(Network.responseReceived(), responseConsumer -> {
			    Response networkResponse = responseConsumer.getResponse();
			    requestId[0] = responseConsumer.getRequestId();
			    String networkResponseUrl = networkResponse.getUrl();
			    int getStatus = networkResponse.getStatus();
			    if(networkResponseUrl.equals("https://development.tms.api.bjshomedelivery.com/vehicles/requests/") && "POST".equalsIgnoreCase(requestMethod)) {
			    	Assert.assertEquals(getStatus, 200);
			    	String responseBody = devTools.send(Network.getResponseBody(requestId[0] )).getBody();
			    	log.info("Successfully getted response body is :" +responseBody);
			    	
			    	   try {
			    		   log.debug("Storing vehicle request id from response");
			                ObjectMapper objectMapper = new ObjectMapper();
			                JsonNode rootNode = objectMapper.readTree(responseBody);
			                JsonNode dataNode = rootNode.path("data");
			                if (dataNode.isArray() && dataNode.size() > 0) {
			                    JsonNode firstItem = dataNode.get(0);
			                    vehicleRequestId = firstItem.path("id").asInt();
			                    log.info("Extracted ID: " + vehicleRequestId);
			                }
			            } catch (Exception e) {
			                e.printStackTrace();
			            }
			    }
			});
	}
	
	public void opensTMSPage() throws IOException {
		log.debug("opening TMS application on browser");
		driver.get(genericUtils.getGlobalValue("url"));
		log.info("Successfully sented url is = " + genericUtils.getGlobalValue("url"));
	}
	
	public void readExcelData(String user, String SheetName) {
		log.debug("Reading credentials from ODS file");
		 try {
			 log.debug("Looking for spread sheet document path");
	            SpreadsheetDocument sheet = SpreadsheetDocument.loadDocument(new File(System.getProperty("user.dir") + "/TMS_Credentials.ods"));
	            log.debug("Getting first spread sheet data values");
	            Table table = sheet.getTableByName(SheetName);
//	            Table table = sheet.getTableList().get(0); // Get the first table throw index value
	            log.debug("Iterate through rows to find the user");
	            for (int row = 1; row < table.getRowCount(); row++) { // Start from row 1 to skip header
	                String currentUserName = table.getCellByPosition(0, row).getStringValue();
	                log.debug("Checking if the current user matches the provided user name");
	                if (currentUserName.equalsIgnoreCase(user)) {
	                	userID = table.getCellByPosition(1, row).getStringValue();
	                	log.info("User ID: " + userID);
	                    password = table.getCellByPosition(2, row).getStringValue();
	                    log.info("Password: " + password);
	                    return;
	                }
	            }
	            log.info("User not found: " + user);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	public void enterUserName() {
		log.debug("Sending User name in username text field");
		genericUtils.sendKeysToElement(userNameField, userID);
		log.info("Successfully sended username value is :"+userID);
	}
	
	public void enterPassword() {
		log.debug("Sending password value in password text field");
		genericUtils.sendKeysToElement(passwordField, password);
		log.info("Successfully sended password value is :"+password);
	}
	
	public void clickSubmitButton() {
		log.debug("Clicking on submit botton");
		genericUtils.click(submitButton);
		log.info("Successfully clicked on submit button");
	}
	
	public void verifyUserLoggedIn() throws InterruptedException {
		log.debug("Verifying the User Logged in TMS Dashboard or not");
		new WebDriverWait(driver, Duration.ofSeconds(50))
				.until(ExpectedConditions.visibilityOfElementLocated(verifyLoginPage));
		boolean verifyNewRequestDashboard = driver.findElement(verifyLoginPage).isDisplayed();
		Assert.assertTrue(verifyNewRequestDashboard);
		log.info("Successfully user logged in TMS Onboarding dashboard");
	}
	
	public void clickVehicleCategory(String VehicleCategory) {
		log.debug("Clicking on vehicle Category radio button");
		new WebDriverWait(driver, Duration.ofSeconds(10))
		.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='" + VehicleCategory + "']/preceding::input[@type='radio']")));
		genericUtils.click(By.xpath("//span[text()='" + VehicleCategory + "']/preceding::input[@type='radio']"));
		log.info("Successfully "+VehicleCategory+" radio button selected");
	}
	
	public void selectSupplierValue(String SelectSupplier) {
		log.debug("Selecting Supplier drop down value");
		genericUtils.click(supplierDropdown);
		genericUtils.click(By.xpath("//div[text()='"+SelectSupplier+"']"));
		log.info("Successfully selected "+SelectSupplier+" supplier value");
	}
	
	public void selectVehicleType(String VehicleType) {
		log.debug("Selecting vehicle type drop down value");
		genericUtils.click(vehicleTypeDropdown);
		genericUtils.click(By.xpath("//div[@title='"+VehicleType+"']"));
		log.info("Successfully selected "+VehicleType+" vehicle type value");
	}
	
	public void sendModelNameValue() {
		log.debug("Sending model name value");
		genericUtils.clear(modelNameField);
		String modelName = genericUtils.getRandomText();
		driver.findElement(modelNameField).sendKeys(modelName);
		log.info("Successfully entered model name is :"+modelName);
	}
	
	public void selectCurrentDate() {
		log.debug("Selecting required date from calander");	
		genericUtils.click(selectDateFiled);
		new WebDriverWait(driver, Duration.ofSeconds(05))
		.until(ExpectedConditions.elementToBeClickable(selectTodayOption));
		genericUtils.click(selectTodayOption);
		log.info("Successfully Selected Current date");
	}
	
	public void sendSpecificationValue() {
		log.debug("Entering Specification value text");
		genericUtils.clear(specificationField);
		String specificatioValue = genericUtils.getRandomText();
		driver.findElement(specificationField).sendKeys(specificatioValue);
		log.info("Successfully enterd specification value is :"+specificatioValue);
	}
	
	public void verifyNewVehicleRequest() {
		log.debug("Validating with new vehicle request successfully created or not");
		new WebDriverWait(driver, Duration.ofSeconds(15))
		.until(ExpectedConditions.visibilityOfElementLocated(thankyouMessage));
		boolean verifyCreateRequest = driver.findElement(thankyouMessage).isDisplayed();
		Assert.assertTrue(verifyCreateRequest);
		log.info("Successfully new vehicle request created");
	}
		
	public void getConnection() throws SQLException, IOException {
		log.debug("Getting TMS DB connection");
		String url = genericUtils.getGlobalValue("sqlURL");
		String userName = genericUtils.getGlobalValue("sqlUserName");
		String pwd = genericUtils.getGlobalValue("sqlPassword");
		con = DriverManager.getConnection(url, userName, pwd);
		log.info("Successfully TMS sql DB connection setted with credentials");
		con.setAutoCommit(false);
	}
	
		
	public void deleteVehicleRequestRecords() throws SQLException {
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;

		try {
			log.debug("Deleting Vehicle requests id foreign key constraints in VehicleRequestStatus table.");
			String vehicleRequestStatusQuery = "DELETE FROM VehicleRequestStatus WHERE Vehicle_request_details_id IN (SELECT id FROM VehicleRequestDetails WHERE Vehicle_request_id = ?)";
			pstmt1 = con.prepareStatement(vehicleRequestStatusQuery);
			pstmt1.setInt(1, vehicleRequestId);
			pstmt1.executeUpdate();
			log.info("Successfully Vehicle Request Status table vehicle request id related data deleted");

			log.debug("Deleting Vehicle requests id foreign key constraints in VehicleRequestDetails table.");
			String vehicleRequestDetailsQuery = "DELETE FROM VehicleRequestDetails WHERE Vehicle_request_id = ?";
			pstmt2 = con.prepareStatement(vehicleRequestDetailsQuery);
			pstmt2.setInt(1, vehicleRequestId);
			pstmt2.executeUpdate();
			log.info("Successfully Vehicle Request Details table vehicle request id related data deleted");

			log.debug("Deleting VehicleRequest table data related with vehicle request id");
			String VehicleRequestQuery = "DELETE FROM VehicleRequests WHERE id = ?";
			pstmt3 = con.prepareStatement(VehicleRequestQuery);
			pstmt3.setInt(1, vehicleRequestId);
			pstmt3.executeUpdate();
			log.info("Successfully Vehicle Requests table vehicle request id data deleted");

			log.debug("Doing vehicle Request commit transaction");
			con.commit();
			log.info("Vehicle request table records deleted successfully");

		} catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback(); // Rollback in case of error
				} catch (SQLException rollbackEx) {
					rollbackEx.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			log.debug("Closing resources in finally method");
			try {
				if (pstmt1 != null)
					pstmt1.close();
				if (pstmt2 != null)
					pstmt2.close();
				if (pstmt3 != null)
					pstmt3.close();
				if (con != null)
					con.close();
			} catch (SQLException closeEx) {
				closeEx.printStackTrace();
			}
		}
	}
}
