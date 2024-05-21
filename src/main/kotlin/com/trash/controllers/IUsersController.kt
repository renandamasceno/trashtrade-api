package com.trash.controllers

import com.trash.models.User

interface IUsersController {
    fun addUser(user: User): String
    fun loginUser(registration: Long): User?
    fun addScore(registration: Long, score: Int): String
    fun aprrovesScore(registration: Long): String
}