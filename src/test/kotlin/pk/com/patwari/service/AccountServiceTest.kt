package pk.com.patwari.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import pk.com.patwari.constant.AccountStatus
import pk.com.patwari.constant.AccountType
import pk.com.patwari.model.Account
import pk.com.patwari.repository.AccountRepository

@ExtendWith(MockitoExtension::class)
class AccountServiceTest{

    @Mock
    private lateinit var accountRepository: AccountRepository
    private lateinit var service: AccountService

    @BeforeEach
    fun setup() {
        this.service = AccountService(accountRepository)
    }

    @Nested
    inner class GetAllAccounts{
        val account = Account(id = "some-account-id", accountNumber = "some-account-number", accountTitle = "some-account-title", accountType = AccountType.ASSETS, status = AccountStatus.ACTIVE)
        @Test
        fun shouldReturnAccountList(){

            whenever(accountRepository.findAll()).thenReturn(listOf(account))

            val accountList = service.getAllAccounts()

            assertEquals(1, accountList.size)
            assertEquals("some-account-id", accountList[0].id)
            assertEquals("some-account-number", accountList[0].accountNumber)
            assertEquals("some-account-title", accountList[0].accountTitle)
            assertEquals(AccountType.ASSETS, accountList[0].accountType)
            assertEquals(AccountStatus.ACTIVE, accountList[0].status)
        }
    }
}