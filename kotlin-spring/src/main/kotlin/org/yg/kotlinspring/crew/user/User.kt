package org.yg.kotlinspring.crew.user


import lombok.NoArgsConstructor
import org.yg.kotlinspring.crew.department.Department
import org.yg.kotlinspring.crew.role.Role
import org.yg.kotlinspring.model.Person
import java.util.*
import javax.persistence.*

@Entity
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToMany(fetch = FetchType.LAZY)
    var roles: List<Role>,
    @ManyToMany
    @JoinTable(
        name = "user_department",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "department_id", referencedColumnName = "id")]
    )
    var departments: List<Department>,
) : Person() {

    constructor() : this(null, Collections.emptyList(), Collections.emptyList(),) {


    }

    val isNew: Boolean
        get() = this.id == null
}
//
//fun main() {
//    var a = User();
//    println(a.firstName)
//}

