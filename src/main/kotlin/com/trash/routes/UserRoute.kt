package com.trash.routes

import com.auth0.jwt.JWT
import com.trash.controllers.ImplUsersController
import com.trash.models.Images
import com.trash.models.User
import com.trash.models.Users
import com.trash.plugins.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.userRoute() {

    post("api/user") {
        val user = call.receive<User>()
        withContext(Dispatchers.IO) {
            ImplUsersController().addUser(user)
        }
        return@post call.respond(HttpStatusCode.Created)
    }

    get("api/user") {
        withContext(Dispatchers.IO) {
            val users = transaction {
                Users.selectAll().map { Users.toUsers(it) }
            }
            if (users.isNotEmpty()) call.respond(HttpStatusCode.OK, users)
            else call.respond(HttpStatusCode.NotFound)
        }
    }

    delete("api/user/{registration}") {
        val registration = call.parameters["registration"]
        if (registration == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing or invalid registration")
            return@delete
        }

        withContext(Dispatchers.IO) {
            val deleted = transaction {
                Users.deleteWhere { Users.registration eq registration.toLong() }
            }

            if (deleted > 0) {
                call.respond(HttpStatusCode.OK, "User deleted successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }

    post("/api/login") {
        val credentials = call.receive<User>()
        val user = transaction {
            Users.select { Users.registration eq credentials.registration }
                .mapNotNull { Users.toUsers(it) }
                .singleOrNull()
        }
        if (user != null) {
            val token = JWT.create()
                .withSubject("Authentication")
                .withIssuer(JwtConfig.issuer)
                .withClaim("registration", user.registration)
                .sign(JwtConfig.algorithm)
            call.respond(mapOf("token" to token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Matrícula inválida!")
        }
    }

    patch("api/addScore") {
        val scoreReceive = call.receive<User>()
        withContext(Dispatchers.IO) {
            val isExistingUser = transaction {
                Users.select { Users.registration eq scoreReceive.registration }
                    .firstNotNullOfOrNull { Users.toUsers(it) }
            }
            if (isExistingUser != null) {
                transaction {
                    Users.update({ Users.registration eq scoreReceive.registration }) {
                        with(SqlExpressionBuilder) {
                            it.update(scoreToAprove, (scoreToAprove + (scoreReceive.scoreToAprove * 3)))
                        }
                    }
                }
                call.respond(HttpStatusCode.OK, "Score updated successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }

    patch("api/approveScore") {
        val scoreReceive = call.receive<User>()
        withContext(Dispatchers.IO) {
            val isExistingUser = transaction {
                Users.select { Users.registration eq scoreReceive.registration }
                    .firstNotNullOfOrNull { Users.toUsers(it) }
            }
            if (isExistingUser != null) {
                transaction {
                    Users.select { Users.registration eq scoreReceive.registration }
                        .map { Users.toUsers(it) }
                        .singleOrNull()?.let { user ->
                            Users.update({ Users.registration eq scoreReceive.registration }) {
                                it[score] = user.score + user.scoreToAprove
                                it[scoreToAprove] = 0
                            }
                        }
                    Images.deleteWhere { registration eq scoreReceive.registration }
                }
                call.respond(HttpStatusCode.OK, "Score approved successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }

    patch("api/cleanAllScores") {
        withContext(Dispatchers.IO) {
            transaction {
                Users.update({ Op.TRUE }) {
                    it[score] = 0
                    it[scoreToAprove] = 0
                }
            }
            call.respond(HttpStatusCode.OK, "All scores cleaned successfully")
        }
    }

}