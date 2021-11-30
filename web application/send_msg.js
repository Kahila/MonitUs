require('dotenv').config();
var client = require('twilio')(process.env.TWILIO_ACCOUNT_SID, process.env.TWILIO_AUTH_TOKEN);

function send_whatsapp(subject, dueDate, assessmentName, weighting, teacher){
  client.messages.create({
    from: 'whatsapp:+14155238886',
    body: `New Assessment : ${subject}\n\nTeacher: ${teacher}\nType : pdf\nHeading : ${assessmentName}\nWeighting : ${weighting}%\nDue On : ${dueDate} \n\n Hand written assessments must be scanned and submitted in PDF format\n\nscanning tool: https://play.google.com/store/apps/details?id=com.intsig.camscanner&hl=en_ZA&gl=US`,
    to: 'whatsapp:+27780052105'
  }).then(message => console.log(message.sid));
}

function send_whatsapp_online(subject, dueDate, assessmentName, weighting, teacher){
  client.messages.create({
    from: 'whatsapp:+14155238886',
    body: `New Assessment: ${subject}\n\nTeacher: ${teacher}\nType : Multiple Choice\nHeading : ${assessmentName}\nWeighting : ${weighting}%\nDue On : ${dueDate}\n\n NOTE : this assessment can only be completed on the MonitUS platform`,
    to: 'whatsapp:+27780052105'
  }).then(message => console.log(message.sid));
}

module.exports = { send_whatsapp, send_whatsapp_online };