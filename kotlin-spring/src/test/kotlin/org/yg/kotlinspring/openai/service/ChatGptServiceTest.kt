package org.yg.kotlinspring.openai.service

import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService


import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ActiveProfiles
import org.yg.kotlinspring.openai.dto.ChatCompletionRequest
import org.yg.kotlinspring.openai.properties.ChatGptProperty
import java.nio.file.Paths
import java.time.Duration

@SpringBootTest
@ActiveProfiles("chat-gpt")
class ChatGptServiceTest @Autowired constructor(
    private val chatGptService: ChatGptService,
    private val chatGptProperty: ChatGptProperty
) {

    var fileId = "file-NAHIuRoyeopadiKDewQw9awE"
    var fineTuneJobId = "ftjob-LmY9Kp2xs9XAXTyEyrSqSmAD"
    val openAiService: OpenAiService = OpenAiService(chatGptProperty.token!!, Duration.ofSeconds(90))

    @Test
    fun testUploadFile() {
        val resource = ClassPathResource("fine-tune/example.jsonl")
        val path = Paths.get(resource.uri)
        val response = chatGptService.uploadFileForFineTune(path.toString())
        println(response)
    }

    @Test
    fun retrieveFineTune() {
        val response = chatGptService.retrieveFile(fileId)
        println(response)
    }

    @Test
    fun listFiles() {
        val response = chatGptService.listFiles()
        println(response)
    }

    @Test
    fun deleteFineTune() {
        val response = chatGptService.deleteFile(fileId)
        println(response)
    }

    @Test
    @DisplayName("파일에 Example 이 10개 정도 있어야 한다. ")
    fun createFineTune() {
        val response = chatGptService.createFineTuneJob(fileId)
        println(response)
        val jobs = chatGptService.listFineTurnJob()
        println(jobs)
    }

    @Test
    @DisplayName("FineTune 의 진행도 ? 를 배열로 준다. ")
    fun listFineTuneEvents() {
        val response = chatGptService.listFineTuneEvents(fineTuneJobId)
        println(response)
    }

    @Test
    @DisplayName("FineTune 모델(fineTunedModel)을 얻을 수 있다.")
    fun retrieveFineTuneJob() {
        val response = chatGptService.retrieveFineTuneJob(fineTuneJobId)
        println(response)
    }

    @Test
    @DisplayName("FineTune 모델 사용 ")
    fun useFineTuneModel() {
        val response = chatGptService.sendChatCompletion("give me umjunsik-lang data type integer 4")
        println(response)
    }

    @Test
    @DisplayName("Token Restrict")
    fun tokenRestrict() {
        val messages = listOf(
            ChatMessage(
            "user",
            """
                아래 내용은 드라마 대본의 한 씬 입니다. 아래 내용을 인물의 행동을 중심으로 80자 내로 요약 해줘. 
                
                내용 .... 

            """.trimIndent()
        ))
        val chatCompletionRequest = com.theokanning.openai.completion.chat.ChatCompletionRequest
            .builder()
            .model("gpt-3.5-turbo-0613")
            .messages(messages)
            .build()
        val result = openAiService.createChatCompletion(chatCompletionRequest)
        println(result)

    }
}