package org.yg.kotlinspring.openai.service

import com.google.gson.Gson
import com.theokanning.openai.completion.chat.ChatCompletionResult
import com.theokanning.openai.completion.chat.ChatFunction
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

//    fun sendFunctionCalling(): ChatCompletionResult {
//        val content = """
//            족발과 술병이 가득한 거실. 꽤나 마신 듯 빈 술병들이 거실에 가득하다. 얼굴이 새빨개진 채 화가 난 시온, 손에 첫눈노트를 든 채로 도준을 노려보고 있다. 당황한 도준. 둘 사이에서 어쩔 줄 모르는 지인과 수오.
//
//            시온	이거 네가 찢었어?
//            도준	(별 일 아니라는 듯) 뭐 때문인가 했네. 아 내가 아까 페이지 넘기다가 좀 찢은 것 같아. (지인, 수오의 불안한 표정을 눈치 채고 멋쩍게 웃는다)　하필 또 그 페이지가 그랬네.
//            시온	뭐?
//            도준	(노트를 가져가며) 이거 딱 봐도 오래된 책이잖아, 관리도 잘 안되어 있고. (페이지를 넘겨보며) 아예 새로 엮어야 하려나.
//            시온	(노트를 잡으며) 언제 그랬어?
//            도준	(당황하며) 응?
//            시온	(도준을 노려본다)　이거 찢은 거 언제냐고.
//            도준	뭐?
//
//            시온과 도준은 서로를 노려본다. 지인은 수오를 보며 이게 뭐냐는 듯 표정을 짓고, 수오는 난감한 듯 어깨를 으쓱한다.
//
//            도준	찢어진 거. 찢은 거 아니고.
//            시온	뭐든. 그게 언제냐고. 나 계속 묻잖아.
//            지인	자, 얘들아. 정리하자. 내일 일어나서 다시 이야기 하자.
//            도준	나도 찢어진 줄 몰랐고, 실수였고. 근데 시간까지 어떻게 알겠어.
//            시온	실수면 다야?
//            수오	아이 시온아, 실수라잖아. 내가 이거 다시 붙여볼까?
//            도준	됐다, 그만하자. 너 이거 너무 진지하게 믿는 거 아냐? 이거 그냥 노트야. 종이 묶음.
//            지인	도준아 너까지 왜 그래. 시온이한테 중요했잖아. 조심 좀 하지. (시온에게) 시온아-
//            시온	(지인 말 가로채며) 그래, 조심 좀 하지.
//            도준	왜 이렇게 오바야. 이 노트가 뭐라도 돼? 이거 하나 때문에 지금 왜 이러고 있어야 하는 건데.
//            시온	다른 사람들 감정, 좀 조심히 대해봐. 내가 말했잖아. 미신이든 확신이든 뭐든 다 의미부여 하는 시기라고. 내 삶이 그렇다고 했잖아. 내 감정이 그렇다고. 이게 너한테는 그냥 종이 쪼가리고 그냥 우리가 장난치는 걸로 보일지 몰라도. (시온 눈에 눈물이 고이기 시작한다.) 진짜 쪽팔리게.... 내가 이거 그냥 썼어? 이거 적었을 때, 내 마음이 그냥 한 없이 가벼워만 보였니? 너한텐 내가 다 가벼워만 보여?
//            도준	알았어. 내가 실수했다. (고조된 상황을 진정시키기 위해 타이르듯)　시온아. 그래도 이건 -
//            시온	(말 끊으며) 야 내 이름 그렇게 부르지 마. 진짜 짜증나. 나는 네가 어떤 상황에 처했듯, 뭘 느끼든 판단 안 하잖아. (한숨 내쉬며) 나한테 자꾸 너한테 당연한 것들 대입시키지 마.
//        """.trimIndent()
//
//        val query = "Summarize the sentence below \n$content"
//        val messages = listOf(
//            ChatMessage("user", content),
//        )
//
//        val chatFunc = ChatFunction.builder()
//            .name("summarize")
//            .description("summarize the sentence")
//            .executor()
//            .build()
//
//        openAiService.createChatCompletion(com.theokanning.openai.completion.chat.ChatCompletionRequest.builder()
//            .messages(messages)
//            .functions()
//            .functionCall(com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
//            .build())
//    }

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
