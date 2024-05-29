package com.trash.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class ImageBase64(
    val id: Int? = null,
    val base64Image: String,
    val registration: Long,
    val numberBatteries: Int
)

object Images : Table() {
    private val id = integer("id").autoIncrement()
    val base64Image = text("image")
    val registration = long("registration")
    val numberBatteries = integer("numberBatteries")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun toImages(row: ResultRow): ImageBase64 = ImageBase64(
        id = row[id],
        base64Image = row[base64Image],
        registration = row[registration],
        numberBatteries = row[numberBatteries]
    )
}
