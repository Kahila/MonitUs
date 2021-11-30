const express = require('express');
const app = express();
require('dotenv').config()
const bcrypt = require('bcrypt');
const Downloader = require('nodejs-file-downloader');
var multer, storage, path, crypto;
multer = require('multer');
path = require('path');
crypto = require('crypto');

var mysql = require('mysql');
require('dotenv').config()

//connecting to server
var connection = (mysql.createConnection({
    host: process.env.HOST,
    user: process.env.USER,
    password: process.env.PASSWORD,
    port: process.env.DB_PORT
}));

/*********************Mobile api methods ***************/

var fs = require('fs');
storage = multer.diskStorage({
    destination: './uploads/assessments',
    filename: function(req, file, cb) {
        let temp = file.originalname
        cb(null, temp.replace(":", ""))
    }
});

//get pdf assessments for subject
app.get('/api/pdfassessments/:subject_id', (req,res) => {
    assessment = 'SELECT Assessment_Name, Assessment_id, DueDate, PDF_File FROM monitus.pdf_assessment WHERE subject_id=?'
    connection.query(assessment, req.params.subject_id, (err, result) => {
        if(err) res.send(err);
        else{
            res.status(200).send(result);
        }
    });
});

//download pdf
app.get('/api/downloadPDF/:PDF_File', (req,res) => {
    const file = __dirname + '/uploads/assessments/' + req.params.PDF_File;
    res.download(file);
});

//get marks for pdf submission
app.get('/api/submissions/:student_id/:subject_id', (req, res) => {
    submissions = `SELECT submission_id, Assessment_Name, score, monitus.submissions.total, monitus.pdf_assessment.weight FROM monitus.submissions INNER JOIN monitus.pdf_assessment ON monitus.submissions.assessment_id=monitus.pdf_assessment.Assessment_id WHERE student_id=? AND subject_id=?`
    connection.query(submissions, [req.params.student_id, req.params.subject_id], (err, result)=>{
        if (err) res.status(400).send(err);
        res.status(200).send(result);
    }); 
}); 

var form = "<!DOCTYPE HTML><html><body>" +
"<form method='post' action='/upload' enctype='multipart/form-data'>" +
"<input type='file' name='upload'/>" +
"<input type='submit' /></form>" +
"</body></html>";

app.get('/', function (req, res){
  res.writeHead(200, {'Content-Type': 'text/html' });
  res.end(form);

});

//student pdf submission
app.post('/upload', multer({storage: storage}).single('upload'), function(req,res) {
    console.log(req.file);
    console.log(req.body);
    var student_id = req.body.student_id;
    var assessment_id = req.body.assessment_id;
    submission = "INSERT INTO monitus.submissions (submitted, results_id, assessment_id, student_id, pdf_file) VALUES (1, 0, ?, ?, ?)";
    connection.query(submission, [req.body.assessment_id, req.body.student_id, req.file.originalname], (err, res) =>{
        if (err) console.log(err);
    })
    res.sendStatus(200);
});

//Calculate average mark percentage achieved by students for comparison
app.get('/api/report/:subject_id', function(req, res) {
    report = "SELECT student_id, AVG(Percentage) AS Percentage FROM ( SELECT student_id, ( monitus.submissions.score / monitus.submissions.total ) * 100 AS Percentage FROM monitus.submissions INNER JOIN monitus.pdf_assessment ON monitus.submissions.assessment_id = monitus.pdf_assessment.Assessment_id INNER JOIN monitus.SUBJECT ON monitus.pdf_assessment.Subject_id = monitus.SUBJECT.subject_id WHERE monitus.pdf_assessment.Subject_id = ?) AS T GROUP BY student_id";
    connection.query(report, req.params.subject_id, (err, result)=>{
        if(err) res.send(err);
        else{
            res.send(result);
        }
    })
});

//get method calculates the student's current assessment mark for each subject
app.get('/api/reportcard/:student_id', function(req, res){
    reportCard = "SELECT subject_id, subject, SUM(Current) AS Mark, Assessment_weighting FROM ( SELECT student_id, subject, monitus.subject.subject_id, monitus.subject.Assessment_weighting, ( ( monitus.submissions.score / monitus.submissions.total) * 100 ) * (monitus.pdf_assessment.weight / 100) * (monitus.subject.Assessment_weighting/100) AS CURRENT FROM monitus.submissions INNER JOIN monitus.pdf_assessment ON monitus.submissions.assessment_id = monitus.pdf_assessment.Assessment_id INNER JOIN monitus.SUBJECT ON monitus.pdf_assessment.Subject_id = monitus.SUBJECT.subject_id WHERE student_id = ? ) AS T GROUP BY subject_id, subject"
    connection.query(reportCard, req.params.student_id, (err, result)=>{
        if(err) res.send(err);
        else{
            res.send(result);
        }
    })
});
/*********************Mobile api methods end***************/

// Get methods Start

//get assessment info for generated assessments
app.get('/api/online/:assessment_id', (req, res) => {
    generated = `SELECT * FROM monitus.generated_assessment WHERE Assessment_id=?`
    connection.query(generated, [req.params.assessment_id], (err, result) => {
        if (err) res.status(400).send(err);
        else {
            res.status(200).send(result);
        }
    });
});

// get all the students doing teachers subject
app.get('/api/students/:teacher_id', (req, res) => {
    teacher = `SELECT * FROM monitus.subject WHERE teacher_id=?`
    connection.query(teacher, [req.params.teacher_id], (err, result) => {
        if (err) res.status(400).send(err);
        else {
            var temp = ""; // creating JSON string
            for (var i = 0; i < result.length; i++) {
                (function(j) {
                    // if (((new Date(`${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[1]} ${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[2]}, ${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[3]} ${result[j].Time}`).getTime()) - new Date().getTime()) < 0) {
                    subject = `SELECT * FROM monitus.student_subjects WHERE subject=?`;
                    connection.query(subject, result[j].subject_id, (err, results) => {
                        if (err) res.status(400).send(err);
                        else {
                            var students = ""; // creating JSON string
                            for (var k = 0; k < results.length; k++) {
                                (function(l) {
                                    subject = `SELECT * FROM monitus.student WHERE id=?`;
                                    connection.query(subject, results[k].student_id, (err, resultss) => {
                                        if (err) res.status(400).send(err);
                                        else{
                                            students += `${resultss[0].name} ${resultss[0].surname},`;
                                            if (l == (results.length - 1)){
                                                temp += `{"subject": "${result[j].name}", "grade": ${result[j].grade_id}, "students": "${students.slice(0, -1)}"},`;
                                                console.log(temp)
                                                if (j === (result.length - 1)) {
                                                    res.status(200).send(JSON.parse(`[${temp.slice(0, -1)}]`));
                                                }
                                            }
                                        }
                                    })
                                })(k);
                            }
                        }
                    })
                })(i);
            }
        }
    });
});

//======================================================
//get completed assessment
app.get('/api/completed_assessment/:std_id', (req, res) => {
    completed_assessement = `SELECT submission_id FROM monitus.submissions WHERE student_id=?`
    connection.query(completed_assessement, [req.params.std_id], (err, result) => {
        if (err) res.status(400).send(err);
        if (result.length > 0) {
            res.status(200).send(result.length.toString());
        } else {
            res.status(200).send('0');
        }
    });
});
//====================================================
// get the assessments and due dates PDF
app.get('/api/assessments/pdf/due', (req, res) => {
    assessment = `SELECT * FROM monitus.pdf_assessment WHERE Active=1`;
    connection.query(assessment, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            if (result) {
                var tmp = ""; // creating JSON string
                for (var i = 0; i < result.length; i++) {
                    (function(j) {
                        if (((new Date(`${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[1]} ${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[2]}, ${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[3]} ${result[j].Time}`).getTime()) - new Date().getTime()) < 0) {
                            update = `UPDATE monitus.pdf_assessment SET Active='0' WHERE Assessment_id=?`;
                            connection.query(update, result[j].Assessment_id, (err, result) => {
                                if (err) res.status(400).send(err);
                                else {
                                    console.log(result[j].Assessment_id);
                                }
                            })
                        }
                        if (j === (result.length - 1)) {
                            res.status(200).send(`update quary`);
                        }
                    })(i);
                }
            } else {
                res.status(200).send(result);
            }
        }
    })
})

// get the assessments and due dates Online
app.get('/api/assessments/online/due', (req, res) => {
    assessment = `SELECT * FROM monitus.generated_assessment WHERE Active=1`;
    connection.query(assessment, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            if (result) {
                var tmp = ""; // creating JSON string
                for (var i = 0; i < result.length; i++) {
                    (function(j) {
                        // console.log("outside");
                        if (((new Date(`${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[1]} ${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[2]}, ${(new String ((result[j].DueDate)).split('-')[0]).split(' ')[3]} 00:00:00`).getTime()) - new Date().getTime()) < 0) {
                            update = `UPDATE monitus.generated_assessment SET Active='0' WHERE Assessment_id=?`;
                            connection.query(update, result[j].Assessment_id, (err, results) => {
                                if (err) res.status(400).send(err);
                                else {
                                    console.log(results);
                                }
                            })
                        }
                        if (j === (result.length - 1)) {
                            res.status(200).send(`update quary`);
                        }
                    })(i);
                }
            } else {
                res.status(200).send(result);
            }
        }
    })
})

// ======================================================
// get online assessments for specific student
app.get('/api/assessments/online/student/:stats/:student_id', (req, res) => {
    student = `SELECT subject FROM monitus.student_subjects WHERE student_id=?` // get all subject IDs' from database
    connection.query(student, req.params.student_id, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            if (result[0].length !== 0) {
                var tmp = ""; // creating JSON string
                for (var i = 0; i < result.length; i++) {
                    (function(j) {
                        if (req.params.stats == 0)
                            assessment = `SELECT * FROM monitus.generated_assessment WHERE subject_id=? ORDER BY Assessment_Date DESC` // get the pdf_assessment
                        else
                            assessment = `SELECT * FROM monitus.generated_assessment WHERE subject_id=? AND Active=1` // get the pdf_assessment
                            connection.query(assessment, result[i].subject, (err, resultss) => {
                                if (err) res.status(400).send(err);
                                else if (resultss !== 0) {
                                    for (var k = 0; k < resultss.length; k++) { // handles multiple assessments for specific id
                                        (function(l) {
                                            tmp += `{"Assessment_id":${resultss[l].Assessment_id},"Assessment_type":"${resultss[l].Assessment_type}","Assessment_Date":"${resultss[l].Assessment_Date}","DueDate":"${resultss[l].DueDate}","Active":${resultss[l].Active},"Subject_id":${resultss[l].Subject_id},"Num_Questions":"${resultss[l].Num_Questions}","Total_Marks":"${resultss[l].Total_Marks}","Options":"${resultss[l].Options}","teacher_id":${resultss[l].teacher_id},"Question":"${resultss[l].Question}","Answers":"${resultss[l].Answers}", "time_set":"${resultss[l].time_set}", "teacher":"${resultss[l].teacher}", "subject":"${resultss[l].subject}", "started":"${resultss[l].started}"},`;
                                        })(k);
                                    }
                                }
                                if (j === (result.length - 1)) {
                                    console.log(`[${tmp.slice(0, -1)}]`)
                                    res.status(200).send(`[${tmp.slice(0, -1)}]`);
                                }
                            })
                    })(i);
                }
            } else {
                res.status(200).send(`${result}`);
            }
        }
    });
})

// get pdf assessments for specific student
app.get('/api/assessments/pdf/student/:stats/:student_id', (req, res) => {
    student = `SELECT subject FROM monitus.student_subjects WHERE student_id=?` // get all subject IDs' from database
    connection.query(student, req.params.student_id, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            if (result[0].length !== 0) {
                var tmp = ""; // creating JSON string
                for (var i = 0; i < result.length; i++) {
                    (function(j) {
                        if (req.params.stats == 0)
                            assessment = `SELECT * FROM monitus.pdf_assessment WHERE subject_id=? ORDER BY Assessment_Date DESC` // get the pdf_assessment
                        else
                            assessment = `SELECT * FROM monitus.pdf_assessment WHERE subject_id=? AND Active=1` // get the pdf_assessment
                        connection.query(assessment, result[i].subject, (err, resultss) => {
                            if (err) res.status(400).send(err);
                            else if (resultss !== 0) {
                                for (var k = 0; k < resultss.length; k++) { // handles multiple assessments for specific id
                                    (function(l) {
                                        // 
                                        tmp += `{"Assessment_id":${resultss[l].Assessment_id},"Assessment_Name":"${resultss[l].Assessment_Name}","Assessment_Date":"${resultss[l].Assessment_Date}","DueDate":"${resultss[l].DueDate}","Active":${resultss[l].Active},"Subject_id":${resultss[l].Subject_id},"Duration":"${resultss[l].Duration}","Num_marks":${resultss[l].Num_marks},"PDF_File":"${resultss[l].PDF_File}","Time":"${resultss[l].Time}","teacher_id":${resultss[l].teacher_id},"Teacher":"${resultss[l].Teacher}","Subject":"${resultss[l].Subject}"},`;
                                    })(k);
                                }
                            }
                            if (j === (result.length - 1)) {
                                res.status(200).send(`[${tmp.slice(0, -1)}]`);
                            }
                        })
                    })(i);
                }
            } else {
                res.status(200).send(`${result}`);
            }
        }
    });
})

// get students specific pdf submission by ID
app.get('/api/assessments/submissions/:submission_id', (req, res) => {
    student = `SELECT * FROM monitus.submissions WHERE submission_id=?` // get all subject IDs' from database
    connection.query(student, req.params.submission_id, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            res.status(200).send(result)
        }
    });
})

// get submissions for teacher to view
app.get('/api/assessments/submissions', (req, res) => {
    student = `SELECT * FROM monitus.submissions` // get all subject IDs' from database
    connection.query(student, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            // console.log("queried submissions");
            res.status(200).send(result)
        }
    });
})

// get submissions for specific assessment ==================================
// get submissions for teacher to view
app.get('/api/assessments/submissions/:id/:type', (req, res) => {
    student = `SELECT * FROM monitus.submissions WHERE assessment_id=? AND generated_id=?`
    connection.query(student,[req.params.id, req.params.type], (err, results) => {
        if (err) res.status(400).send(err);
        else {
            var i = 0;
            var tmp = "";
            if (!results[0]){
                 res.status(200).send(`[{}]`);
            }
            for (var i = 0; i < results.length; i++) {
                (function(j) {
                    subject = `SELECT name, surname, id FROM monitus.student WHERE id=?`
                    connection.query(subject, results[j].student_id, (err, resultss) => {
                        if (err) res.status(400).send(err);
                        else{
                            tmp += `{"submission_id": "${results[j].submission_id}", "score": ${results[j].score}, "total":${results[j].total}, "student_id":${results[j].student_id}, "student_name":"${resultss[0].name}", "surname":"${resultss[0].surname}", "pdf_file":"${results[j].pdf_file}"},`
                        }
                            if (j === (results.length - 1)){
                                // console.log(results);
                                // console.log(resultss);
                                res.status(200).send(`[${tmp.slice(0, -1)}]`);
                            }
                    })
                })(i);
            }
            // console.log(result);
            // res.status(200).send(result)
        }
    });
})

// get teacher info 
app.get('/api/teachers/:teacher_id', (req, res) => {
    teachers = `SELECT * FROM  monitus.teacher WHERE id=?`
    connection.query(teachers, req.params.teacher_id, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            res.status(200).send(result);
        }
    });
});


// mark pdf assessment
app.post('/api/mark/pdf/:assessment_id/:mark/:total', (req, res) => { // get the subjects that are being taught by the teacher
    teachers = `UPDATE monitus.submissions SET score=?, total=? WHERE submission_id=?`
    connection.query(teachers, [req.params.mark, req.params.total, req.params.assessment_id], (err, result) => {
        if (err) res.status(400).send(err);
        else {
            res.status(200).send(result);
        }
    });
});

app.get('/api/subjects/teacher/:teacher_id', (req, res) => { // get the subjects that are being taught by the teacher
    teachers = `SELECT name, grade_id FROM monitus.subject WHERE teacher_id=?`
    connection.query(teachers, req.params.teacher_id, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            res.status(200).send(result);
        }
    });
});

// get subjects that belong to specified student
app.get('/api/subjects/student/:student_id', (req, res) => {
    student = `SELECT subject FROM monitus.student_subjects WHERE student_id=?` // get all subject IDs' from database
    connection.query(student, req.params.student_id, (err, result) => {
        if (err) res.status(400).send(err);
        else {
            if (result[0].length !== 0) {
                var tmp = "";
                for (var i = 0; i < result.length; i++) {
                    (function(j) {
                        subject = `SELECT name, teacher_id FROM monitus.subject WHERE subject_id=?` // get the subject names from the table
                        connection.query(subject, result[i].subject, (err, resultss) => {
                            if (err) res.status(400).send(err);
                            else {
                                teacher = `SELECT surname FROM monitus.teacher WHERE id=?` // get the subject names from the table
                                connection.query(teacher, resultss[0].teacher_id, (err, result2) => {
                                    if (err) res.status(400).send(err);
                                    // tmp += `{"subject":"${resultss[0].name}", "subject_id":${result[j].subject}, "teacher_id":${resultss[0].teacher_id}, "teacher_name":${result2[0].surname}},`
                                    tmp += `{"subject":"${resultss[0].name}", "subject_id":"${result[j].subject}", "teacher_id":${resultss[0].teacher_id}, "teacher_name":"${result2[0].surname}"},`
                                    if (j === (result.length - 1)) {
                                        res.status(200).send(JSON.parse(`[${tmp.slice(0, -1)}]`));
                                    }
                                })
                            }
                        })
                    })(i);
                }
            }
        }
    });
});

// get assessment info for specific teacher pdf
app.get('/api/assessments/:teacher_id', (req, res) => {
    assessment = `SELECT * FROM monitus.pdf_assessment WHERE teacher_id=?`
    connection.query(assessment, req.params.teacher_id, (err, results) => {
        if (err) res.status(400).send(err);
        if (!results[0]){
            res.status(200).send(`[{}]`);
       }else {
            var i = 0;
            var tmp = "";
            for (var i = 0; i < results.length; i++) {
                (function(j) {
                    subject = `SELECT name, grade_id FROM monitus.subject WHERE subject_id=?`
                    connection.query(subject, results[j].Subject_id, (err, resultss) => {
                        if (err) res.status(400).send(err);
                        // get number of submissions
                        count = `SELECT COUNT(*) FROM monitus.submissions WHERE Assessment_id=? AND generated_id=0`
                        connection.query(count, results[j].Assessment_id, (err, sub_count) => {
                            if (err) console.log(err)
                            var sum = Object.values(Object.values(JSON.parse(JSON.stringify(sub_count)))[0])[0];
                            tmp += `{"Assessment_id":${results[j].Assessment_id},"Assessment_Name":"${results[j].Assessment_Name}","Assessment_Date":"${results[j].Assessment_Date}","DueDate":"${results[j].DueDate}","Active":${results[j].Active},"Subject_id":${results[j].Subject_id},"Duration":"${results[j].Duration}","Num_marks":${results[j].Num_marks},"PDF_File":"${results[j].PDF_File}","Note":"${results[j].Time}","teacher_id":${results[j].teacher_id},`
                            tmp += `"name":"${resultss[0].name}","grade_id":${resultss[0].grade_id}, "count": ${sum}},`
                            if (j === (results.length - 1))
                                res.status(200).send(`[${tmp.slice(0, -1)}]`);
                        })
                    })
                })(i);
            }
        }
    });
});

// get assessment info for specific teacher online
app.get('/api/assessments/online/:teacher_id', (req, res) => {
    assessment = `SELECT * FROM monitus.generated_assessment WHERE teacher_id=?`
    connection.query(assessment, req.params.teacher_id, (err, results) => {
        if (err) res.status(400).send(err);
        if (results[0].length !== 0) {

            var i = 0;
            var tmp = "";
            for (var i = 0; i < results.length; i++) {
                (function(j) {
                    subject = `SELECT name, grade_id FROM monitus.subject WHERE subject_id=?`
                    connection.query(subject, results[j].Subject_id, (err, resultss) => {
                        if (err) res.status(400).send(err);
                        else{
                            // get number of submissions
                            count = `SELECT COUNT(*) FROM monitus.submissions WHERE Assessment_id=? AND generated_id=1`
                            connection.query(count, results[j].Assessment_id, (err, sub_count) => {
                                if (err) console.log(err)
                                var sum = Object.values(Object.values(JSON.parse(JSON.stringify(sub_count)))[0])[0];
                                tmp += `{"Assessment_id":${results[j].Assessment_id},"Assessment_Date":"${results[j].Assessment_Date}","DueDate":"${results[j].DueDate}","Active":${results[j].Active},"Subject_id":${results[j].Subject_id}, "teacher_id":${results[j].teacher_id},`
                                tmp += `"name":"${resultss[0].name}","grade_id":${resultss[0].grade_id}},`
                                if (j === (results.length - 1)){
                                    console.log("\n\n" + tmp);
                                    res.status(200).send(`[${tmp.slice(0, -1)}]`);
                                }
                            })
                        }
                    })
                })(i);
            }
        }
    });
});

app.get('/api/students/:student_id', (req, res) => {
    student = `SELECT * FROM monitus.student WHERE id=?`
    connection.query(student, req.params.student_id, (err, result) => {
        if (err) res.status(400).send(err);
        res.status(200).send(result);
    });
});

app.get('/api/receptionist/:recp_id', (req, res) => {
    receptionist = `SELECT * FROM monitus.receptionist WHERE id=?`
    connection.query(receptionist, req.params.recp_id, (err, result) => {
        if (err) res.status(400).send(err);
        res.status(200).send(result);
    });
});

app.get('/api/newsletters', (req, res) => {
    newsletters = `SELECT * FROM monitus.newsletters`
    connection.query(newsletters, (err, result) => {
        if (err) res.status(400).send(err);
        res.status(200).send(result);
    });
}); // Get methods end


// POST methods start

// =========== online assessment creation ============
app.post('/api/onlineAssessment/creation/:subject/:grade/:teacher_id/:assessment_type/:num_questions/:total_marks/:options/:question/:answers/:due/:teacher', (req, res) => {
    subject = `SELECT subject_id FROM monitus.subject WHERE name=? AND  grade_id=?`
    connection.query(subject, [req.params.subject.trim(), req.params.grade.trim()], (err, result) => {
        if (err) console.log(err);
        else {
            results = JSON.parse(JSON.stringify(result))
            console.log(results[0].subject_id);
            create = `INSERT INTO monitus.generated_assessment (Assessment_type, Assessment_Date, DueDate, Active, Subject_id, Num_Questions, Total_Marks, Options, Question, teacher_id, Answers, time_set, teacher, subject, started) VALUES (?, curdate(), ?, 1, ?, ?, ?, ?, ?, ?, ?, ${new Date().getTime()}, ?, ?, 0)`;
            connection.query(create, [req.params.assessment_type, req.params.due, results[0].subject_id, req.params.num_questions, req.params.num_questions, req.params.options, req.params.question, req.params.teacher_id, req.params.answers, req.params.teacher, req.params.subject], (err, result) => {
                if (err) res.send(err);
                else {
                    res.send({
                        "code": 200,
                        "message": "assessment created"
                    })
                }
            });
        }
    });
});

// =========== online assessment submission ============
app.post('/api/onlineAssessment/submit/:array/:student_id', (req, res) => {
    // console.log(req.params.array);
    const tmp = req.params.array.split(',');
    
    // getting the answers
    answers = `SELECT answers FROM monitus.generated_assessment WHERE Assessment_id=?`
    connection.query(answers, [tmp[0].split('_')[1]], (err, result) => {
        if (err) res.send(err);
        else {
            // marking the assessment
            if (result[0].answers.split(',').length == req.params.array.split(',').length){
                var i = 0;
                var mark = 0;
                for (var i = 0; i < result[0].answers.split(',').length; i++) {
                    (function(j) {
                        // console.log(parseInt(req.params.array.split(',')[j].split('_')[0]) + "-------------------" + parseInt(result[0].answers.split(',')[j]))
                        if (parseInt(req.params.array.split(',')[j].split('_')[0]) == parseInt(result[0].answers.split(',')[j])){
                            mark++;
                        }
                        if (j == (result[0].answers.split(',').length - 1)){
                            // saving to database
                            console.log( req.params.array.split(',')[0].split('_')[1])
                            submit = `INSERT INTO monitus.submissions (generated_id, student_id, score, total, assessment_id) VALUES (1, ?, ?, ?, ?)`;
                            connection.query(submit, [req.params.student_id, mark, req.params.array.split(',').length, parseInt(req.params.array.split(',')[0].split('_')[1])], (err, result) => {
                                if (err) res.send(err);
                                else {
                                    res.status(200).send({
                                        "code": 200,
                                        "message": "submited",
                                        "mark" : ((mark/req.params.array.split(',').length) * 100)
                                    })
                                }
                            });
                            // res.send(mark.toString());
                        }
                    })(i);
                }
                // [{"answers":"3,2"}]
            }else{
                res.send("wrong question");
            }
        }
    });
});


//================================== login user start
app.post('/api/login/:username/:password', (req, res) => {
    login = `SELECT id, name, surname, password, grade_id, active, title FROM monitus.student WHERE username=? UNION SELECT  id, name, surname, password, rank, active, title FROM monitus.teacher WHERE username=? UNION SELECT id, name, surname, password, rank, active, title FROM monitus.receptionist WHERE username=?`
    connection.query(login, [req.params.username, req.params.username, req.params.username], (err, result) => {
        if (err) res.send(err);
        else if (result.length > 0) { // checking user passowrd match
            bcrypt.compare(req.params.password, result[0].password, function(err, results) {
                if (err) res.status(400).send({
                    "message": "invalid login"
                });
                if (results) {
                    var rank = 0;
                    var grade = 0;

                    // setting the rank and the grade
                    if (result[0].grade_id == 1) rank = 1; // teacher
                    else if (result[0].grade_id == 2) rank = 2; // receptionist
                    else { grade = result[0].grade_id; } // student

                    // content to send
                    // console.log(`${req.params.username} login attemp`)
                    res.status(200).send({
                        id: result[0].id,
                        loggen_in: true,
                        username: req.params.username,
                        name: result[0].name,
                        surname: result[0].surname,
                        grade: grade,
                        rank: rank,
                        title: result[0].title
                    });
                } else {
                    res.status(400).send({
                        "message": "invalid login"
                    });
                }
            });
        } else {
            res.status(400).send({
                "message": "invalid login"
            });
        }
    });
});
//================================== login user end

//================================== pdf upload
app.post('/api/upload/:subject/:grade/:date/:time/:name/:heading/:id/:teacher/:weight', (req, res) => {

    (async() => { //Wrapping the code with an async function

        const downloader = new Downloader({
            url: `http://localhost:3000/uploads/${req.params.name}`,
            directory: "./uploads/assessments", //This folder will be created, if it doesn't exist.
        })
        try {
            await downloader.download(); //Downloader.download() returns a promise.

            // get subject info
            subject = `SELECT subject_id FROM monitus.subject WHERE name=? AND  grade_id=?`
            connection.query(subject, [req.params.subject.trim(), req.params.grade.trim()], (err, result) => {
                if (err) console.log(err);
                else {
                    results = JSON.parse(JSON.stringify(result))
                    console.log(results[0].subject_id);
                    pdf = `INSERT INTO monitus.pdf_assessment (Assessment_Name, Assessment_Date, DueDate, Active, Subject_id, Duration, Num_marks, PDF_File, Time, teacher_id, Teacher, Subject, time_set, grade, weight) VALUES (?, curdate(), ?, '1', ?, curdate(), 0, ?, ?, ?, ?, ?, ${new Date().getTime()}, ?, ?)`;
                    connection.query(pdf, [req.params.heading, req.params.date, results[0].subject_id, req.params.name, req.params.time, req.params.id, req.params.teacher, req.params.subject, parseInt(req.params.grade.trim()), req.params.weight], (err, result) => {
                        if (err) res.send(err);
                        else {
                            res.send({
                                "code": 200,
                                "message": "upload successful"
                            })
                        }
                    });
                }
            });

        } catch (error) { 
            console.log('Download failed', error)
        }
    })();
});

//=====================================================================
//get all marked
//====================================================
//marked assessement
app.get('/api/marked_assessment/:std_id', (req, res) => {
    completed_assessement = `SELECT submission_id FROM monitus.submissions WHERE student_id=? AND score IS NOT NULL `
    connection.query(completed_assessement, [req.params.std_id], (err, result)=>{
        if (err) res.status(400).send(err);
        if ( result.length > 0){
            
            res.status(200).send(result.length.toString());
        }
        else{
            res.status(200).send("0");

        }
    });
});

//get all assessment that are not active - completed
//======================================================
app.get('/api/all_assessment/:student_id', (req, res) => {
    student = `SELECT subject FROM monitus.student_subjects WHERE student_id=?`
    connection.query(student, req.params.student_id, (err, results)=>{
        if (err) res.status(400).send(err);
        else
        {
        if(results.length==8)
        {
            
            assessement = `SELECT Assessment_id FROM monitus.pdf_assessment WHERE (Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? )AND Active=0 UNION SELECT Assessment_id FROM monitus.generated_assessment WHERE Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? And Active=0 `
        
            connection.query(assessement, [results[0].subject,results[1].subject,results[2].subject,results[3].subject,results[4].subject,results[5].subject,results[6].subject,results[7].subject,results[0].subject,results[1].subject,results[2].subject,results[3].subject,results[4].subject,results[5].subject,results[6].subject,results[7].subject], (err, result)=>{
            
                if (err) res.status(400).send(err);
                if ( result.length > 0){
                
                    res.status(200).send(result.length.toString());
                    }
                    else{
                        res.status(200).send("0");
            
                    }
                });
            }
            if (results.length==9)
            {
                due_assessement = `SELECT Assessment_id FROM monitus.pdf_assessment WHERE (Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? ) AND Active= 0  UNION SELECT Assessment_id FROM monitus.generated_assessment WHERE (Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? OR Subject_id=? ) AND Active=0 `
                connection.query(due_assessement, [results[0].subject,results[1].subject,results[2].subject,results[3].subject,results[4].subject,results[5].subject,results[6].subject,results[7].subject,results[8].subject,results[0].subject,results[1].subject,results[2].subject,results[3].subject,results[4].subject,results[5].subject,results[6].subject,results[7].subject,results[8].subject], (err, result)=>{
                if (err) res.status(400).send(err);
                if ( result.length > 0){
                    res.status(200).send(result.length.toString());
                    }
                    else{
                        res.status(200).send('0');
                    }
                });
            }
            }
        });
    
});





//================================== pdf submision student
app.post('/api/submit/:assessment_id/:grade/:student_id/:filename', (req, res) => {
    (async() => { //Wrapping the code with an async function
        const downloader = new Downloader({
            url: `http://localhost:3000/uploads/${req.params.filename}`,
            directory: "./uploads/assessments", //This folder will be created, if it doesn't exist.
        })
        try {
            await downloader.download(); //Downloader.download() returns a promise.

            // delete old submission for same student with same assessment
            deletion = `DELETE FROM monitus.submissions WHERE assessment_id=? AND student_id=?`
            connection.query(deletion, [req.params.assessment_id, req.params.student_id], (err, result) => {
                if (err) console.log(err);
                else {
                    // submitting assessments
                    subject = `INSERT INTO monitus.submissions (assessment_id, generated_id, student_id, pdf_file) VALUES (?, 0, ?, ?)`
                    connection.query(subject, [req.params.assessment_id, req.params.student_id, req.params.filename], (err, result) => {
                        if (err) console.log(err);
                        else {
                            res.send({
                                "code": 200,
                                "message": "submission successful"
                            })
                        }
                    });
                }
            });
        } catch (error) { //IMPORTANT: Handle a possible error. An error is thrown in case of network errors, or status codes of 400 and above.
            //Note that if the maxAttempts is set to higher than 1, the error is thrown only if all attempts fail.
            console.log('Download failed', error)
        }
    })();
});

// teacher upload submission
app.post('/api/submit/:submission_id/:filename', (req, res) => {
    (async() => { //Wrapping the code with an async function
        const downloader = new Downloader({
            url: `http://localhost:3000/uploads/${req.params.filename}`,
            directory: "./uploads/assessments", //This folder will be created, if it doesn't exist.
        })
        try {
            await downloader.download(); //Downloader.download() returns a promise.
            // console.log("============================================");
            // upload marked pdf file to submissions
            update = `UPDATE monitus.submissions SET marked_script=? WHERE submission_id=?`
            connection.query(update, [req.params.filename, req.params.submission_id], (err, result) => {
                if (err) console.log(err);
                else {
                    res.send({
                        "code": 200,
                        "message": "submission successful"
                    })
                }
            });
        } catch (error) { 
            console.log('Download failed', error)
        }
    })();
});

// download assessment student
var path = require('path');
app.use(express.static(path.join(__dirname, 'uploads')));
app.use('uploads/assessments', express.static('assessments'));

// newsletter creation
app.post('/api/createNewsletter/:subject/:body/:id', (req, res) => {
    if (req.params) {
        newsletter = "INSERT INTO monitus.newsletters (headline, message, date_published, recp_id) VALUES (?, ?, curdate(), ?)";
        connection.query(newsletter, [req.params.subject.replace(/_/g, " "), req.params.body.replace(/_/g, " "), req.params.id], (err, result) => {
            if (err) res.send(err);
            else {
                res.send({
                    "code": 200,
                    "message": "newsletter created successfully"
                })
            }
        });
    } else {
        res.send({
            "code": 400,
            "message": "newsletter Note created"
        })
    }
});

// PORT -> env
const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}...`));