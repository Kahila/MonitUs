const io = require("socket.io")();
var express = require('express');
var router = express.Router();
const request = require('request');


const socketapi = {
    io: io
};

// Add your socket.io logic here!
io.on("connection", function(socket) {
    // console.log( "A user connected" );
    socket.on('assessments', function(status) {
        add_status(status, function(res) {
            if (res) {
                io.emit('refresh feed');
            } else {
                io.emit('error');
            }
        });
    });

    socket.on('submissions', function() {
        request.get({
            headers: { 'content-type': 'application/x-www-form-urlencoded' },
            url: `http://localhost:5000/api/assessments/submissions`,
            body: ""
        }, function(error, response, body) {
            io.emit('body', body);
        });

    });
});
// end of socket.io logic

// methods updating the assessment due date in ral time
var add_status = function(status, callback) {
    request.get({
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
        url: `http://localhost:5000/api/assessments/pdf/due`,
        body: ""
    }, function(error, response, body) {
        // console.log(body);
    });
    request.get({
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
        url: `http://localhost:5000/api/assessments/online/due`,
        body: ""
    }, function(error, response, body) {
        // console.log(body);
    });
}

// get students submissions in real time
var submissions= function(status, callback) {
    request.get({
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
        url: `http://localhost:5000/api/assessments/submissions`,
        body: ""
    }, function(error, response, body) {
        
    });
   
}

module.exports = socketapi;