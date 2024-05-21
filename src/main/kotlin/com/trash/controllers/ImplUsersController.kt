package com.trash.controllers

import com.trash.models.User
import com.trash.models.Users
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.sql.SQLException

class ImplUsersController : IUsersController {
    override fun addUser(user: User): String {
        return try {
            transaction {
                Users.insert {
                    it[registration] = user.registration
                    it[name] = user.name
                    it[score] = user.score
                    it[scoreToAprove] = user.scoreToAprove
                }
            }
            "User added successfully"
        } catch (e: SQLException) {
            e.printStackTrace()
            "Error adding user: ${e.localizedMessage}"
        }
    }

    override fun loginUser(registration: Long): User? {
        return try {
            transaction {
                Users.select { Users.registration eq registration }
                    .map { Users.toUsers(it) }
                    .singleOrNull()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    override fun addScore(registration: Long, score: Int): String {
        return try {
            transaction {
                Users.update({ Users.registration eq registration }) {
                    with(SqlExpressionBuilder) {
                        it.update(Users.scoreToAprove, Users.scoreToAprove + score)
                    }
                }
            }
            "Score added successfully"
        } catch (e: SQLException) {
            e.printStackTrace()
            "Error adding score: ${e.localizedMessage}"
        }
    }

    override fun aprrovesScore(registration: Long): String {
        return try {
            transaction {
                Users.select { Users.registration eq registration }
                    .map { Users.toUsers(it) }
                    .singleOrNull()?.let { user ->
                        Users.update({ Users.registration eq registration }) {
                            it[Users.score] = user.score + user.scoreToAprove
                            it[Users.scoreToAprove] = 0
                        }
                    }
            }
            "Score approved successfully"
        } catch (e: SQLException) {
            e.printStackTrace()
            "Error approving score: ${e.localizedMessage}"
        }
    }
}
