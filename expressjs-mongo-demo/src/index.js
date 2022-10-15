require('dotenv').config();
const server = require('./server');

server.listen(3000, () => {
    console.log(`Server Started at ${3000}`)
})

