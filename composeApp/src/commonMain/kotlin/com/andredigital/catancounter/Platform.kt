package com.andredigital.catancounter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform