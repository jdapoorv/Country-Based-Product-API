package db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(
        jdbcUrl: String,
        driver: String,
        user: String,
        password: String
    ) {
        Database.connect(
            url = jdbcUrl,
            driver = driver,
            user = user,
            password = password
        )

        transaction {
            SchemaUtils.create(Products, ProductDiscounts)
        }
    }
}
