package com.game.gameofthree.controller

data class CustomPage<T>(
    val number: Int,
    val size: Int,
    val totalElements: Int,
    val content: List<T>,
)
