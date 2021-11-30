var express = require('express');
var router = express.Router();
const request = require('request');
const body_parser = require('body-parser');

var id = 0;
router.post('/markpdf', function(req, res, next) {
    console.log(req.body);
    request.post({
        headers: {'content-type' : 'application/x-www-form-urlencoded'},
        url: `http://localhost:5000/api/mark/pdf/${total}/${req.body.mark}/${req.body.total}`,
        body: ""
    },function(error, response, body){
        res.redirect("../viewSubmissions");
    });
});

router.get('/', function(req, res, next) {
    total = req.query.id;
     if (req.session.user.rank == 1){
        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/assessments/submissions/${req.query.id}`,
            body: ""
        },function(error, response, body){
            res.render('pages/markpdf', {name: `${req.session.user.title}. ${req.session.user.surname}`, submission:body});
        });
    }else{
        res.redirect("../error");
     }
});

module.exports = router;