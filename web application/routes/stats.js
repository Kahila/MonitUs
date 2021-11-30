var express = require('express');
var router = express.Router();
const request = require('request');

/* GET home page. */
router.get('/', function(req, res, next) {
    if (req.session.user.rank == 1){
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/historical/${req.session.user.id}`,
            body: ""
        },function(error, response, body){
            request.get({
                headers: {'content-type' : 'application/x-www-form-urlencoded'},
                url: `http://localhost:5000/api/students/${req.session.user.id}`,
                body1: ""
            },function(error, response, body1){
                request.get({
                    headers: {'content-type' : 'application/x-www-form-urlencoded'},
                    url: `http://localhost:5000/api/number_gen_assessment/${req.session.user.id}`,
                    body2: ""
                },function(error, response, body2){
                    request.get({
                        headers: {'content-type' : 'application/x-www-form-urlencoded'},
                        url: `http://localhost:5000/api/number_pdf_assessment/${req.session.user.id}`,
                        body4: ""
                    },function(error, response, body4){
                        request.get({
                            headers: {'content-type' : 'application/x-www-form-urlencoded'},
                            url: `http://localhost:5000/api/assessmentsPerSubmject/${req.session.user.id}`,
                            body3: ""
                        },function(error, response, body3){
                            res.render('pages/stats', {name: `${req.session.user.title}. ${req.session.user.surname}`, old:body, tot:body1, total_assessments:(parseInt(body2) + parseInt(body4)), grade:body3});
                        });
                    });
                });
            });
        });
    }
});

module.exports = router;