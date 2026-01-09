package com.cnl.istudy_sts

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableAdminServer
@SpringBootApplication
class IStudySTSApplication

fun main(args: Array<String>) {
    System.setProperty("spring.devtools.restart.enabled", "true");
    runApplication<IStudySTSApplication>(*args)
}
