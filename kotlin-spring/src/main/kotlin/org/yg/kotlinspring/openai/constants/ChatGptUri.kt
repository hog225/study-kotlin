package org.yg.kotlinspring.openai.constants

enum class ChatGptUri(
    val uri: String
) {
    COMPLETION("/v1/completions"),
    FINETUNE_JOBS("/v1/fine_tuning/jobs"),
    FILES("/v1/files"),
    CHAT_COMPLETION("/v1/chat/completions");

}
