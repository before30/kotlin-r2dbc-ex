package cc.before30.home.kotlinex.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

/**
 *
 * AppConfiguration
 *
 * @author before30
 * @since 22/09/2019
 */

@Configuration
class AppConfiguration {

    @Bean
    fun webClient() = WebClient.builder().baseUrl("http://localhost:8080").build()

}