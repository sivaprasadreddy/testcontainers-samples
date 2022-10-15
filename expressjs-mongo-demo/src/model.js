const mongoose = require('mongoose');

const productSchema = new mongoose.Schema({
    name: {
        required: true,
        type: String
    },
    price: {
        required: true,
        type: Number
    }
})

const ProductModel = mongoose.model('Product', productSchema);
module.exports = {
    ProductModel
}