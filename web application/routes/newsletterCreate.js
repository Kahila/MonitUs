const express = require('express');
const router = express.Router();
const request = require('request');

router.post('/newsletterCreate', function(req, res, next) {
    console.log(req.body.note.replace(/\s/g, "_"))
    request.post({
        headers: {'content-type' : 'application/x-www-form-urlencoded'},
        url: `http://localhost:5000/api/createNewsletter/${req.body.subject.replace(/\s/g, "_")}/${req.body.note.replace(/\s/g, "_")}/${req.session.user.id}`,
        body: ""
    },function(error, response, body){
        console.log(req.body)
        if ((JSON.parse(body).code) == 200)
            res.redirect("back");
        else{
            res.redirect("back"); // add an error message
        }
    });
});

router.get('/', function(req, res, next) {
    if (req.session.user.rank == 2)
        res.render('pages/newsletterCreate', {name: `${req.session.user.title}. ${req.session.user.surname}`});
    else{
        res.redirect("pages/error");
    }
});

module.exports = router;