var express = require('express');
var router = express.Router();
/* GET home page. */
router.get('/', function(req, res, next) {

    // io.on('connection', (socket)=>{
    //     console.log('Socket connected successfully')
    // });

    res.render('pages/index', { title: 'Express' });
});

module.exports = router;