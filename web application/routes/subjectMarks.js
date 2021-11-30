var express = require('express');
var router = express.Router();
const request = require('request');

/* GET subjectMarks page. */
router.get('/', function(req, res, next) {
    if (req.session.user.rank == 0){
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/students/subject/submissions/pdf/${req.session.user.id}/${req.query.id}`,
            body: ""
        },function(error, response, body){
            request.get({
                headers: {'content-type' : 'application/x-www-form-urlencoded'},
                url: `http://localhost:5000/api/students/subject/submissions/online/${req.session.user.id}/${req.query.id}`,
                body1: ""
            },function(error, response, body1){
                res.render('pages/subjectMarks', {name: `${req.session.user.title}. ${req.session.user.surname}`, submission:body, online:body1, id:req.query.id});
            });

        });
    }
});

module.exports = router;