package cc.before30.home.kotlinex.controller

import cc.before30.home.kotlinex.domain.product.Product

/**
 *
 * ProductStockView
 *
 * @author before30
 * @since 22/09/2019
 */
class ProductStockView(product: Product, var quantity: Integer?) {
    var id: Int = 0
    var name: String = ""
    var price: Float = 0.0f

    init {
        this.id = product.id
        this.name = product.name
        this.price = product.price
    }
}