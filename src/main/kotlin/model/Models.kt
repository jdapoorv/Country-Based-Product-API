package model

import kotlinx.serialization.Serializable

@Serializable
data class DiscountRequest(
    val discountId: String,
    val percent: Double
)

@Serializable
data class Discount(
    val discountId: String,
    val percent: Double
)

@Serializable
data class ProductResponse(
    val id: String,
    val name: String,
    val basePrice: Double,
    val country: String,
    val discounts: List<Discount>,
    val finalPrice: Double
)
