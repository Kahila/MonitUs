var mysql = require('mysql');
require('dotenv').config()

//connecting to server
function conn() {
    return (mysql.createPool({
        host: process.env.HOST,
        user: process.env.USER,
        password: process.env.PASSWORD,
        port: process.env.DB_PORT
    }));
}

module.exports = { conn };