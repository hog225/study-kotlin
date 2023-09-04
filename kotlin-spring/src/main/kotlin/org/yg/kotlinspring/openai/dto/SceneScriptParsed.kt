package org.yg.kotlinspring.openai.dto

@Deprecated("chatGPT 연구용, 로직에서 사용 금지")
data class SceneScriptParsed(
    val sceneTitle: String,
    val originalMessage: String,
    val stripParsed: StripElements?,
)
