package org.yg.kotlinspring.openai.api


import org.yg.kotlinspring.openai.constants.ChatGptUri
import org.yg.kotlinspring.openai.dto.ChatCompletion
import org.yg.kotlinspring.openai.dto.ChatCompletionRequest
import org.yg.kotlinspring.openai.properties.ChatGptProperty
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ChatGptApi(
    private val chatGptProperty: ChatGptProperty,
    private val chatGPTWebClient: WebClient,
) {
    private val log = KotlinLogging.logger {  }

    fun createChatCompletion(request: ChatCompletionRequest): Mono<ChatCompletion> {
        return chatGPTWebClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path(ChatGptUri.CHAT_COMPLETION.uri)
                    .build()
            }.bodyValue(request)
            .headers {
                it.setBearerAuth(chatGptProperty.token!!)
                it.set("OpenAI-Organization", chatGptProperty.org!!)
                it.set("Content-Type", "application/json")
            }.retrieve()
            .bodyToMono(ChatCompletion::class.java)
    }




}
