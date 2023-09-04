package org.yg.kotlinspring.openai.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

data class ChatCompletion(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>

) {
    @JsonNaming(value= PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Usage(
        val promptTokens: Int,
        val completionTokens: Int,
        val totalTokens: Int
    )

    @JsonNaming(value= PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Choice(
        val index: Int,
        val message: Message,
        val finishReason: String
    )

    data class Message(
        val role: String,
        val content: String
    )

}
