package yg.study.kotlindatajdbc

import org.springframework.stereotype.Component

import yg.study.kotlindatajdbc.model.Purchase
import yg.study.kotlindatajdbc.repository.PurchaseRepository
import javax.annotation.PostConstruct

@Component
class TestCaseOne(
    val purchaseRepository: PurchaseRepository
) {

    @PostConstruct
    fun test() {
        println()
        var purchase = purchaseRepository.save(Purchase().apply {
            addr = "seoul !!!"
        })
        println(purchase)
        println( purchaseRepository.findAllByAddr("seoul !!!") )
    }
}