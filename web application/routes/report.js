var express = require('express');
var router = express.Router();
const request = require('request');

/* GET report page. */
router.get('/', function(req, res, next) {
    if (req.session.user.rank == 0){
        if (req.session.user.rank == 0){
            request.get({
                headers: {'content-type' : 'application/x-www-form-urlencoded'},
                url: `http://localhost:5000/api/reportcard/${req.session.user.id}`,
                body: ""
            },function(error, response, body){
                 // console.log(JSON.parse(body) );
                 res.render('pages/report', {name: `${req.session.user.title}. ${req.session.user.surname}`, surname: req.session.user.surname, grade: req.session.user.grade, subjects:body});
            });
       }
    }
});

module.exports = router;