var express = require('express');
var router = express.Router();
const request = require('request');

router.get('/', function(req, res, next) {

     if (req.session.user.rank == 0){
          request.get({
              headers: {'content-type' : 'application/x-www-form-urlencoded'},
              url: `http://localhost:5000/api/completed_assessment/${req.session.user.id}`,
              body: ""
          },function(error, response, body){
               if (req.session.user.rank == 0){
                    request.get({
                        headers: {'content-type' : 'application/x-www-form-urlencoded'},
                        url: `http://localhost:5000/api/assessments/pdf/student/1/${req.session.user.id}`,
                        body1: ""
                    },function(error, response, body1){
                         if (req.session.user.rank == 0){
                              request.get({
                                  headers: {'content-type' : 'application/x-www-form-urlencoded'},
                                  url: `http://localhost:5000/api/assessments/online/student/1/${req.session.user.id}`,
                                  body2: ""
                              },function(error, response, body2){
                                   if (req.session.user.rank == 0){
                                        request.get({
                                            headers: {'content-type' : 'application/x-www-form-urlencoded'},
                                            url: `http://localhost:5000/api/marked_assessment/${req.session.user.id}`,
                                            body3: ""
                                        },function(error, response, body3){
                                             if (req.session.user.rank == 0){
                                                  request.get({
                                                      headers: {'content-type' : 'application/x-www-form-urlencoded'},
                                                      url: `http://localhost:5000/api/all_assessment/${req.session.user.id}`,
                                                      body4: ""
                                                  },function(error, response, body4){
                                                       if (req.session.user.rank == 0){
                                                            request.get({
                                                                headers: {'content-type' : 'application/x-www-form-urlencoded'},
                                                                url: `http://localhost:5000/api/reportcard/${req.session.user.id}`,
                                                                body5: ""
                                                            },function(error, response, body5){
                                                                 // console.log(JSON.parse(body) );
                                                                 res.render('pages/home', {name: `${req.session.user.title}. ${req.session.user.surname}`, completed: body, due: (JSON.parse(body1).length + JSON.parse(body2).length), marked:body3, overdue:body4, report:body5});
                                                            });
                                                       }
                                                  });
                                             }
                                        });
                                   }
                              });
                         }
                    });
               }
          });
     }// teacher
     else if (req.session.user.rank == 1){
          request.get({
              headers: {'content-type' : 'application/x-www-form-urlencoded'},
              url: `http://localhost:5000/api/number_pdf_assessment/${req.session.user.id}`,
              body: ""
          },function(error, response, body){
               request.get({
                    headers: {'content-type' : 'application/x-www-form-urlencoded'},
                    url: `http://localhost:5000/api/number_gen_assessment/${req.session.user.id}`,
                    body1: ""
               },function(error, response, body1){
                    request.get({
                         headers: {'content-type' : 'application/x-www-form-urlencoded'},
                         url: `http://localhost:5000/api/number_gen_assessment/active/${req.session.user.id}`,
                         body2: ""
                    },function(error, response, body2){
                         request.get({
                              headers: {'content-type' : 'application/x-www-form-urlencoded'},
                              url: `http://localhost:5000/api/number_gen_assessment/off/${req.session.user.id}`,
                              body3: ""
                         },function(error, response, body3){
                              request.get({
                                   headers: {'content-type' : 'application/x-www-form-urlencoded'},
                                   url: `http://localhost:5000/api/assessmentsPerSubmject/${req.session.user.id}`,
                                   body4: ""
                              },function(error, response, body4){
                                   request.get({
                                        headers: {'content-type' : 'application/x-www-form-urlencoded'},
                                        url: `http://localhost:5000/api/students/${req.session.user.id}`,
                                        body5: ""
                                    },function(error, response, body5){
                                         res.render('pages/home1', {name: `${req.session.user.title}. ${req.session.user.surname}`, total_assessments: (parseInt(body1) + parseInt(body)), active: parseInt(body2), off:parseInt(body3), grade:body4, num_students:body5});
                                    });
                              });
                         });
                    });
               });
          });
     }
     else if (req.session.user.rank == 2) // receptionist
          res.render('pages/home2', {name: `${req.session.user.title}. ${req.session.user.surname}`});
});

module.exports = router;