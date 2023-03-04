package com.shorten.backend.auth.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shorten.backend.auth.models.*
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.transaction.BeforeTransaction
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.Charset
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.Signature
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTests {

    // Workaround for Mockito's issues with Kotlin's non-nullable types
    // https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    private fun <T> any(): T = Mockito.any<T>()
    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var preSignupRepo: PreSignupRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    private val VALID_PASSWORD = "testPassword123"

    private val VALID_EMAIL = "test@email.com"

    // added to assist {@code @Transactional(propagation = Propagation.NEVER)} to surface DataIntegrityViolationException
    @BeforeTransaction
    fun setup() {
        userRepo.deleteAll()
    }

    private fun createUserFixture(email: String? = null, password: String? = null, phone: String? = null, emailVerified: Boolean = true, deactivated: Boolean = false): User {
        val user = User()

        user.email = email ?: VALID_EMAIL
        user.password = passwordEncoder.encode(password ?: VALID_PASSWORD)

        if (phone != null) {
            user.phone = phone
        }

        if (emailVerified) {
            user.emailVerified = true
        }

        if (deactivated) {
            user.deactivated = true
        }

        userRepo.save(user)
        return user
    }

    @Test
    fun test_Signup_Without_Password() {
        val requestBody = mapOf(
            "email" to "test2@email.com"
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

        MatcherAssert.assertThat(userRepo.findByEmail(requestBody["email"]!!), Matchers.nullValue())
    }

    @Test
    fun test_Signup_With_InvalidEmail() {
        val requestBody = mapOf(
            "email" to "test",
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
            "password" to VALID_PASSWORD
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

        MatcherAssert.assertThat(userRepo.findByEmail(requestBody["email"] as String), Matchers.nullValue())
    }

    @Test
    fun test_Signup_With_DeactivatedEmail() {
        val registeredUser = createUserFixture(email = "test@gmail.com", password = VALID_PASSWORD, deactivated = true)

        val requestBody = mapOf(
            "email" to registeredUser.email,
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
            "password" to registeredUser.password
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun test_Signup_With_InvalidPassword() {
        val requestBody = mapOf(
            "email" to "test@email.com",
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
            "password" to "abc"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

        MatcherAssert.assertThat(userRepo.findByEmail(requestBody["email"] as String), Matchers.nullValue())
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    fun test_Signup_With_RegisteredEmail() {
        val registeredUser = createUserFixture()

        val requestBody = mapOf(
            "email" to registeredUser.email,
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
            "password" to VALID_PASSWORD
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    fun test_Signup_With_CaseFlippedRegisteredEmail() {
        val email = "test@email.com"
        createUserFixture(email)

        val requestBody = mapOf(
            "email" to email.toUpperCase(),
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
            "password" to VALID_PASSWORD
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

}


