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
                
                족발과 술병이 가득한 거실. 꽤나 마신 듯 빈 술병들이 거실에 가득하다. 얼굴이 새빨개진 채 화가 난 시온, 손에 첫눈노트를 든 채로 도준을 노려보고 있다. 당황한 도준. 둘 사이에서 어쩔 줄 모르는 지인과 수오.

                시온	이거 네가 찢었어?
                도준	(별 일 아니라는 듯) 뭐 때문인가 했네. 아 내가 아까 페이지 넘기다가 좀 찢은 것 같아. (지인, 수오의 불안한 표정을 눈치 채고 멋쩍게 웃는다)　하필 또 그 페이지가 그랬네.
                시온	뭐?
                도준	(노트를 가져가며) 이거 딱 봐도 오래된 책이잖아, 관리도 잘 안되어 있고. (페이지를 넘겨보며) 아예 새로 엮어야 하려나.
                시온	(노트를 잡으며) 언제 그랬어?
                도준	(당황하며) 응?
                시온	(도준을 노려본다)　이거 찢은 거 언제냐고.
                도준	뭐?

                시온과 도준은 서로를 노려본다. 지인은 수오를 보며 이게 뭐냐는 듯 표정을 짓고, 수오는 난감한 듯 어깨를 으쓱한다.

                도준	찢어진 거. 찢은 거 아니고.
                시온	뭐든. 그게 언제냐고. 나 계속 묻잖아.
                지인	자, 얘들아. 정리하자. 내일 일어나서 다시 이야기 하자.
                도준	나도 찢어진 줄 몰랐고, 실수였고. 근데 시간까지 어떻게 알겠어.
                시온	실수면 다야?
                수오	아이 시온아, 실수라잖아. 내가 이거 다시 붙여볼까?
                도준	됐다, 그만하자. 너 이거 너무 진지하게 믿는 거 아냐? 이거 그냥 노트야. 종이 묶음.
                지인	도준아 너까지 왜 그래. 시온이한테 중요했잖아. 조심 좀 하지. (시온에게) 시온아-
                시온	(지인 말 가로채며) 그래, 조심 좀 하지.
                도준	왜 이렇게 오바야. 이 노트가 뭐라도 돼? 이거 하나 때문에 지금 왜 이러고 있어야 하는 건데.
                시온	다른 사람들 감정, 좀 조심히 대해봐. 내가 말했잖아. 미신이든 확신이든 뭐든 다 의미부여 하는 시기라고. 내 삶이 그렇다고 했잖아. 내 감정이 그렇다고. 이게 너한테는 그냥 종이 쪼가리고 그냥 우리가 장난치는 걸로 보일지 몰라도. (시온 눈에 눈물이 고이기 시작한다.) 진짜 쪽팔리게.... 내가 이거 그냥 썼어? 이거 적었을 때, 내 마음이 그냥 한 없이 가벼워만 보였니? 너한텐 내가 다 가벼워만 보여?
                도준	알았어. 내가 실수했다. (고조된 상황을 진정시키기 위해 타이르듯)　시온아. 그래도 이건 -
                시온	(말 끊으며) 야 내 이름 그렇게 부르지 마. 진짜 짜증나. 나는 네가 어떤 상황에 처했듯, 뭘 느끼든 판단 안 하잖아. (한숨 내쉬며) 나한테 자꾸 너한테 당연한 것들 대입시키지 마.

                시온과 도준 사이 침묵이 흐른다. 시온이 노트를 상 위에 둔다. 그리고는 앞에 놓인 남은 소주를 따라본다. 잔은 반도 차지 않고, 시온은 그만큼이라도 털어 마신다.

                시온	다 마셨네.
                수오	하나 더 꺼내줄까?
                시온	됐다. (노트를 툭 치며) 이거 좋네. 내가 하자 있어서 차인 건 아니니까. (갑자기 자리에서 일어나며) 나 먼저 들어간다.
                지인	어, 그래. 자라. 늦었다.
                시온	(방으로 향하다가 뒤 돌며) 그래도 신기하지 않아? 만나고, 헤어지고. (도준을 바라보며) 타이밍 딱딱 맞게.

                시온은 방문을 닫고 들어간다. 수오는 도준의 어깨를 잡고 토닥거린다. 지인이 닫힌 시온의 방문을 한참 본다. 치즈가 탁자 위를 걸어가다가 노트를 쳐 노트가 떨어진다. 그 소리에 지인, 뭔가 떠오른 듯 떨어진 노트를 가져온다.

                지인	우리 이름 한 번 더 적어보자.
                수오	야. 그게 지금 무슨.
                지인	이게 진짜인지 아닌지, 한 번 더 해보자고. 그러면 확실해지겠지. 다른 방법 있어? 우리끼리 계속 싸울 거야?
                도준	쓸 데 없는 얘기는 둘이 해라. 나 들어간다. (방으로 들어간다)
                수오	도준아. (지인을 보며) 확인해본다고 치자. 누구 쓸 건데?
                지인	내가 적어?
                수오	너가 써보자며.
                지인	나 누구 적어? 민욱이 이름 적어봤자 뭐 확인이 되겠어? 아, 딴 남자를 적어야 되나
                수오	미쳤냐?
                지인	그러니까, 그건 아니잖아. 시온이한테 한 번 더 적어보라 그럴 수도 없고. 도준이한테 한 번 더 얘기를-
                수오	내놔. 나보고 적으라는 거잖아.

                미소를 지으며 노트를 건네는 지인. 노트를 가져가는 수오.

            """.trimIndent()
        ))
        val chatCompletionRequest = ChatCompletionRequest
            .builder()
            .model("gpt-3.5-turbo-0613")
            .messages(messages)
            .build()
        val result = openAiService.createChatCompletion(chatCompletionRequest)
        println(result)

    }
}