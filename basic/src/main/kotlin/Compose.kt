class Compose {

    companion object {
        fun compose(arg1: (Int) -> Int, arg2: (Int) -> Int): (Int) -> Int = { x -> arg1(arg2(x)) }
    }

}

fun main(args: Array<String>) {

    fun square(n: Int) = n * n
    fun triple(n: Int) = n * 3


    val composeVal = Compose.compose(::square, ::triple);
    println( composeVal(2))
}

