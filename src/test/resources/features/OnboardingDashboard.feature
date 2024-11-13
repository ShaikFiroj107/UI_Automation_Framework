Feature: Validating Onbarding dashboard UI test cases
Scenario: Verify user can able to login in TMS Onboarding dashboard
	Given User is on TMS page
	When get credentials of "Firoj" from sheet "cred"
  Then user enter username
  And user enter password
  Then Click on submit button
  Then verify user is successfully logged in TMS
  
Scenario Outline: Verify user can able to create new vehicle request or not
	Given get network https response
	When User is on TMS page
	When get credentials of "Firoj" from sheet "cred"
  Then user enter username
  And user enter password
  Then Click on submit button
  Then verify user is successfully logged in TMS
  When Click "<VehicleCategory>" radio button
  Then Choose Select Supplier dropdown value with "<SelectSupplier>"
  And Select Vehicle Type dropdown value with "<VehicleType>"
  And Enter vehicle model name value
  Then Select current date from date required by calander
  And Enter Specification value in specification box
  Then Click on submit button
  Then verify successfully new vehicle request created or not
  Then get DB connection
  Then delete latest created vehicle request id from DB
 
	Examples:
	|VehicleCategory|SelectSupplier|VehicleType |
	|Trailer        |Renault       |Roller Crane|
	|Unit           |SDC           |Urban       |