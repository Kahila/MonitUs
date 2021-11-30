var express = require('express');
var router = express.Router();
const request = require('request');

/* GET assessmentMark page. */
router.get('/', function(req, res, next) {
    if (req.session.user.rank == 0){
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/students/marks/ave/${req.session.user.id}`,
            body: ""
        },function(error, response, body){
            res.render('pages/assessmentMark', {name: `${req.session.user.title}. ${req.session.user.surname}`, subject:body});
        });
    }
});

module.exports = router;