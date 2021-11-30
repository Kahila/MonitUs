var msg = require('../send_msg');
var express = require('express');
var router = express.Router();
const request = require('request');
const body_parser = require('body-parser');
const url = require('url'); // built-in utility

var FormData = require('form-data');
var fs = require('fs');
var form = new FormData();
var numQuestions = 0;
var selectedSubject = "";
var selectedDue = "";
var time ="";
var heading ="";
var weighting = 0;

router.use(body_parser.urlencoded({extended: true}));
router.post('/newAssessmenyOnline', function(req, res, next) {
    if (req.body.num && req.body.subject && req.body.date){
        numQuestions = req.body.num;
        selectedSubject = req.body.subject;
        selectedDue = req.body.date;
        time = req.body.time;
        weighting = req.body.weight;
        heading = req.body.heading;
        console.log(time);
        res.redirect("back");
    }else{
        request.post({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/onlineAssessment/creation/${selectedSubject.split("|")[0]}/${selectedSubject.split("|")[1]}/${req.session.user.id}/0/${numQuestions}/${numQuestions}/${req.body.radio}/${req.body.question}/${req.body.answer}/${selectedDue}/${req.session.user.title +"_"+ req.session.user.surname}/${heading}/${weighting}`,
            body: ""
        },function(error, response, body){
            //checking if the file has been downloaded by remote server
            if ((JSON.parse(body).code) == 200){
                try {
                    fs.unlinkSync(`./public/uploads/${tmp}`)
                    msg.send_whatsapp_online(selectedSubject.split("|")[0], selectedDue, heading, weighting, `${req.session.user.title +" "+ req.session.user.surname}`);
                    res.redirect("../createAssessment");
                  } catch(err) {
                    console.error(err)
                    msg.send_whatsapp_online(selectedSubject.split("|")[0], selectedDue, heading, weighting, `${req.session.user.title +" "+ req.session.user.surname}`);
                    res.redirect("back");
                  }
            }
            console.log(body);
        });
        // console.log(req.body);
    }
});

/* GET newAssessmenyOnline page. */
router.get('/', function(req, res, next) {
    var subject = [];
    var grade = [];

    if (req.session.user.rank == 1){

        // checking for assessment creation cancelation
        if (req.query.cancel){
            numQuestions = 0;
            res.redirect("/home");
        }
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/subjects/teacher/${req.session.user.id}`,
            body: ""
        },function(error, response, body){
            //checking if the file has been downloaded by remote server
            if ((JSON.parse(body).code) == 200){
                console.log(body)
            }
            // for (var item in JSON.parse(body)[0])
            //     console.log(item[0]);
            // console.log(JSON.parse(body).length);
            for (var i = 0; i < JSON.parse(body).length; i++){
                if (i < JSON.parse(body).length){
                    subject.push(JSON.parse(body)[i].name);
                    grade.push(JSON.parse(body)[i].grade_id);
                }
            }
            res.render('pages/newAssessmentOnline', {name: `${req.session.user.title}. ${req.session.user.surname}`, teacher: subject, grade: grade, num: numQuestions, selectedDue: selectedDue, selectedSubject: selectedSubject, time:time, weight:weighting, heading:heading});
        });
    } else {
        res.redirect('/error');
    }
});

module.exports = router;