package db

import org.jetbrains.exposed.sql.Table

object Products : Table("products") {
    val id = varchar("id", 50)
    val name = varchar("name", 255)
    val basePrice = double("base_price")
    val country = varchar("country", 50)
    override val primaryKey = PrimaryKey(id)
}

object ProductDiscounts : Table("product_discounts") {
    val productId = varchar("product_id", 50)
    val discountId = varchar("discount_id", 50)
    val percent = double("percent")

    override val primaryKey = PrimaryKey(productId, discountId)
}
