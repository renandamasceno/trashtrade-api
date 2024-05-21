package com.trash.routes

import com.trash.models.ImageBase64
import com.trash.models.Images
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.imageRoute() {
    post("/api/image") {
        val imageReceive = call.receive<ImageBase64>()
        withContext(Dispatchers.IO) {
            transaction {
                Images.insert {
                    it[base64Image] = imageReceive.base64Image
                    it[registration] = imageReceive.registration
                    it[numberBatteries] = imageReceive.numberBatteries
                }
            }
            call.respond(HttpStatusCode.Created, "Image saved successfully")
        }
    }

    get("/api/image/{registration}") {
        withContext(Dispatchers.IO) {
            val registration = call.parameters["registration"]
            if (registration != null) {
                val imagesBase64 = transaction {
                    Images.select { Images.registration eq registration.toLong()}
                        .map { Images.toImages(it) }
                }
                call.respond(imagesBase64)
            } else {
                call.respondText("Registration parameter is missing", status = HttpStatusCode.BadRequest)
            }
        }
    }
}