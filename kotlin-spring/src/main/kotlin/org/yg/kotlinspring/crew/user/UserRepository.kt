package org.yg.kotlinspring.crew.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.util.*

// TODO 여기서 부터
interface UserRepository : JpaRepository<User, Long> {
//    @Query("SELECT DISTINCT u FROM User u left join fetch u.roles left join fetch u.departments WHERE u.lastName LIKE :lastName%")
//    @Transactional(readOnly = true)
//    fun findByLastName(lastName: String): Collection<User>

//    @Query("SELECT DISTINCT u FROM User u left join fetch u.roles left join fetch u.departments WHERE u.id = :id")
//    @Transactional(readOnly = true)
//    override fun findById(id: Long): User?
}