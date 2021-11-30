var express = require('express');
var router = express.Router();
const request = require('request');
const multer = require("multer");
const body_parser = require('body-parser');
const fs = require("fs");

var tmp = "";
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'public/uploads')
        console.log(file[0]);
    },
    filename: function (req, file, cb) {
        tmp = file.fieldname + '-submitted' + Date.now()+".pdf";
        var path = "uploads/assessments";
        cb(null, tmp);
    }
})
var upload = multer({ storage: storage })

var submitted = 0;
router.use(body_parser.urlencoded({extended: true}));
router.post('/assessments', upload.single('upload'), function(req, res, next) {
    // console.log(req.body.option + "-----------------");
    if (tmp.length > 0){
        request.post({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/submit/${req.body.option[0]}/${req.session.user.grade}/${req.session.user.id}/${tmp}`,
            body: ""
        },function(error, response, body){
            //checking if the file has been downloaded by remote server
            if ((JSON.parse(body).code) == 200){
                // delete local file
                try {
                    fs.unlinkSync(`./public/uploads/${tmp}`)
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
        submitted = 1;
    }
});


router.get('/', function(req, res, next) {
    if (req.session.user.rank == 0){
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/assessments/pdf/student/0/${req.session.user.id}`,
            body: ""
        },function(error, response, body){
            request.get({
                headers: {'content-type' : 'application/x-www-form-urlencoded'},
                url: `http://localhost:5000/api/assessments/online/student/0/${req.session.user.id}`,
                body1: ""
            },function(error, response, body1){
                console.log(body);
                if (body.length > 0){
                    res.render('pages/assessments', {name: `${req.session.user.title}. ${req.session.user.surname}`, assessments: body, submitted: submitted, onlineAssessments: body1});
                    submitted = 0; // setting value back to zero
                    tmp = "";
    
                }else
                    res.render('pages/assessments', {name: `${req.session.user.title}. ${req.session.user.surname}`, assessments: null, submitted: null, onlineAssessments: null});
            });

        });
    }
});

module.exports = router;