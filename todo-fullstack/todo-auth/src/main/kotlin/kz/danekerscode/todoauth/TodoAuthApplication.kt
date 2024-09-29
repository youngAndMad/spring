package kz.danekerscode.todoauth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodoAuthApplication

fun main(args: Array<String>) {
    runApplication<TodoAuthApplication>(*args)
}
