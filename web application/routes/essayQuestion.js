var express = require('express');
var router = express.Router();

router.post('/essayQuestion', function(req, res, next) {
    console.log(req.body);
    res.redirect("back");
});

router.get('/', function(req, res, next) {
    if (req.session.user.rank == 1){
        res.render('pages/essayQuestion', {name: `${req.session.user.title}. ${req.session.user.surname}`});
    }
});

module.exports = router;