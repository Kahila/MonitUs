var express = require('express');
var router = express.Router();
const body_parser = require('body-parser');

router.use(body_parser.urlencoded({ extended: true }));
router.get('/download', function(req, res, next) {
    if (req.session.user.logged_in == true && req.session.user.rank == 0) {
        const file = `${__dirname}/../public/assessments/` + req.query.id;
        console.log("+++++++++++++++++++++++++++++++++++++", file);
        res.download(file);
    } else {
        res.redirect('/error');
    }
});

module.exports = router;