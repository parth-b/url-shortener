package com.shorten.backend

import com.shorten.backend.auth.enums.ErrorCode
import com.shorten.backend.auth.exceptions.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping


@ControllerAdvice
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class BackendControllerAdvice {

    private val logger = KotlinLogging.logger {}

    private fun captureHandledException(e: Exception) {
        logger.error(e) { "Internal Error" }
    }

    @ExceptionHandler
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<BackendError> {
        val error = BackendError("Validation failure")

        val bindingResult = e.bindingResult

        if (bindingResult.hasGlobalErrors()) {
            val globalErrorList = mutableListOf<ErrorItem>()
            for (err in bindingResult.globalErrors) {
                globalErrorList.add(ErrorItem(err.defaultMessage))
            }
            error.globalErrors = globalErrorList
        }

        if (bindingResult.hasFieldErrors()) {
            val fieldErrorMap = mutableMapOf<String, MutableList<ErrorItem>>()
            for (err in bindingResult.fieldErrors) {
                val errorList = fieldErrorMap.getOrPut(err.field) { mutableListOf() }
                errorList.add(ErrorItem(err.defaultMessage))
            }
            error.fieldErrors = fieldErrorMap
        }

        captureHandledException(e)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleUserDeactivatedException(e: UserDeactivatedException): ResponseEntity<BackendError> {
        val error = BackendError("Your account has been deactivated. Please contact us at support@gmail.com")
        error.code = ErrorCode.USER_DEACTIVATED.number
        error.globalErrors = listOf(ErrorItem("Your account has been deactivated. Please contact us at support@gmail.com"))
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }


    @ExceptionHandler
    fun handleEmailAlreadyRegisteredException(e: EmailAlreadyRegisteredException): ResponseEntity<BackendError> {
        val error = BackendError("This email address is already being used for another account. Please re-confirm and try again.")
        error.fieldErrors = mapOf("email" to listOf(ErrorItem("Already registered")))
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

}

class BackendError(var message: String) {
    var code: Int = ErrorCode.DEFAULT_ERROR.number
    var globalErrors: List<ErrorItem> = emptyList()
    var fieldErrors: Map<String, List<ErrorItem>> = emptyMap()
    var details: Map<String, Any> = emptyMap()
}

class ErrorItem(var message: String?) {
    var details: Map<String, Any> = emptyMap()
}
