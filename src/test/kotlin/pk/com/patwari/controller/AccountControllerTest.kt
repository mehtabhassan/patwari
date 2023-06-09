package pk.com.patwari.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import pk.com.patwari.advice.ApplicationExceptionAdvice
import pk.com.patwari.constant.AccountStatus
import pk.com.patwari.constant.AccountType
import pk.com.patwari.dto.response.AccountDetailsResponse
import pk.com.patwari.service.AccountService

@ExtendWith(MockitoExtension::class)
class AccountControllerTest{

    private lateinit var mvc: MockMvc
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var service: AccountService
    private val BASE_URI = "/v1/account"

    @BeforeEach
    fun setup() {
        this.objectMapper = ObjectMapper()
        this.mvc = MockMvcBuilders.standaloneSetup(AccountController(service))
            .setControllerAdvice(ApplicationExceptionAdvice())
            .build()
    }

    @Nested
    inner class FetchAllAccounts {
        @Test
        @Throws(Exception::class)
        fun success() {
            val accountList = listOf(AccountDetailsResponse("some-account-id", "some-account-title", "some-account-number", 0.0, AccountType.ASSETS, AccountStatus.ACTIVE ))

            whenever(service.getAllAccounts()).thenReturn(accountList)

            mvc.perform(MockMvcRequestBuilders.get(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(accountList)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

            Mockito.verify(service, Mockito.times(1)).getAllAccounts()
        }
    }

    @Nested
    inner class CreateAccount {
        @Test
        fun success() {
            val requestPayload = "{\"id\":\"1234567890\",\"accountTitle\":\"TEST ACCOUNT\",\"accountNumber\":\"ACC-0001\",\"accountType\":\"ASSETS\"}"
            val responsePayload  = AccountDetailsResponse("some-account-id", "some-account-title", "some-account-number", 0.0, AccountType.ASSETS, AccountStatus.ACTIVE )

            whenever(service.createAccount(any())).thenReturn(responsePayload)

            mvc.perform(MockMvcRequestBuilders.post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

            Mockito.verify(service, Mockito.times(1)).createAccount(any())
        }

        @Test
        fun failure_in_case_of_bad_payload() {
            val payload = "{\"id\":\"1234567890\",\"accountTitle\":\"TEST ACCOUNT\",\"accountNumber\":\"ACC-0001\",\"accountType\":\"ABC\"}"

            mvc.perform(MockMvcRequestBuilders.post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError)

            Mockito.verify(service, Mockito.times(0)).createAccount(any())
        }
    }

    @Nested
    inner class FundTransfer {
        @Test
        fun successfully_receive_request_with_good_payload() {
            val requestPayload = "{\"srcAccount\":\"some-src-acc-num\",\"destAccount\":\"some-dest-acc-num\",\"amount\":\"100\",\"description\":\"\"}"

            whenever(service.fundTransfer(any())).thenReturn(any())

            mvc.perform(MockMvcRequestBuilders.post(BASE_URI + "/fund/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

            Mockito.verify(service, Mockito.times(1)).fundTransfer(any())
        }

        @Test
        fun successfully_receive_request_with_no_src_account() {
            val requestPayload = "{\"destAccount\":\"some-dest-acc-num\",\"amount\":\"100\",\"description\":\"\"}"

            whenever(service.fundTransfer(any())).thenReturn(any())

            mvc.perform(MockMvcRequestBuilders.post(BASE_URI + "/fund/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

            Mockito.verify(service, Mockito.times(1)).fundTransfer(any())
        }

        @Test
        fun failure_in_case_of_bad_payload() {
            val requestPayload = "{\"srcAccount\":\"some-src-acc-num\",\"amount\":\"abc\",\"description\":\"\"}"

            mvc.perform(MockMvcRequestBuilders.post(BASE_URI  + "/fund/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError)

            Mockito.verify(service, Mockito.times(0)).fundTransfer(any())
        }
    }
}