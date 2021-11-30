const database = require("./connect");
const chalk = require('chalk');
//const users = require("./json/users.json"); //json file containing sudo applicants
const bcrypt = require('bcrypt');
const log = console.log;

database.connect().connect(function(err){
    if (err) throw (err);
    console.log("Connection to server success");

    // dropping any existing database
    drop = "DROP DATABASE monitUS";
    database.connect().query(drop, function(err, result){
        if (err) // database does not exist
            (log(chalk.red("Droping Database -> failed:\n\tCause : Database Does Not Exist.")))
        else
            log(chalk.greenBright("Droping Database -> success: ..."));
    })

    // creating a new database

    createDatabase = "CREATE DATABASE monitUS";
    database.connect().query(createDatabase, function(err, result) {
        if (err)
        (log(chalk.red("Database Creation -> fail")));
        else {
            log(chalk.greenBright("Database Creation -> success"));
            
            //creating new tables
            log("Tables:");
            
            // creating every user related tables -----------Start
            // teacher table
            teachersTable = `CREATE TABLE monitus.teacher (
                id INT PRIMARY KEY AUTO_INCREMENT, 
                username VARCHAR(255) not null,
                name VARCHAR(255) not null,
                surname VARCHAR(255) not null,
                password VARCHAR(255) not null,
                email VARCHAR(255) not null,
                id_number VARCHAR(255) not null,
                active BOOLEAN not null,
                rank INT not null
           );`;
            database.connect().query(teachersTable, function(err, result) {
                if (err) (log(chalk.red("\tteachersTable -> fail")));
                else (log(chalk.greenBright("\tteachersTable -> success")));
            });

            // students table
            studentsTable = `CREATE TABLE monitus.student(
                id INT PRIMARY KEY AUTO_INCREMENT,
                grade_id INT not null,
                username VARCHAR(255) not null,
                name VARCHAR(255) not null,
                surname VARCHAR(255) not null,
                password VARCHAR(255) not null,
                email VARCHAR(255) not null,
                id_number VARCHAR(255) not null,
                active BOOLEAN not null,
                rank INT not null
            );`;
            database.connect().query(studentsTable, function(err, result) {
                if (err) (log(chalk.red("\tstudentsTable -> fail")));
                else (log(chalk.greenBright("\tstudentsTable -> success")));
            });

            // receptionistTable
            receptionistTable= `CREATE TABLE monitus.receptionist (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(255) not null,
                name VARCHAR(255) not null,
                surname VARCHAR(255) not null,
                password VARCHAR(255) not null,
                email VARCHAR(255) not null,
                id_number VARCHAR(255) not null,
                active BOOLEAN not null,
                rank INT not null
            );`;
            database.connect().query(receptionistTable, function(err, result) {
                if (err) (log(chalk.red("\treceptionistTable -> fail")));
                else (log(chalk.greenBright("\treceptionistTable -> success")));
            });
            // creating every user related tables ----------- End

            // newsletterTable
            newsletterTable = `CREATE TABLE monitus.newsletters (
                news_id INT PRIMARY KEY AUTO_INCREMENT,
                headline VARCHAR(255) not null,
                message VARCHAR(255) not null,
                date_published DATETIME not null,
                recp_id INT not null
            );`;
            database.connect().query(newsletterTable, function(err, result) {
                if (err) (log(chalk.red("\tnewsletterTable -> fail")));
                else (log(chalk.greenBright("\tnewsletterTable -> success")));
            });
    
             // pdf_assessmentsTable
             pdf_assessmentsTable = `CREATE TABLE monitus.pdf_assessment (
                Assessment_id INT PRIMARY KEY AUTO_INCREMENT,
                Assessment_Name VARCHAR(255) not null,
                Assessment_Date DATETIME not null,
                DueDate DATETIME not null,
                Active BOOLEAN not null,
                Subject_id INT not null,
                Duration DATETIME not null, 
                Num_marks INT not null,
                PDF_File VARCHAR(255) not null
            );`;
            database.connect().query(pdf_assessmentsTable, function(err, result) {
                 if (err) (log(chalk.red("\tpdf_assessmentsTable -> fail")));
                 else (log(chalk.greenBright("\tpdf_assessmentsTable -> success")));
            });

            // generated_assessmentTable
            generated_assessmentTable= `CREATE TABLE monitus.generated_assessment (
                Assessment_id INT PRIMARY KEY AUTO_INCREMENT,
                Assessment_Name VARCHAR(255) not null,
                Assessment_Date DATETIME not null,
                DueDate DATETIME not null,
                Active BOOLEAN not null,
                Subject_id INT not null,
                Num_Questions INT not null,
                Total_Marks INT not null
            );`;
             database.connect().query(generated_assessmentTable, function(err, result) {
                 if (err) (log(chalk.red("\tgenerated_assessmentTable -> fail")));
                 else (log(chalk.greenBright("\tgenerated_assessmentTable -> success")));
             });

            // questionTable
            questionTable = `CREATE TABLE monitus.question (
                question_id INT PRIMARY KEY AUTO_INCREMENT,
                description VARCHAR(255) not null,
                mark INT not null,
                assessment_id INT not null
            );`;
            database.connect().query(questionTable, function(err, result) {
                 if (err) (log(chalk.red("\tquestionTable -> fail")));
                 else (log(chalk.greenBright("\tquestionTable -> success")));
             });

            // answerTable
            answerTable = `CREATE TABLE monitus.answer (
                answer_id INT PRIMARY KEY AUTO_INCREMENT,
                description VARCHAR(255) not null,
                question_id INT not null
            );`;
            database.connect().query(answerTable, function(err, result) {
                 if (err) (log(chalk.red("\tanswerTable -> fail")));
                 else (log(chalk.greenBright("\tanswerTable -> success")));
             });

             // answerTable
             gradeTable = `CREATE TABLE monitus.grade (
                grade_id INT PRIMARY KEY AUTO_INCREMENT
            );`;
            database.connect().query(gradeTable, function(err, result) {
                 if (err) (log(chalk.red("\tgradeTable -> fail")));
                 else (log(chalk.greenBright("\tgradeTable -> success")));
             });

            // resultsTable
            resultsTable = `CREATE TABLE monitus.results (
                results_id INT PRIMARY KEY AUTO_INCREMENT,
                final_Mark DOUBLE,
                subject_id INT not null,
                student_id INT not null
            );`;
            database.connect().query(resultsTable, function(err, result) {
                 if (err) (log(chalk.red("\tresultsTable -> fail")));
                 else (log(chalk.greenBright("\tresultsTable -> success")));
             });

             // subjectTable
             subjectTable = `CREATE TABLE monitus.subject (
                subject_id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) not null,
                grade_id INT not null,
                teacher_id INT not null
            );`;
            database.connect().query(subjectTable, function(err, result) {
                 if (err) (log(chalk.red("\tsubjectTable -> fail")));
                 else (log(chalk.greenBright("\tsubjectTable -> success")));
             });

            // submissionsTable
            submissionsTable = `CREATE TABLE monitus.submissions (
                submission_id INT PRIMARY KEY AUTO_INCREMENT,
                submitted BOOLEAN not null,
                feedback VARCHAR(255),
                score INT,
                results_id INT not null,
                pdf_id INT,
                generated_id INT
            );`;
            database.connect().query(submissionsTable, function(err, result) {
                 if (err) (log(chalk.red("\tsubmissionsTable -> fail")));
                 else (log(chalk.greenBright("\tsubmissionsTable -> success")));
             });
        }
    })
});