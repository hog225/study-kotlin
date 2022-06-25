package org.yg.kotlinspring.crew.department

import org.yg.kotlinspring.crew.user.User
import org.yg.kotlinspring.model.NamedEntity
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class Department(
    @Id var id: Long,
    @ManyToMany(mappedBy = "departments")
    private var users: List<User>
): NamedEntity()