package com.trash.controllers

import com.trash.models.ImageBase64
import com.trash.models.Images
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

class ImplImagesController : IImagesController {
    override fun saveImage(image: ImageBase64): String {
        return try {
            transaction {
                Images.insert {
                    it[base64Image] = image.base64Image
                    it[registration] = image.registration
                }
            }
            "Image added successfully"
        } catch (e: SQLException) {
            e.printStackTrace()
            "Error adding image: ${e.localizedMessage}"
        }
    }

    override fun getImageByRegistration(registration: Long): List<ImageBase64> {
        TODO("Not yet implemented")
    }

    override fun approveImage(registration: Long): String {
        TODO("Not yet implemented")
    }
}