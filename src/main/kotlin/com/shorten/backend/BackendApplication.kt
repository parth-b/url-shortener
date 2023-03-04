package com.shorten.backend
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(exclude = [
    /* Don't auto-configure Spring Security.
     * We added spring security only to make use of its password encoding,
     * which we will configure explicitly.
     */
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration::class,
    org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration::class
])
@EnableCaching
@ComponentScan("com.shorten.backend.service", "com.shorten.backend.controller",     "com.shorten.backend.repo",
    "com.shorten.backend.model")
@EnableScheduling
@EnableJpaRepositories("package")
class BackendApplication {
    public fun BackendApplication(){

    }
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}

