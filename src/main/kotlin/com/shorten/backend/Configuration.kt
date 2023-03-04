package com.shorten.backend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class Configuration : WebMvcConfigurer {
    /* Create a password encoder for encoding/decoding passwords
     */
    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}
