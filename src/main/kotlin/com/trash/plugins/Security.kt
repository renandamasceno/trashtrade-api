package com.trash.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*

object JwtConfig {
    private const val secret = "trashtrade-secret"
    const val issuer = "com.trash"
    val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

}

fun Application.configureSecurity() {
}
