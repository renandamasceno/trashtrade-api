package com.trash.controllers

import com.trash.models.ImageBase64
import javax.imageio.ImageIO

interface IImagesController {
    fun saveImage(image: ImageBase64): String
    fun getImageByRegistration(registration: Long): List<ImageBase64>
    fun approveImage(registration: Long): String
}