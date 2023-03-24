package pk.com.patwari.advice

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApplicationExceptionAdvice {

    private val logger: Logger = LogManager.getLogger(ApplicationExceptionAdvice::class.java)
    private val ERROR_KEY = "errors"
    private val errorMessage = "An {} exception has occurred, errors : [{}]"

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Any> {
        logger.error(errorMessage,
            ex.javaClass.name, listOf(ex.message).joinToString(), ex)

        return ResponseEntity(
            mapOf(ERROR_KEY to listOf(errorMessage)),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}