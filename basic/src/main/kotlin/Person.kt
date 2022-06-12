import java.io.Serializable
import java.time.Instant

class Person(
    val name: String,
    val registered: Instant
) :Serializable, Comparable<Person>{
    override fun compareTo(other: Person): Int {
        return 1;
//        TODO("Not yet implemented")

    }


}