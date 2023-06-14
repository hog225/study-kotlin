import java.util.stream.Collectors.toList

fun main() {

    val request = Request(0, 3)



    val originalOrders = IntRange(0,5 - 1).toList()
    var allOrderList = originalOrders.toMutableList()

    if (request.fromOrder < request.toOrder) {
        allOrderList.add(request.toOrder + 1, request.fromOrder)
        allOrderList.removeAt(request.fromOrder)
    } else {
        allOrderList.add(request.toOrder, request.fromOrder)
        allOrderList.removeAt(request.fromOrder + 1)
    }

    println(originalOrders)
    println(allOrderList)

    var changedData = allOrderList.mapIndexed { idx, order ->
        order to idx
    }.filter { it.first != it.second }
    println(changedData)

}


class Request(
    val fromOrder: Int,
    val toOrder: Int
) {

}
