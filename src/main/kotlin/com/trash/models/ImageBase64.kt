package com.trash.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class ImageBase64(
    val base64Image: String,
    val registration: Long,
    val numberBatteries: Int
)

object Images : Table() {
    val base64Image = varchar("image", 1024)
    val registration = long("registration")
    val numberBatteries = integer("numberBatteries")

    override val primaryKey: PrimaryKey = PrimaryKey(registration)

    fun toImages(row: ResultRow): ImageBase64 = ImageBase64(
        base64Image = row[base64Image],
        registration = row[registration],
        numberBatteries = row[numberBatteries]
    )
}