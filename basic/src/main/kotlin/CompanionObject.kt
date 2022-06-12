import java.time.ZonedDateTime

class CompanionObject(
        val name: String,
        val dat: ZonedDateTime
) {
        /**
         * Static 대신 사용 가능
         */
        companion object {
                fun create(xml: String): CompanionObject {
                        return CompanionObject("name", ZonedDateTime.now());
                }
        }
}

fun main(array: Array<String>) {
        CompanionObject.create("건들지마 ")
}