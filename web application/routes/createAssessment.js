var express = require('express');
var router = express.Router();
const request = require('request');

router.get('/', function(req, res, next) {
    if (req.session.user.rank == 1){

        request.get({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/number_gen_assessment/${req.session.user.id}`,
            body: ""
        },function(error, response, body){
            if (req.session.user.rank == 1){
                 request.get({
                     headers: {'content-type' : 'application/x-www-form-urlencoded'},
                     url: `http://localhost:5000/api/number_pdf_assessment/${req.session.user.id}`,
                     body1: ""
                 }
        ,function(error, response, body1){
            console.log(body);
            res.render('pages/createAssessment', {name: `${req.session.user.title}. ${req.session.user.surname}`,online:body,pdf:body1});
        });
    }
});
}
});

module.exports = router;