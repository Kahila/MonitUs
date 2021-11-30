var express = require('express');
var router = express.Router();
const request = require('request');
const body_parser = require('body-parser');

/* GET viewSubmissions page. */
router.get('/', function(req, res, next) {
    if (req.session.user.rank == 1){
        request.get({
            headers: { 'content-type': 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/assessments/${req.session.user.id}`,
            body: ""
        }, function(error, response, body) {
            // console.log(body + "-------------" + response);
            request.get({
                headers: { 'content-type': 'application/x-www-form-urlencoded' },
                url: `http://localhost:5000/api/assessments/online/${req.session.user.id}`,
                body1: ""
            }, function(error, response, body1) {
                // console.log(body + "-------------" + response);
                res.render('pages/viewSubmissions', {name: `${req.session.user.title}. ${req.session.user.surname}`, submissions: body, submissions2: body1});
            });
        });
    }else{
        res.redirect("../error");
    }
});

module.exports = router;