var express = require('express');
var router = express.Router();
const request = require('request');
const multer = require("multer");
const body_parser = require('body-parser');
const fs = require("fs");

var tmp = "";
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'public/uploads')
        console.log(file[0]);
    },
    filename: function (req, file, cb) {
        tmp = file.fieldname + '-marked' + Date.now()+".pdf";
        var path = "uploads/assessments";
        cb(null, tmp);
    }
})
var upload = multer({ storage: storage });

router.use(body_parser.urlencoded({extended: true}));
router.post('/upload_marked', upload.single('file'), function(req, res, next) {
    // console.log(req.query.id + "------------------------");
    if (tmp.length > 0){
        request.post({
            headers: {'content-type' : 'application/x-www-form-urlencoded'},
            url: `http://localhost:5000/api/submit/${req.body.upload}/${tmp}`,
            body: ""
        },function(error, response, body){
            //checking if the file has been downloaded by remote server
            if ((JSON.parse(body).code) == 200){
                // delete local file
                try {
                    fs.unlinkSync(`./public/uploads/${tmp}`)
                    res.redirect("back");
                    //file removed
                  } catch(err) {
                    //delete file was a failure
                    console.error(err)
                    res.redirect("back");
                  }
            }
            console.log(body);
        });
        submitted = 1;
    }
});

module.exports = router;