import util.create
import java.time.Instant

fun main(args: Array<String>) {

    val tripleList = listOf(
        Triple(1, 2, 3),
        Triple(4, 5, 6),
        Triple(7, 8, 9)
    )

    val tripleListArray = tripleList.map {
        mapOf(
            "a" to it.first,
            "b" to it.second,
            "c" to it.third
        )
    }.toTypedArray()

    tripleListArray.forEach { println(it) }


}