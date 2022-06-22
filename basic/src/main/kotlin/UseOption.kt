import util.Option
import util.create
import java.time.Instant

fun main(args: Array<String>) {

    println(Option.invoke(4).isEmpty())
    println( Option.None.toString())

    val myOption = Option(10)
    val mySome = Option.Some(10)
    mySome.getValue()


}