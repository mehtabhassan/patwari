package pk.com.patwari.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AccountServiceTest{

    private lateinit var service: AccountService

    @BeforeEach
    fun setup() {
        this.service = AccountService()
    }

    @Nested
    inner class GetAllAccounts{
        @Test
        fun shouldReturnAccountList(){

            val accountList = service.getAllAccounts()

            assertEquals(1, accountList.size)
        }
    }
}