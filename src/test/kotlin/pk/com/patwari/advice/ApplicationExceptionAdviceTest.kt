package pk.com.patwari.advice

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ApplicationExceptionAdviceTest{

    lateinit var exceptionAdvice: ApplicationExceptionAdvice
    lateinit var objectMapper: ObjectMapper

    val defaultErrorMessage = "Unexpected error occurred"

    @BeforeEach
    fun beforeEach() {
        exceptionAdvice = ApplicationExceptionAdvice()
        objectMapper = ObjectMapper()
    }

    @Nested
    inner class `Base Exception` {
        @Test
        fun `Handle`() {
            val exception = Exception(defaultErrorMessage)
            val responseEntity: ResponseEntity<Any> = exceptionAdvice.handleGenericException(exception)
            val body: JsonNode = objectMapper.convertValue(responseEntity.body, JsonNode::class.java)

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.statusCode)
            assertEquals(defaultErrorMessage, body["errors"][0].asText())
        }
    }
}