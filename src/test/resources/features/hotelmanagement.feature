Feature: Hotel Management Platform features

  Scenario: Create event
    Given user have hotel management application
    And user login with admin and password
    When create an entry
      |hotelName|address|owner|phoneNumber|email|
      |Test Hotel|London|Vijay|0765432899|test@hotmail.com|
    Then entry should be added successfully

  Scenario: Delete event
    Given user have hotel management application
    And user login with admin and password
    When user delete an entry
      |hotelName|address|owner|phoneNumber|email|
      |Ramada |Hounslow|John|0712222233|john.test@ramada.com|
    Then entry should be deleted successfully

  Scenario: Create multiple events
    Given user have hotel management application
    And user login with admin and password
    When create an entry
      |hotelName|address|owner|phoneNumber|email|
      |Holidays Inn|Bristol|James|0722334455|james@test.com|
      |Hotel Random|Southampton|Scott|0764463636|scott.test@hotmail.com|
    Then entry should be added successfully