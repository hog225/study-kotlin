// Levenshtein 거리 계산
fun main() {
    val words = listOf("사과", "수박", "바나나", "사자", "배", "산", "소금", "바람")

    // 첫 글자를 기준으로 단어들을 그룹화
    val groupedWords = words.groupBy { it[0] }

    // 결과 출력
    groupedWords.forEach { (firstLetter, words) ->
        println("'${firstLetter}'로 시작하는 단어: $words")
    }
}