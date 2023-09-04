package org.yg.kotlinspring.openai.dto

import org.yg.kotlinspring.openai.dto.ChatCompletion

data class ChatCompletionRequest(
    val user: String = "finch-user-t7aqwgc58c",
    val model: String,
    val messages: List<ChatCompletion.Message>,
) {
}
