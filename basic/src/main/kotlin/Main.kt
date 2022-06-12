import util.create
import java.time.Instant

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val person = PersonTwo("bl");
    val person1 = PersonTwo("kh", Instant.now());

    println(person)

    show(listOf(person, person1))
    showTwo(listOf(person, person1))

    create("hi");

    // 코틀린에서 리스트는 기본적으로 불변이다.
    var list = listOf<String>("h", "b")
    val newList = list.plus("c")

    println(list)
    println(newList)

    // 가변
    var newMutableList: List<Int> = mutableListOf<Int>(1, 2, 3)
    println(newMutableList)

    // + 는 불변리스트를 리턴한다.
    val country = "KOR"
    val capital = when(country) {
        "AUS" -> "canberra"
        "KOR" -> "soul"
        else -> "UnKnown"
    }

    for (i in 0..10){
        println(i)
    }

    CompanionObject.create("tttt");


}