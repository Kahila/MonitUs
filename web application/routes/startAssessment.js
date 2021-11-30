var express = require('express');
var router = express.Router();
const body_parser = require('body-parser');
const request = require('request');

// 
var submited = 0;
router.post('/startAssessment', function(req, res, next) {
    // querying the API for data
    console.log((req.body));
    var inputs = Object.values(req.body);
    
    request.post({
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
        url: `http://localhost:5000/api/onlineAssessment/submit/${inputs}/${req.session.user.id}`,
        body: ""
    }, function(error, response, body) {
        if ((JSON.parse(body).code) == 200){
            submited = 1;
            console.log((body));
            res.redirect("../assessments");
        }
    });
});

/* GET startAssessment page. */
router.use(body_parser.urlencoded({ extended: true }));
router.get('/', function(req, res, next) {
    if (req.session.user.rank == 0) {
        if (!req.query.aid) {
            res.redirect('/error');
        } else {
            request.get({
                headers: { 'content-type': 'application/x-www-form-urlencoded' },
                url: `http://localhost:5000/api/online/${req.query.aid}`,
                body: ""
            }, function(error, response, body) {
                res.render('pages/startAssessment', { name: `${req.session.user.title}. ${req.session.user.surname}`, assess: body , submited: submited});
            });
        }
    }
});

module.exports = router;