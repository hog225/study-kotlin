package org.yg.kotlinspring.openai.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ActiveProfiles
import org.yg.kotlinspring.openai.api.ChatGptApi
import java.nio.file.Paths

@SpringBootTest
@ActiveProfiles("chat-gpt")
class ChatGptServiceTest @Autowired constructor(
    private val chatGptService: ChatGptService
) {

    var fileId = "file-NAHIuRoyeopadiKDewQw9awE"
    var fineTuneJobId = "ftjob-LmY9Kp2xs9XAXTyEyrSqSmAD"


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
}