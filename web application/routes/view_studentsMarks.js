var express = require('express');
var router = express.Router();
const request = require('request');

router.get('/', function(req, res, next) {
    if (req.session.user.rank == 1){
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/students/${req.session.user.id}`,
            body: ""
        },function(error, response, body){
            res.render('pages/view_studentsMarks', {name: `${req.session.user.title}. ${req.session.user.surname}`, students: body});
        });
    }
});

module.exports = router;