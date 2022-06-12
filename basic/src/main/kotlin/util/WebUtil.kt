package util

import Person
import java.time.Instant

fun create(xml: String): Person {
    return Person("utilname", Instant.now());
}