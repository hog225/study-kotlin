package org.yg.kotlinspring.crew.role

import org.yg.kotlinspring.model.NamedEntity
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Role(
    @Id var id: Long,

): NamedEntity()