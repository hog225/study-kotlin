import java.time.Instant
import java.util.*

data class PersonTwo(
    val name: String,
    val registered: Instant = Instant.now()
)

fun show(persons: List<PersonTwo>) {
    for ((name, registered) in persons) {
        println("$name  $registered")
    }

}

fun showTwo(persons: List<PersonTwo>) {
    for (person in persons) {
        println("${person.component1()}  ${person.component2()}")
    }

}

fun getNameYg(person: PersonTwo): Boolean {
    val k = if (person.name == "yg")
        true
    else
        false
    return k;
}

fun main() {
    var koList = ArrayList<String>();
    koList.add("power")
    koList.add("man")
    println(koList)

}
