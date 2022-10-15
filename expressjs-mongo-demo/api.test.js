const supertest = require('supertest');
const mongoose = require("mongoose");
const {MongoDBContainer} = require("testcontainers");
let server;
let requestWithSupertest;
let mongodbContainer;

describe('Product Endpoints', () => {
    jest.setTimeout(240_000);

    beforeAll(async() => {
        mongodbContainer = await new MongoDBContainer().start();
        const db = await mongoose.createConnection(mongodbContainer.getConnectionString(), {
            directConnection: true,
        });
        console.log("db->", db)
        const conn= `mongodb://${mongodbContainer.getHost()}:${mongodbContainer.getMappedPort(27017)}/admin`
        console.log("conn->", conn)
        process.env['DATABASE_URL'] = conn
        server = require('./src/server');
        requestWithSupertest = supertest(server);
    })

    afterAll(async() => {
        try {
            await mongoose.disconnect();
            await mongodbContainer.stop();
        } catch (error) {
            console.log(error);
        }
    })

    it('GET /products should show all products', async () => {
        const res = await requestWithSupertest.get('/api/products');
        expect(res.status).toEqual(200);
        expect(res.type).toEqual(expect.stringContaining('json'));
        //expect(res.body).toHaveProperty('users')
    });

});