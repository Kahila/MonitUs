const express = require('express');
const router = express.Router();
const request = require('request');

router.post('/login', function(req, res, next) {
    // querying the API for data
    // console.log(req.body);
    request.post({
        headers: {'content-type' : 'application/x-www-form-urlencoded'},
        url: `http://localhost:5000/api/login/${req.body.username.toLowerCase()}/${req.body.pass}`,
        body: ""
    },function(error, response, body){
        if ((JSON.parse(body).code) == 400)
            res.redirect("back");
        else{
            // creating the user session
            req.session.user = JSON.parse(body);
            if (req.session.user)
                res.redirect("/home");
        }
    });
});

router.get('/', function(req, res, next) {
    res.render('pages/login');
});

module.exports = router;