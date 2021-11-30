var express = require('express');
var router = express.Router();
const request = require('request');
const body_parser = require('body-parser');

/* GET viewSubmissions page. */
router.get('/', function(req, res, next) {
    // console.log("here bro here");
    if (req.session.user.rank == 1){
        // console.log(body + "-------------" + response);
        request.get({
            headers: { 'content-type': 'application/x-www-form-urlencoded' },
            url: `http://localhost:5000/api/assessments/submissions/${req.query.id}/${req.query.type}`,
            body: ""
        }, function(error, response, body) {
            console.log(body);
            res.render('pages/studentSubmissions', {name: `${req.session.user.title}. ${req.session.user.surname}`, submissions: body});
        });
    }else{
        res.redirect("../error");
    }
});

module.exports = router;