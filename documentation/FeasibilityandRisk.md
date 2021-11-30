# Feasibility and Risk Study

This document covers the system's technical, economical and
operational feasibility. 

## System feasibility
* Similarities with existing systems

A current system that exists and is used frequently by some schools is **d6 Communicator**. This application has close similarities with some of the systems and functionality we intend to implement into our solution for Barnato Park High School. 

As the d6 schools webpage says: 
> d6 Connect Application was developed primarily to address communications shortcomings and now enable both integrations with the entire d6+ product suite (admin, curriculum, finance, communication and cashless) and also allow schools to create private channels with more controlled access to the information they communicate. 

d6 Communicator has been designed to accommodate multiple schools rather than built for a specific school. So, different schools can register on the application and utilise its services for multiple uses such as facilitate transactions made on school premises, allowing homework uploads per subject, give parents feeds for reading notices and responding to them as well as providing student/parent profiles and specific learner information (d6 Connect 2021).

Our system may have similarities with the functionality specific to student profiles, learner information, feeds and homework creation and linkage. As d6 is not designed for a specific school and our system aims to solve a specific problem of Barnato Park High, monitoring student progress and reporting on that progress, we intend to have visible differences in how our system handles student profiles, information, school feeds(newsletters and reports to parents) and managing homework and assignment creation and upload.

## Teachnical feasibility
* Hardware and software requirements

The system will be implemented on the web as a website/web portal along with a companion app and are both useable and accessible by students and teachers. The system will need to support most browsers and mobile android phones screen sizes, allowing for quick access, intuitive design, easy to understand UI and provide a good user experience.

Most software implementations will need to adhere to our requirements to provide quick use(log in, loading student data, generating reports) and support a large number of users(atleast 950). This lends to the need for using a DBMS with easy to use and fast query generation, having a normalised database for quick record retrieval and an equally fast API.

Hardware concerns are mainly on the mobile app as it will need to be supported for a vast majority of mobile phones with varying screen sizes and performance. 
As highlighted by the Which? article in 2020:
>The current software version is Android 10 while Android 9 (aka Android Pie) and Android 8 (Android Oreo) are still in theory getting security updates too. Using anything below Android 8 will carry security risks.

The system should at least be optimized for android 8.0, this is the oldest android version still recieving crucial OS security updates from google (which? 2020). This should allow us to cover most phones still recieving security patches in the current market without leaving users vulnerable to having their information possibly compromised. 720x1280 is a commonly used resolution for most current mobile phones (Piejko 2016). So, the system needs to support this resolution and newer.

## Economical feasibility
* Affordability of the system

Students and teachers will be using the system and it needs to work on the internet(a constant internet connection is required). As specified in the Technical feasibilty, we need to accommodate various phones and as students may not have a dedicated internet connection the system not only has to be platform independent but also not consume too much bandwidth.

So the system needs to be affordable both in bandwidth usage and performance in mobile phones. High bandwidth usage can be mitigated with less visual elements on the user interface or less graphically taxing objects (3d objects, high quality video,audio and images).

Increased affordability on mobile performance, by optimizing on lower end devices and providing a portable user experience maybe not as robust as the web app but providing the fundamental and core functionality to the user.

Currently the system is mainly created for solving the problem of students managing task progress and teachers distributing the tasks, and report generation of the tasks all while adhering to covid regulations. The system will need to be designed and function in a way that allows future use and provides further enhancement to the current school tracking system even without the school conducting socially distant lessons.

Giving the student and teacher a dashboard portal that displays progress towards a task for students and reporting statistics about the class' general performance and understanding of a task and topic to the teacher can supplement learning and feedback transactions between teacher and student. 

The Inherit value added by the system of report generation needs to give both teacher and student a visual of how well a student's own progress in the curriculum is changing over time and a teacher can evaluate their students in a 'big picture' of the progress of specific tasks while also being able to evaluate each student one-on-one.

## Operational feasibility
* Human factor

The system will be operated mainly by teenage students (~age 13 to 18) teachers as well as an administrator in the age range from 22+. The system therefore has to accomdate multiple people from different age ranges and technical ability.

Having an interface that is simple to understand, navigate and manage by the teacher and admin is important because we intend for the system to have continous usage even after the covid pandemic. Clear, concise and easy to read text should lead users to working with the system in a efficient way.

Students may not all have a lot of experience with a technical school portal for homework submission and reporting software. So, the system needs to be user friendly to all users so as to have a unified understanding and accommodate users of different technical skills. Having clear and simple instructions for report generation, homework submission etc. for both teacher and student should supplement the user's understanding of how the system functions.

References:

d6 Connect 2021, viewed 20 March 2021, <https://d6.co.za/education/products/d6-plus/d6-connect/>

Piejko, P 2016, *the most common mobile screen resolution in Q3 2016 (new report)*, viewed 20 March 2021 <https://mobiforge.com/news-comment/720x1280-is-the-most-common-mobile-screen-resolution-in-q3-2016-new-report>

Which? Press Office 2021, viewed 20 March 2021, <https://press.which.co.uk/whichpressreleases/void-android-more-than-one-billion-android-devices-at-risk-of-hacking-attacks/>
