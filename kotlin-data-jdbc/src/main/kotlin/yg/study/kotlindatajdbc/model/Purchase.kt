package yg.study.kotlindatajdbc.model


import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table

@Table(name = "purchase")
data class Purchase(
    @Id var purchaseNo: Long? = null,
    var addr: String? = null,
    //orders: Set<OrderItem>  = HashSet<>();
) {
}
