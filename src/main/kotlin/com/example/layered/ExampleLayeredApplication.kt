package com.example.layered

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExampleLayeredApplication

fun main(args: Array<String>) {
    runApplication<ExampleLayeredApplication>(*args)
}
