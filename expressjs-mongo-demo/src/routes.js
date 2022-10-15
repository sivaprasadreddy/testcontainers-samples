const express = require('express');
const {ProductModel} = require('./model');
const router = express.Router()

router.post('/products', async (req, res) => {
    const product = new ProductModel({
        name: req.body.name,
        price: req.body.price
    })
    try {
        const savedProduct = await product.save();
        res.status(200).json(savedProduct)
    }
    catch (error) {
        res.status(400).json({message: error.message})
    }
})

router.get('/products', async (req, res) => {
    try{
        const products = await ProductModel.find();
        res.json(products)
    }
    catch(error){
        res.status(500).json({message: error.message})
    }
})

module.exports = router;