package service

import db.ProductDiscounts
import db.Products
import model.Discount
import model.ProductResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object ProductService {

    private fun vat(country: String): Double =
        when (country.lowercase()) {
            "sweden" -> 0.25
            "germany" -> 0.19
            "france" -> 0.20
            else -> 0.0
        }

    fun getProducts(country: String): List<ProductResponse> = transaction {
        val products = Products.select { Products.country eq country }.toList()

        products.map { p ->
            val discounts = ProductDiscounts
                .select { ProductDiscounts.productId eq p[Products.id] }
                .map {
                    Discount(it[ProductDiscounts.discountId], it[ProductDiscounts.percent])
                }

            val totalDiscount = discounts.sumOf { it.percent } / 100
            val finalPrice =
                p[Products.basePrice] * (1 - totalDiscount) * (1 + vat(country))

            ProductResponse(
                id = p[Products.id],
                name = p[Products.name],
                basePrice = p[Products.basePrice],
                country = p[Products.country],
                discounts = discounts,
                finalPrice = finalPrice
            )
        }
    }

    fun applyDiscount(productId: String, discountId: String, percent: Double) =
        transaction {
            Products
                .select { Products.id eq productId }
                .forUpdate()
                .single()

            ProductDiscounts.insertIgnore {
                it[ProductDiscounts.productId] = productId
                it[ProductDiscounts.discountId] = discountId
                it[ProductDiscounts.percent] = percent
            }
        }
}
