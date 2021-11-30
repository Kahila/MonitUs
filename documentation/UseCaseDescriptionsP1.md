# Use Case Descriptions Part 1: Login, Settings, and Registration subsystems

This section describes the use cases defined for Login, Settings, and Registration subsystems along with their respective activity diagrams.

## Use Case Descriptions Login

**Use case: Login** 

1. User must Input details on login form which include Username and password

2. MonitUs System will then validate the details 

3. MonitUs will check if the user exists in the database

4. MonitUs System will check the type of user 

5. MonitUs System retrieves the user from the database

6. MonitUs System will display the interface for that user

**Extensions**

Use case: Login
 
1a. User enter invalid details
   
1. MonitUs system  show error massege 

2. User can re-enter

2a. User doesn't exist in the database

1. MonitUs system show error massage

2. MonitUs display login page
 



![Login Activity diagram](/images/ActivityDiagrams/Login_activity_diagram.png)




## Use Case Descriptions Settings



**Case modify account**
 
 1. User select option setting
  
 2. MonitUs system will display a setting form

**Use Case: Change password**

1. User enter password and match password then submit
 
2. MonitUs system will validate the details
 
3. MonitUs system store the details in the database
  
4. MonitUs System will display massage updated

**Extensions**

Use case: Change password
 
1a. User entered passwords don't match 
  
1. MonitUs system display error message 

2. User can re-enter


![Setting Activity diagram](/images/ActivityDiagrams/Setting_activity_diagram.png)



## Use Case Descriptions Registration




**Use case:  Registration**

1.MonitUs System will get registration details which include username and temporary password from the school database

**Extensions**
 
 Use case: Registration
  
 1a. If a user is deleted from the school database
   
 1. MonitUs system get notification
 
 2. MonitUs System  delete the user on it database 





![Registration Activity diagram](/images/ActivityDiagrams/Register_activity_diagram.png)
