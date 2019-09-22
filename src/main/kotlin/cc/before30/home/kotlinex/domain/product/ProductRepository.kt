package cc.before30.home.kotlinex.domain.product

import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 *
 * ProductRepository
 *
 * @author before30
 * @since 22/09/2019
 */

@Repository
class ProductRepository(private val client: DatabaseClient) {

    fun getProductById(id: Int): Mono<Product> {
        return client.execute("SELECT * FROM products WHERE id = $1")
                .bind(0, id)
                .`as`(Product::class.java)
                .fetch()
                .one()
    }

    fun addNewProduct(name: String, price:Float): Mono<Void> {
        return client.execute("INSERT INTO products (name, price) VALUES($1, $2)")
                .bind(0, name)
                .bind(1, price)
                .then()
    }

    fun getAllProduts(): Flux<Product> {
        return client.select().from("products")
                .`as`(Product::class.java)
                .fetch()
                .all()
    }
}