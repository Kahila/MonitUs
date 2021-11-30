var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    if (req.session.user.rank == 1){
        res.render('pages/home1');
    }
});

module.exports = router;