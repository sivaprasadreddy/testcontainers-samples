package com.sivalabs.demo

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
    fromApplication<Application>().with(ContainersConfig::class).run(*args)
}
