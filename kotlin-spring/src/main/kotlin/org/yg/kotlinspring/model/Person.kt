package org.yg.kotlinspring.model


import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotEmpty

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 * @author Antoine Rey
 */
@MappedSuperclass
open class Person : BaseEntity() {

    @Column(name = "first_name")
    @NotEmpty
    var firstName = ""

    @Column(name = "last_name")
    @NotEmpty
    var lastName = ""

}
