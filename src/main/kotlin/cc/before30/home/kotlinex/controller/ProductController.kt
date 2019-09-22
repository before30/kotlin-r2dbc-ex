package cc.before30.home.kotlinex.controller

import cc.before30.home.kotlinex.domain.product.Product
import cc.before30.home.kotlinex.domain.product.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 *
 * PorductController
 *
 * @author before30
 * @since 22/09/2019
 */
@RestController
@RequestMapping("/v1")
class ProductController(val webClient: WebClient,
                        val productRepository: ProductRepository) {
    val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: Int): Mono<Product> {
        return productRepository.getProductById(id)
    }

    @GetMapping("/{id}/stock")
    fun findOneInStock(@PathVariable id: Int): Mono<ProductStockView> {
        val product = productRepository.getProductById(id)
        val stockQuantity = webClient.get().uri("/v1/stock-service/product/$id/quantity")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Integer::class.java)
        return product.zipWith(stockQuantity) {
            productInStock, stockQty -> ProductStockView(productInStock, stockQty)
        }
    }

    @GetMapping("/stock-service/product/{id}/quantity")
    fun getStockQuantity(): Mono<Int> {
        logger.info("getStockQuantity")
        return Mono.just(2)
    }

    @GetMapping("/")
    fun findAll(): Flux<Product> {
        return productRepository.getAllProduts()
    }
}