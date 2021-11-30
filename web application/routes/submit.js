var express = require('express');
var router = express.Router();


router.get('/', function(req, res, next) {
    if (req.session.user.rank == 0){
        res.render('pages/submit', {name: `${req.session.user.title}. ${req.session.user.surname}`, assessments: body});
    }
});

module.exports = router;