package com.ravikrs.kmp.birdapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform