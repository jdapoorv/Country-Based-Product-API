package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.DiscountRequest
import model.ProductResponse
import service.ProductService

fun Route.productRoutes() {

    get("/products") {
        val country = call.request.queryParameters["country"]?.trim()
            ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "country is required"
            )

        val products = ProductService.getProducts(country)

        call.respond(products)

    }

    put("/products/{id}/discount") {
        val productId = call.parameters["id"]!!
        val req = call.receive<DiscountRequest>()

        ProductService.applyDiscount(productId, req.discountId, req.percent)
        call.respondText("OK")
    }
}
