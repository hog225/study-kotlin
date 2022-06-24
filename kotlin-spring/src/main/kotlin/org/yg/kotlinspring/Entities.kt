package org.yg.kotlinspring

import javax.persistence.*

@Entity
class User(
    var name: String,
    var headline: String,
    var content: String,
    @OneToMany(fetch = FetchType.LAZY)
    var roles: List<Role>,
    @ManyToMany
    @JoinTable(
        name = "user_department",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "department_id", referencedColumnName = "id")]
    )
    var departments: List<Department>,
    @Id @GeneratedValue var id: Long? = null)

@Entity
class Role(
    @Id var id: Long,
    var name: String
)

@Entity
class Department(
    @Id var id: Long,
    var name: String,
    @ManyToMany(mappedBy = "departments")
    private var users: List<User>
)

@Entity
class Contents(
    @Id var id: Long,
    var Content: String
)