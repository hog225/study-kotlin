class Indicis {
}

data class Test(
    val a: Long,
) {}

fun varargsMethod(vararg tests: Test) {
    tests.forEach { test ->
        println(test)
    }
}

fun main(args: Array<String>) {

    var c = listOf(Test(23), Test(63))
    var result = c.indices.joinToString { i -> "(:sceneNo, :placeNo$i)" }
    println(result)

    varargsMethod(*(c.toTypedArray()))

}