const express = require('express');
const mongoose = require('mongoose');
const routes = require('./routes');

(async ()=>{
    const mongoString = process.env.DATABASE_URL
    const database = await mongoose.createConnection(mongoString, {
        directConnection: true,
    });
    database.on('error', (error) => {
        console.log(error)
    })

    database.once('connected', () => {
        console.log('Database Connected');
    })
})();

/*mongoose.connect(mongoString)
const database = mongoose.connection

database.on('error', (error) => {
    console.log(error)
})

database.once('connected', () => {
    console.log('Database Connected');
})*/

const server = express();
server.use(express.json());
server.use('/api', routes)

module.exports=server
