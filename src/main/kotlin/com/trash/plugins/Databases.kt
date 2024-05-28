package com.trash.plugins

import com.trash.models.Images
import com.trash.models.Users
import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    Database.connect(
        url = System.getenv("DATABASE_URL"),
        user = System.getenv("DATABASE_USER"),
        driver = System.getenv("DATABASE_DRIVER"),
        password = System.getenv("DATABASE_PASSWORD")
    )

    Flyway.configure()
        .dataSource(
            System.getenv("DATABASE_URL"),
            System.getenv("DATABASE_USER"),
            System.getenv("DATABASE_PASSWORD")
        )
        .locations("classpath:db/migration")
        .baselineOnMigrate(true)
        .load()
        .migrate()
    transaction {
        SchemaUtils.create(Users, Images)
    }
}
