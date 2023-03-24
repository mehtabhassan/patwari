package pk.com.patwari.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import pk.com.patwari.ApplicationExceptionAdvice

class PingControllerTest{

    private lateinit var mvc: MockMvc
    private lateinit var objectMapper: ObjectMapper
    private val BASE_URI = "/v1/application/"

    @BeforeEach
    fun setup() {
        this.objectMapper = ObjectMapper()
        this.mvc = MockMvcBuilders.standaloneSetup(PingController())
            .setControllerAdvice(ApplicationExceptionAdvice())
            .build()
    }

    @Nested
    inner class batchCreateCashbook {
        @Test
        @Throws(Exception::class)
        fun success() {

            mvc.perform(MockMvcRequestBuilders.get(BASE_URI + "ping")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
        }
    }
}