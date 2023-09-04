package org.yg.kotlinspring.openai.service

import com.google.gson.Gson
import com.theokanning.openai.completion.chat.ChatCompletionResult
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.file.File
import com.theokanning.openai.fine_tuning.FineTuningEvent
import com.theokanning.openai.fine_tuning.FineTuningJob
import com.theokanning.openai.fine_tuning.FineTuningJobRequest
import com.theokanning.openai.service.OpenAiService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.yg.kotlinspring.openai.api.ChatGptApi
import org.yg.kotlinspring.openai.dto.*
import org.yg.kotlinspring.openai.properties.ChatGptProperty
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime

@Service
class ChatGptService(
    private val chatGptApi: ChatGptApi,
    private val chatGptProperty: ChatGptProperty
) {

    val log = KotlinLogging.logger { }
    private val openAiService: OpenAiService = OpenAiService(chatGptProperty.token!!, Duration.ofSeconds(90))

    fun uploadFileForFineTune(file: String): File {
        return openAiService.uploadFile("fine-tune", file)

    }

    fun deleteFile(fileId: String): Boolean {
        val result = openAiService.deleteFile(fileId)
        log.info { "delete file result : $result" }
        return result.isDeleted
    }

    fun retrieveFile(fileId: String): File {
        return openAiService.retrieveFile(fileId)
    }

    fun listFiles(): List<File> {
        return openAiService.listFiles()
    }

    fun createFineTuneJob(fileId: String): FineTuningJob {
        val fineTuneRequest= FineTuningJobRequest.builder()
            .trainingFile(fileId)
            .model("gpt-3.5-turbo")
            .build()
        return openAiService.createFineTuningJob(fineTuneRequest);
    }

    fun listFineTurnJob(): List<FineTuningJob> {
        return openAiService.listFineTuningJobs()
    }

    fun listFineTuneEvents(jobId: String): List<FineTuningEvent> {
        return openAiService.listFineTuningJobEvents(jobId)
    }

    fun retrieveFineTuneJob(jobId: String): FineTuningJob {
        return openAiService.retrieveFineTuningJob(jobId)
    }

    fun sendChatCompletion(message: String): ChatCompletionResult {
        val request = com.theokanning.openai.completion.chat.ChatCompletionRequest.builder()
            .model("ft:gpt-3.5-turbo-0613:finch::7v6EdjuE")
            .messages(listOf(ChatMessage(
                "user",
                message
            )))
            .build()
        return openAiService.createChatCompletion(request)
    }

    fun sendStripParsingQuery(sceneNo: Long, stripOfScene: String): Mono<List<SceneStripElements>> {
        log.info { "sendStripParsingQuery start ${LocalDateTime.now()} sceneNo : $sceneNo" }
        val queryMessages = this.buildQueryMessage(stripOfScene)
        val results = queryMessages.map {
            val request = ChatCompletionRequest(model = "gpt-3.5-turbo", messages = listOf(ChatCompletion.Message("user", it)))
            chatGptApi.createChatCompletion(request).map {completion ->
                log.info { "used token : ${completion?.usage?.totalTokens} sceneNo : $sceneNo" }
                val content = completion.choices[0].message.content
                val stripElement = try {
                    Gson().fromJson(content, StripElements::class.java)
                } catch (e: Exception) {
                    //log.info { "query Message ${queryMessages[index]}" }
                    log.info { "result Json String $it" }
                    e.printStackTrace()
                    StripElements()
                }
                SceneStripElements(sceneNo, stripElement)
            }.onErrorReturn(SceneStripElements(sceneNo, StripElements()))
        }
        return Flux.fromIterable(results)
            .flatMap { it }
            .collectList()

    }

    fun sendStripSummaryQuery(sceneNo: Long, stripOfScene: String): Mono<List<SceneSummarized>> {
        log.info { "sendStripSummaryQuery start ${LocalDateTime.now()}" }
        val queryMessages = this.buildQueryMessage(stripOfScene, "summary")
        val results = queryMessages.map {
            val request = ChatCompletionRequest(model = "gpt-3.5-turbo", messages = listOf(ChatCompletion.Message("user", it)))
            chatGptApi.createChatCompletion(request).map { completion ->
                log.info { "total used token : ${completion?.usage?.totalTokens} ${LocalDateTime.now()} sceneNo : $sceneNo" }
                val content = completion.choices[0].message.content
                SceneSummarized(sceneNo, content)
            }.onErrorReturn(SceneSummarized(sceneNo, ""))
        }
        return Flux.fromIterable(results)
            .flatMap { it }
            .collectList()
    }

    private fun splitScript(script: String, unit: Int): List<String> {
        val splitScripts = script.split("\n")
        var chunkScripts = mutableListOf("")
        var idx = 0
        for ((index, line) in splitScripts.withIndex()) {
            if ((chunkScripts[idx].length + splitScripts[index].length) < unit) {
                chunkScripts[idx] += line + "\n"
            } else {
                chunkScripts.add(line + "\n")
                idx++
            }
        }
        return chunkScripts
    }


    private fun buildQueryMessage(sceneStrip: String, type: String? = null): List<String> {
        var chunkedStrips = if (sceneStrip.length > WORD_LENGTH_THRESHOLD) {
            this.splitScript(sceneStrip, WORD_LENGTH_THRESHOLD)
        } else {
            listOf(sceneStrip)
        }

        return chunkedStrips.map {
            if (type == "summary") getSummaryQuery(it) else getQuery(it)
        }

    }

    private fun getQuery(sceneStrip: String): String {
        val messageTemplate = """
            "===Script==="와 "===Script End===" 사이의 텍스트는 한국 드라마 대본의 일부 이다.
            대본에서 언급된 소품(props), 의상(costumes), 메이크업(makeups) 으로 보이는 단어를 찾아 JSON 형식으로 제공 하시오.
            단어는 25자를 넘지 않아야 한다.
            example :
             {
               "props":["String",...],
               "costumes":["String",...],
               "makeup":["String",...]
             }

            ===Script===
            $sceneStrip
            ===Script End===
        """.trimIndent()
        return messageTemplate
    }

    private fun getSummaryQuery(sceneStrip: String): String {
        val messageTemplate = """
            아래 내용은 드라마 대본의 한 씬 입니다. 아래 내용을 인물의 행동을 중심으로 80자 내로 요약 해줘. (요약 내용은 80자를 넘지 않아야 함)
            만약 내용이 없으면 빈 문자열을 반환 해줘.

            $sceneStrip
        """.trimIndent()
        return messageTemplate
    }


    companion object {
        const val WORD_LENGTH_THRESHOLD = 3000
    }

}
