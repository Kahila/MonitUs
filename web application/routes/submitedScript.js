var express = require('express');
var router = express.Router();
const request = require('request');

router.get('/', function(req, res, next) {
    if (req.session.user.rank == 0){
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/assessment/script/${req.query.id}`,
            body: ""
        },function(error, response, body){
            res.render('pages/submitedScript', {name: `${req.session.user.title}. ${req.session.user.surname}`, submission:body, id:req.query.id1});
        });
    }
});

module.exports = router;