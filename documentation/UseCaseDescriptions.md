## Use Case Descriptions
Information to support the use case definition for the MonitUs system.


### Pre-condition
* The Monitus registration system will communicate with the Barnato Park High Schools' database:

   * get all the students, teachers, and receptionist information
   * allowing the registration system to create accounts for these actors(students, teachers, and receptionist)

*	The primary actor (Teacher, student, and receptionist) must have logged into the system by providing a valid username and password.


### Primary flow

* The Teacher will use the monitoring sub-system to view :
    *	the classes' results 
    *	their class's assignments that have been uploaded by the student.
    
* The student will use the monitoring sub-system to:
    *	check on their report cards
    *	view the teachers' comments 
    *	view their assessment results.
* The students' results are also sent to their parents through the emailing subsystem

* The teacher will be using the upload sub-system to:
    * set assessments
    * upload students' results and announcements.
* The student is only able to submit their assignments
* Receptionists are only able to upload newsletters.
* The Teacher, student, and receptionist can use the settings sub-system to:
    * modify their account details(change password)

* The MonitUs system gets information about the learners, teachers, and receptionists from the schools' database:
   *	enabling modification of user account by :
   *	deleting the account of users that are no longer active members of the school 
   *	changing the username of an actor.





## Basic path from the primary actors' point of view
### Teacher

*	The teacher will log in to the system by inputting their credentials:
    * This will then allow them access to the MonitUs system

*	Then they will move to the Upload system where:
    *	they can upload or create assignments
    *	Upload results for completed assessments

*	The teacher can then move to the Monitoring subsystem to view:
    *	assessment that they have set
    *	who has completed the assessments
    *	the performance of the class as a whole


### Student
*	The student will log in to the system by inputting their credentials to access the MonitUs monitoring sub-system:
    *	to view any newly uploaded assessments
    * to view assessments results
    * to view assessments feedback

*	The student can then move the Upload system :
    * to complete and submit assessments


## Alternate paths from the secondary actors' point of view


### Receptionist
*	The receptionist will log in by inputting their credentials then will have access to the upload sub-system where they can:
    *	where they will then upload the schools' newsletter.


### Post-conditions
The actor (Teacher, student, and receptionist) can log out of the system.
