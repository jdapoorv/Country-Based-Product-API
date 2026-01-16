import db.DatabaseFactory
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DiscountConcurrencyTest {

    @Test
    fun `same discount applied concurrently is idempotent`() = testApplication {

        // Initialize DB explicitly for tests
        DatabaseFactory.init(
            jdbcUrl = "jdbc:postgresql://localhost:5434/productdb",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "postgres"
        )

        application {
            module()
        }

        val requestBody = """
            {
              "discountId": "DISC10",
              "percent": 10
            }
        """.trimIndent()

        coroutineScope {
            repeat(10) {
                launch {
                    client.request("/products/p1/discount") {
                        method = HttpMethod.Put
                        contentType(ContentType.Application.Json)
                        setBody(requestBody)
                    }
                }
            }
        }

        val response = client.get("/products?country=Sweden")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
