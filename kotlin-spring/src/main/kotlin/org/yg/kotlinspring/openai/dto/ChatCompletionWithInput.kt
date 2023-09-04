package org.yg.kotlinspring.openai.dto

import org.yg.kotlinspring.openai.dto.ChatCompletion

data class ChatCompletionWithInput(
    val sceneTitle: String,
    val originalMessage: String,
    val chatCompletion: ChatCompletion?,
)
