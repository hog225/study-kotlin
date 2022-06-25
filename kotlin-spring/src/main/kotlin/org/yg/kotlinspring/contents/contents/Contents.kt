package org.yg.kotlinspring.contents.contents

import org.yg.kotlinspring.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Contents(
    @Id var id: Long,
    var Content: String
): BaseEntity()