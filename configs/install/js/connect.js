var mysql = require('mysql');

//function that establishes connection to DB
function connect() {
    return (mysql.createConnection({
        host: "localhost",
        user: "root",
        password: "root",
        port: "3306"
    }));
}

module.exports = { connect };