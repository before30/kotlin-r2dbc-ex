package cc.before30.home.kotlinex.controller

import cc.before30.home.kotlinex.domain.product.ProductHandler
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

/**
 *
 * ProductRouter
 *
 * @author before30
 * @since 22/09/2019
 */
@Configuration
class ProductRouter {

    @FlowPreview
    @Bean
    fun productRoutes(productHandler: ProductHandler) = coRouter {
        GET("/v2/", productHandler::findAll)
        GET("/v2/{id}", productHandler::findOne)
        GET("/v2/{id}/stock", productHandler::findOneInStock)
    }
}