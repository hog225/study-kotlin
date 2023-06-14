import java.util.stream.Collectors.toList

fun main() {

    // 0, 1, 2, 3, 4
    var partOrderLists = IntRange(2 + 1, 4).toMutableList()
    val data = partOrderLists.map{ order ->
        order to order -1
    }

    println(data)

}


