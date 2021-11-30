var express = require('express');
var router = express.Router();
const pdf2base64 = require('pdf-to-base64');
const request = require('request');
const fs = require("fs");
const multiparty = require("multiparty");
const multer = require("multer");
const body_parser = require('body-parser');
const { Console } = require('console');
var msg = require('../send_msg');

var tmp = "";
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'public/uploads')
    },
    filename: function (req, file, cb) {
        tmp = file.fieldname + '-' + Date.now()+".pdf";
        var path = "uploads/assessments";
        cb(null, tmp);
    }
})
var upload = multer({ storage: storage })

router.use(body_parser.urlencoded({extended: true}));
router.post('/newAssessment', upload.single('file'), function(req, res, next){
    // post request to remote server
    request.post({
        headers: {'content-type' : 'application/x-www-form-urlencoded'},
        url: `http://localhost:5000/api/upload/${req.body.subject.split("|")[0]}/${req.body.subject.split("|")[1]}/${req.body.date}/${req.body.time.replace(/\s/g, "_")}/${tmp}/${req.body.heading.replace(/\s/g, "_")}/${req.session.user.id}/${req.session.user.surname}/${req.body.weighting}`,
        body: ""
    },function(error, response, body){
        //checking if the file has been downloaded by remote server
        if ((JSON.parse(body).code) == 200){
            // delete local file
            try {
                fs.unlinkSync(`./public/uploads/${tmp}`)
                msg.send_whatsapp(req.body.subject.split("|")[0], req.body.date, req.body.heading, req.body.weighting, `${req.session.user.title}. ${req.session.user.surname}`);
                res.redirect("back");
                //file removed
              } catch(err) {
                //delete file was a failure
                console.error(err)
                res.redirect("back");
              }

        }
        console.log(body);
    });
});

router.get('/', function(req, res, next) {
    var subject = [];
    var grade = [];
    var today = new Date();

    // getting cuurent date
    today = today.getFullYear() + '-' + (String(today.getMonth() + 1).padStart(2, '0')) + '-' + (String(today.getDate()).padStart(2, '0')) ;
    // getting cuurent date
    console.log(today)

    if (req.session.user.rank == 1){
        // getting teachers info
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/subjects/teacher/${req.session.user.id}`,
            body: ""
        },function(error, response, body){
            //checking if the file has been downloaded by remote server
            if ((JSON.parse(body).code) == 200){
                console.log(body)
            }
            for (var item in JSON.parse(body)[0])
                console.log(item[0]);
            console.log(JSON.parse(body).length);
            for (var i = 0; i < JSON.parse(body).length; i++){
                if (i < JSON.parse(body).length){
                    subject.push(JSON.parse(body)[i].name);
                    grade.push(JSON.parse(body)[i].grade_id);
                }
            }
            res.render('pages/newAssessment', {name: `${req.session.user.title}. ${req.session.user.surname}`, teacher: subject, grade: grade, date:today});
        });
    } else {
        res.redirect('/error');
    }
});

module.exports = router;