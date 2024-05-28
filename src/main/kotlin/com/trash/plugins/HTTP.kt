package com.trash.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("Authorization")
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.ContentLength)
    }

    install(DefaultHeaders) {
        header("Content-Security-Policy", "default-src 'self'; connect-src 'self' https://trashtrade.onrender.com")
    }
}
