var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var mongoose = require('mongoose');
var dotenv = require('dotenv');

var indexRouter = require('./routes/index');
var laundryRouter = require('./routes/laundry');

dotenv.config({ path: '.env' });

var app = express();

mongoose.Promise = global.Promise;

mongoose.connect(process.env.MONGODB_URI, { useUnifiedTopology: true, useNewUrlParser: true, useCreateIndex: true })
.then(console.log("Established connection to the database!"))
.catch(err => {
    console.log(`DB Error: ${err.message}`);
    process.exit(-1);
});

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/laundry', laundryRouter);

module.exports = app;
