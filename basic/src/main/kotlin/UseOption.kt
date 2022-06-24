import util.Option
import util.create
import java.time.Instant
import java.util.Collections.max

fun main(args: Array<String>) {

    println(Option.invoke(4).isEmpty())
    println( Option.None.toString())

    fun getDefault(): Int = throw RuntimeException();
    val myOption = Option(10)

    val mySome = Option.Some(10)
    mySome.getValue()
//
//    val val1 = Option(max(listOf(1, 2, 5))).getOrElse(throw RuntimeException())
//    println(val1)


    val k = Option<Int>(4)
    val newK = k.map { value -> value + 1 }
    println(newK)

}