import db.DatabaseFactory
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.serialization.kotlinx.json.json
import routes.productRoutes

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init(
        jdbcUrl = "jdbc:postgresql://localhost:5434/productdb",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )

    install(ContentNegotiation) {
        json()
    }

    routing {
        productRoutes()
    }
}
