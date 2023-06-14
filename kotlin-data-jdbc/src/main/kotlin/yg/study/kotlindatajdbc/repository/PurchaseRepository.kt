package yg.study.kotlindatajdbc.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import yg.study.kotlindatajdbc.model.Purchase

interface PurchaseRepository: PagingAndSortingRepository<Purchase, Long> {

    fun findAllByAddr(addr: String): List<Purchase>?
}