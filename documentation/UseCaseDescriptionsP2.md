# Use Cases Descriptions Part 2: Emailing, Monitoring, and Uploading subsystems

This section describes the use cases defined for Emailing, Monitoring, and Uploading subsystems along with their respective activity diagrams.

## E-mailing Subsystem 

**Use case: Send email parent**

1.MonitUs system gets parent's email of each student from the school database

2.MonitUs system generates a report card of each student with teacher feedback and academic results and sends a report to the parent email


**Use case: Send Report Card**

1.MonitUs system gets student's assessment marks from database to create report card displaying student's marks

2.MonitUs system adds academic results to report card 

3.MonitUs system uses parent email of the student to send academic results to the parent

**Use case: Send Teachers' Feedback**

1.MonitUs system gets teacher's comment left on student assessments from the internal database 

2.MonitUs system adds teacher's comment on the report card and sends to parent email

**Use case: Send School Newsletters**

1.MonitUs system checks for any newsletters/notices that have been created by the receptionist 

2.MonitUs gets notices and newsletters from the internal database and sends them to the parent email

**Extensions**

Use case: Send an email to the parent

1a. No email exists for the parent

1. MonitUs uses an alternative method of communication
2. MonitUs sends an SMS notification


Use case: Send Teachers' Feedback

1a. Teacher comment does not exist

1. MonitUs system uses default comment "No teacher comment"

2a. Teacher comment not found

1. MonitUs adds the comment "No teacher comment" on the report card


Use case: Send School Newsletters

1a. School notices/newsletters do not exist

1. MonitUs does not send any notices/newsletters to the parent email 





![E-mailing Activity diagram](/images/ActivityDiagrams/E-mailing/E-mailingActivityDiagram.png)


## Monitoring Subsystem 

**Use case: View class results**

1.Teacher logins into the system by inputting username and password

2.Login subsystem validates entered credentials with teacher's credentials stored in the internal database

3.Teacher selects the option to view class results of particular assignments

4.Monitoring subsystem retrieves results from completed assignments in the internal database

5.Monitoring subsystem calculates class average mark of all completed and marked class assignments

6.Monitoring system displays cumulative results of selected assignment

**Extensions**

2a. Invalid credentials entered

1. login system prompts the teacher to renter values again
2. teacher can successfully login





![View Class Results Activity diagram](/images/ActivityDiagrams/Monitoring/ViewClassResults.png)

**Use case: View Class assignments**

1.Teacher selects the option to view current class assignments 

2.Monitoring system displays subject with currently open assignments and closed assignments

3.Teacher selects subject to view all submitted assignments and associated assignment document for a particular subject

4.Monitoring subsystem retrieves all submitted assignments from the internal database

5.Monitoring subsystem displays student-submitted assignments and assignment document

![View Class Assignments Activity diagram](/images/ActivityDiagrams/Monitoring/ViewClassAssignments.png)

**Use case: View newsletter**

1.Teacher and student login by inputting credentials

2.Login subsystem validates credentials with credentials in the internal database

3.Teacher and student select option to view newsletters and school notices

4.Monitoring subsystem retrieves all newest newsletters and notices from the internal database 

5.Teacher or student selects notice to display

6.Monitoring subsystem displays detailed information of the newsletters and notices 

**Extensions**

2a. Invalid credentials entered

1. login system prompts the user to renter values again
2. user can successfully login





![View Newsletter Activity diagram](/images/ActivityDiagrams/Monitoring/ViewNewsletter.png)

**Use case: Track progress**

1.Student inputs username and password to log in

2.Login subsystem validates credentials with credentials of student in the internal database

3.Monitoring system displays options to view report card, view teachers feedback, view assessment results

**Extensions**

2a. Invalid credentials entered

1. login system prompts the user to renter values again
2. student can successfully login





![Track Progress Activity diagram](/images/ActivityDiagrams/Monitoring/TrackProgress.png)

**Use case: View report card**

1.Student selects view report card

2.Monitoring subsystem retrieves class assignment information and marks for each subject from the internal database

3.Monitoring subsystem displays report card information including average mark per subject and teacher feedback





![View Report card Activity diagram](/images/ActivityDiagrams/Monitoring/ViewReportCard.png)

**Use case: View teacher's feedback**

1.Student selects view teacher feedback

2.Monitoring subsystem retrieves any existing teacher feedback for a student in a specific subject from the database

3.Monitoring subsystem displays all existing teacher feedback of student

**Extensions**

3a. No teacher feedback
1. Monitoring system displays "No teacher comment"





![ViewTeacherFeedback Activity diagram](/images/ActivityDiagrams/Monitoring/ViewTeacherFeedback.png)

**Use case: View assessment results**

1.Student selects view assessment results

2.Monitoring system prompts the user to select a specific subject

3.Monitoring subsystem retrieves results of completed assessments from database 

4.Monitoring subsystem displays results of all completed assessments of the student





![View assessment results Activity diagram](/images/ActivityDiagrams/Monitoring/ViewAssessmentResults.png)


## Upload Subsystem

**Use case: Upload**

1.Teacher login into MonitUS system

2.Login subsystem validate username and password

3.MonitUs system displays the class dashboard

4.Teacher selects upload option

5.Upload system displays options for uploading PDF assignment, create an assignment, upload assignment results, and upload class announcements

**Extensions**

2a. Invalid login information

1. prompt teacher to reenter credentials
2. teacher can log in successfully





![Upload Activity diagram](/images/ActivityDiagrams/Upload/Upload.png)

**Use case: Upload PDF assignment**

1.Teacher selects upload PDF assignment

2.Upload subsystem asks the teacher to select a subject to upload to

3.Teacher selects subject to upload to

4.Upload subsystem displays form for the teacher to enter assignment details

5.Teacher enters details of the assignment and uploads actual PDF document

6.Upload system saves and stores the document in the internal database

7.Upload system notifies students in the subject of a new assignment

**Extensions**

5a. Teacher cancels the request

1. Return teacher to upload page





![Upload PDF Assignment Activity diagram](/images/ActivityDiagrams/Upload/UploadPDFAssignment.png)

**Use case: create assignment**

1.Teacher selects the option to create an assignment

2.Upload subsystem prompts the teacher to select a subject to create an assignment for

3.Upload subsystem displays interface for creating an assignment 

4.Teacher inputs all relevant information for the assignment

5.Upload assignment saves assignment and stores into the internal database

6.Upload system notifies students in the subject of a new assignment

**Extensions**

3a. Teacher cancels the creation

1. System returns teacher to the teacher dashboard

***
The activity diagram of the "create assignment" use case is identical to the activity diagram of the use case "upload PDF assignment", it is this reason it is not modeled here. refer to the Upload PDF document diagram for more

***

**Use case: Upload assignment results**

1.Teacher selects subject assignment to modify marks for

2.Upload system displays marks of submitted assignments

3.Teacher modifies marks 

4.Teacher uploads marks 

5.Upload system validates modified marks

6.Upload system stores modified marks in the internal database

**Extensions**

2.a Teacher cancels the operation

1. System returns teacher to the class interface

5a. Marks are invalid

1. Upload system prompts the user to renter marks

2. stores correctly entered marks





![Upload Assignment Results Activity diagram](/images/ActivityDiagrams/Upload/UploadAssignmentResults.png)

**Use case: Upload announcements**

1.Teacher selects the option to create an announcement

2.Upload system asks the teacher to select a subject for the directed announcement

3.Teacher selects subject

4.Upload system displays a form for creating an announcement

5.Teacher inputs relevant information of announcement

6.Upload system saves and stores announcement in the internal database and notifies students in the subject





![Upload Announcements Activity diagram](/images/ActivityDiagrams/Upload/UploadAnnouncements.png)

**Use case: Submit**

1.Student login into MonitUs system

2.login subsystem validates login credentials

3.MonitUs system displays student dashboard

4.Student selects submit option for an assignment

5.Upload system prompts the student to upload a file

6.Student uploads files

7.Upload system validates file type and saves the file to database

8.Upload system notifies the student of successful upload

**Extensions**

6a. student cancels upload

1. return the student to the student portal

7a. invalid file uploaded

1. prompt student to reupload appropriate file type

2. system saves the file to the database





![Submit Activity diagram](/images/ActivityDiagrams/Upload/Submit.png)

**Use case: Upload newsletters**

1.Receptionist logins in with credentials

2.Login subsystem validates credentials

3.MonitUs system displays receptionist dashboard

4.Receptionist selects the option to upload newsletter

5.Upload subsystem displays form to create a newsletter

6.Receptionist enter the relevant information into the form

7.Upload subsystem saves created newsletter to the internal database

8.Upload system sends a newsletter to email subsystem to send to parents and students

**Extensions**

5a. Receptionist cancels the upload

1. System returns receptionist to receptionist dashboard

***
The activity diagram of the "Upload newsletters" use case is identical to the activity diagram of the use case "upload PDF document", it is this reason it is not modeled here. refer to the Upload PDF document diagram for more

***
