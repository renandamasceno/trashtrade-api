package com.trash.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val registration: Long,
    val name: String = "",
    val score: Int = 0,
    val scoreToAprove: Int = 0
)


object Users : Table() {
    val registration = long("registration")
    val name = varchar("name", 255)
    val score = integer("score")
    val scoreToAprove = integer("scoreToAprove")

    override val primaryKey: PrimaryKey = PrimaryKey(registration)

    fun toUsers(row: ResultRow): User = User(
        registration = row[registration],
        name = row[name],
        score = row[score],
        scoreToAprove = row[scoreToAprove]
    )
}
