var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var session = require('express-session');

var indexRouter = require('./routes/index');
var loginRouter = require('./routes/login');
var homeRouter = require('./routes/home');
var downloadRouter = require('./routes/download');
var assessmentsRouter = require('./routes/assessments');
var assessmentMarkRouter = require('./routes/assessmentMark');
var reportRouter = require('./routes/report');
var startAssessmentRouter = require('./routes/startAssessment');
var newAssessmentRouter = require('./routes/newAssessment');
var newAssessmenyOnlineRouter = require('./routes/newAssessmentOnline');
var viewSubmissionsRouter = require('./routes/viewSubmissions');
var logoutRouter = require('./routes/logout');
var newsletterCreateRouter = require('./routes/newsletterCreate');
var submitRouter = require('./routes/submit');
var studentSubmissionsRouter = require('./routes/studentSubmissions');
var markpdfRouter = require('./routes/markpdf');
var uploaddfRouter = require('./routes/upload_marked');
var view_studentsMarksRouter = require('./routes/view_studentsMarks');
var createAssessmentRouter = require('./routes/createAssessment');
var essayQuestionRouter = require('./routes/essayQuestion');
var subjectMarksRouter = require('./routes/subjectMarks');
var submitedScriptRouter = require('./routes/submitedScript');
var statsRouter = require('./routes/stats');

var app = express();

// web socket includes
var socketapi = require("./socketsapi");

//sattings for the session 
app.use(session({
    secret: 'secret',
    resave: true,
    saveUninitialized: true
}))

app.use(function(req, res, next) {
    res.locals.user = req.session.user;
    res.locals.errorMessages = req.session.errorMessages;
    next()
})

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use('public/uploads', express.static('uploads'));
app.use(express.static('public/images'));

app.use('/', indexRouter);
// app.use('/users', usersRouter);
app.use('/login', loginRouter);
app.use('/logout', logoutRouter);
app.use('/home', homeRouter);
// app.use('/home1', home1Router);
app.use('/download', downloadRouter);
app.use('/assessments', assessmentsRouter);
app.use('/assessmentMark', assessmentMarkRouter);
app.use('/report', reportRouter);
app.use('/startAssessment', startAssessmentRouter);
app.use('/newAssessment', newAssessmentRouter);
app.use('/newAssessmenyOnline', newAssessmenyOnlineRouter);
app.use('/viewSubmissions', viewSubmissionsRouter);
app.use('/newsletterCreate', newsletterCreateRouter);
app.use('/submit', submitRouter);
app.use('/studentSubmissions', studentSubmissionsRouter);
app.use('/markpdf', markpdfRouter);
app.use('/upload_marked', uploaddfRouter);
app.use('/view_studentsMarks', view_studentsMarksRouter);
app.use('/createAssessment', createAssessmentRouter);
app.use('/essayQuestion', essayQuestionRouter);
app.use('/subjectMarks', subjectMarksRouter);
app.use('/submitedScript', submitedScriptRouter);
app.use('/stats', statsRouter);

// error handler
app.use(function(err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('pages/error');
});

module.exports = { app, socketapi };