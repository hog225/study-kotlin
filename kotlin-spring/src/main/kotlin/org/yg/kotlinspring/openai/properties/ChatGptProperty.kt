package org.yg.kotlinspring.openai.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "chat-gpt")
data class ChatGptProperty(
    var token: String? = null,
    var org: String? = null
) {

}
