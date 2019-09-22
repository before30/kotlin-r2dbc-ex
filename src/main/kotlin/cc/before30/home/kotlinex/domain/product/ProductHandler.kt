package cc.before30.home.kotlinex.domain.product

import cc.before30.home.kotlinex.controller.ProductStockView
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.json

/**
 *
 * ProductHandler
 *
 * @author before30
 * @since 22/09/2019
 */

@Component
class ProductHandler(
        val webClient: WebClient,
        val productRepository: ProductRepositoryCoroutines) {

    @FlowPreview
    suspend fun findAll(request: ServerRequest): ServerResponse =
            ServerResponse.ok().json().bodyAndAwait(productRepository.getAppProducts())

    suspend fun findOneInStock(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toInt()
        val product: Deferred<Product?> = GlobalScope.async {
            productRepository.getProductById(id)
        }

        val quantity: Deferred<Integer> = GlobalScope.async {
            webClient.get()
                    .uri("/v2/stock-service/product/$id/quantity")
                    .accept(MediaType.APPLICATION_JSON)
                    .awaitExchange()
                    .awaitBody<Integer>()
        }

        return ServerResponse.ok().json()
                .bodyAndAwait(ProductStockView(product.await()!!, quantity.await()))
    }

    suspend fun findOne(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toInt()
        return ServerResponse.ok().json().bodyAndAwait(productRepository.getProductById(id)!!)
    }
}