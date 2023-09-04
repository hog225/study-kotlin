package org.yg.kotlinspring.openai.config

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriComponentsBuilder

@Component
class ChatGPTClientConfig(
    private val baseWebClient: WebClient
) {

    @Bean
    fun chatGPTWebClient(): WebClient {
        val uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("api.openai.com")
            .build()
        val uriEncodingFactory = DefaultUriBuilderFactory(uriComponents.toString())
        uriEncodingFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT

        return baseWebClient.mutate()
            .uriBuilderFactory(uriEncodingFactory)
            .baseUrl(uriComponents.toString())
            .build()
    }
}
