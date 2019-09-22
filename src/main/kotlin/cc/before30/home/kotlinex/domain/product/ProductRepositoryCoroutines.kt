package cc.before30.home.kotlinex.domain.product

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.intellij.lang.annotations.Flow
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

/**
 *
 * ProductRepositoryCoroutines
 *
 * @author before30
 * @since 22/09/2019
 */

@Repository
class ProductRepositoryCoroutines(private val client: DatabaseClient) {

    suspend fun getProductById(id: Int): Product? {
        return client.execute("SELECT * FROM products WHERE id = $1")
                .bind(0, id)
                .`as`(Product::class.java)
                .fetch()
                .one()
                .awaitFirstOrNull()
    }

    suspend fun addNewProduct(name: String, price: Float) =
            client.execute("INSERT INTO products (name, price) VALUES($1, $2)")
                    .bind(0, name)
                    .bind(1, price)
                    .then()
                    .awaitFirstOrNull()

    @FlowPreview
    fun getAppProducts() =
            client.select()
                    .from("products")
                    .`as`(Product::class.java)
                    .fetch()
                    .all()
                    .log()
                    .asFlow()
}