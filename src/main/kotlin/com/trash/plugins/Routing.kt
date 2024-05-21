package com.trash.plugins

import com.trash.routes.imageRoute
import com.trash.routes.userRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        userRoute()
        imageRoute()
    }
}
