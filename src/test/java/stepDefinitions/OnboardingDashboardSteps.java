package stepDefinitions;

import io.cucumber.java.en.*;
import pageobjects.OnboardingDashboardPage;
import utilsClasses.TestSetup;
import java.io.IOException;
import java.sql.SQLException;

public class OnboardingDashboardSteps {

	public TestSetup testSetup;// to make testContextSetup instance globally available to this class
	public OnboardingDashboardPage onboardingDashboardPage;

	public OnboardingDashboardSteps() {}
	
	public OnboardingDashboardSteps(TestSetup testSetup) {
		this.testSetup = testSetup;
		this.onboardingDashboardPage = testSetup.pageObjectManager.getOnboardingDashboardPage(testSetup);
	}
	
	@Given("get network https response")
	public void get_network_https_response() throws IOException {
		onboardingDashboardPage.getNetworkResponse();
	}
	
	@When("User is on TMS page")
	public void User_is_on_TMS_page() throws IOException {
		onboardingDashboardPage.opensTMSPage();
	}
	
	@When("get credentials of {string} from sheet {string}")
	public void one(String userName, String SheetName) {
		onboardingDashboardPage.readExcelData(userName, SheetName);
	} 
	
	@Then("user enter username")
	public void user_enter_username() {
		onboardingDashboardPage.enterUserName();
	}
	
	@And("user enter password")
	public void user_enter_password() {
		onboardingDashboardPage.enterPassword();
	}
	
	@Then("Click on submit button")
	public void click_on_submit_button() {
		onboardingDashboardPage.clickSubmitButton();
	}

	@Then("verify user is successfully logged in TMS")
	public void verify_user_is_successfully_logged_in_TMS() throws InterruptedException {
		onboardingDashboardPage.verifyUserLoggedIn();
	}
	
	@When("Click {string} radio button")
	public void click_radio_button(String VehicleCategory) {
		onboardingDashboardPage.clickVehicleCategory(VehicleCategory);
	}
	
	@Then("Choose Select Supplier dropdown value with {string}")
	public void choose_select_supplier_dropdown_value_with(String SelectSupplier) {
		onboardingDashboardPage.selectSupplierValue(SelectSupplier);
	}

	@Then("Select Vehicle Type dropdown value with {string}")
	public void select_vehicle_type_dropdown_value_with(String VehicleType) {
		onboardingDashboardPage.selectVehicleType(VehicleType);
	}
	
	@Then("Enter vehicle model name value")
	public void enter_vehicle_model_name_value() {
		onboardingDashboardPage.sendModelNameValue();
	}

	@Then("Select current date from date required by calander")
	public void select_current_date_from_date_required_by_calander() {
		onboardingDashboardPage.selectCurrentDate();
	}

	@Then("Enter Specification value in specification box")
	public void enter_specification_value_in_specification_box() {
		onboardingDashboardPage.sendSpecificationValue();
	}
	
	@Then("verify successfully new vehicle request created or not")
	public void verify_successfully_new_vehicle_request_created_or_not() {
		onboardingDashboardPage.verifyNewVehicleRequest();
	}
	
	@Then("get DB connection")
	public void get_DB_connection() throws SQLException, IOException {
		onboardingDashboardPage.getConnection();
	}
	
	@Then("delete latest created vehicle request id from DB")
	public void delete_latest_created_vehicle_request_id_from_DB() throws SQLException {
		onboardingDashboardPage.deleteVehicleRequestRecords();
	}

}
