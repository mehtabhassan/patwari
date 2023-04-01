package pk.com.patwari.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import pk.com.patwari.constant.AccountStatus
import pk.com.patwari.constant.AccountType
import pk.com.patwari.dto.request.AccountCreationRequest
import pk.com.patwari.dto.request.FundsTransferRequest
import pk.com.patwari.model.Account
import pk.com.patwari.model.AccountLedger
import pk.com.patwari.repository.AccountLedgerRepository
import pk.com.patwari.repository.AccountRepository

@ExtendWith(MockitoExtension::class)
class AccountServiceTest{

    @Mock
    private lateinit var accountRepository: AccountRepository
    @Mock
    private lateinit var accountLedgerRepository: AccountLedgerRepository
    private lateinit var service: AccountService

    @BeforeEach
    fun setup() {
        this.service = AccountService(accountRepository, accountLedgerRepository)
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

    @Nested
    inner class CreateAccount{
        @Captor
        private lateinit var accountCaptor: ArgumentCaptor<Account>

        private val requestDto = AccountCreationRequest(accountNumber = "some-account-number", accountTitle = "some-account-title", accountType = AccountType.ASSETS)
        private val account = Account(id = "some-account-id", accountNumber = "some-account-number", accountTitle = "some-account-title", accountType = AccountType.ASSETS, status = AccountStatus.ACTIVE)
        @Test
        fun shouldReturnAccountCreated(){

            whenever(accountRepository.save(any(Account::class.java))).thenReturn(account)

            val account = service.createAccount(requestDto)

            Mockito.verify(accountRepository, Mockito.times(1)).save(accountCaptor.capture())

            assertNotNull(account)
//            assertEquals("some-account-id", account.id)
            assertEquals(accountCaptor.value.accountNumber, account.accountNumber)
            assertEquals(accountCaptor.value.accountTitle, account.accountTitle)
            assertEquals(accountCaptor.value.accountType, account.accountType)
            assertEquals(AccountStatus.ACTIVE, account.status)

        }
    }

    @Nested
    inner class FundTransfer{
        @Captor
        lateinit var debitLedgerCaptor: ArgumentCaptor<AccountLedger>
        @Captor
        lateinit var creditLedgerCaptor: ArgumentCaptor<AccountLedger>

        private var requestDto = FundsTransferRequest(srcAccount = "some-src-account-number", destAccount = "some-dest-account-number", amount = 100.0, description = "")
        private var srcAccount = getSourceAccount(AccountType.ASSETS)
        private var destAccount = getDestAccount(AccountType.LIABILITIES)
        private var updatedSrcAccount = getSourceAccount(AccountType.ASSETS, 0.0)
        private var updatedDestAccount = getDestAccount(AccountType.EXPENSES, 200.0)

        @Test
        fun `should only process for debit account when src account is empty`(){

            requestDto = FundsTransferRequest(destAccount = "some-dest-account-number", amount = 100.0, description = "")

            whenever(accountRepository.findByAccountNumber("some-dest-account-number")).thenReturn(destAccount)
            whenever(accountRepository.addAmount("some-dest-account-id", 100.0)).thenReturn(updatedDestAccount)

            val response = service.fundTransfer(requestDto)

            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-dest-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).addAmount("some-dest-account-id", 100.0)
            Mockito.verify(accountRepository, Mockito.times(0)).subtractAmount("",0.0)
            Mockito.verify(accountLedgerRepository, Mockito.times(1)).save(any())

            assertNull(response.srcAccount)
            assertEquals(200.0, response.destAccount.balance)
            assertEquals("some-dest-account-number", response.destAccount.accountNumber)
        }

        @Test
        fun `should increase amount in both accounts when src is asset dest is liability`(){
            updatedSrcAccount = getSourceAccount(AccountType.ASSETS, 200.0)

            whenever(accountRepository.findByAccountNumber("some-src-account-number")).thenReturn(srcAccount)
            whenever(accountRepository.findByAccountNumber("some-dest-account-number")).thenReturn(destAccount)
            whenever(accountRepository.addAmount("some-src-account-id", 100.0)).thenReturn(updatedSrcAccount)
            whenever(accountRepository.addAmount("some-dest-account-id", 100.0)).thenReturn(updatedDestAccount)

            val response = service.fundTransfer(requestDto)

            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-src-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-dest-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).addAmount("some-src-account-id", 100.0)
            Mockito.verify(accountRepository, Mockito.times(1)).addAmount("some-dest-account-id", 100.0)
            Mockito.verify(accountRepository, Mockito.times(0)).subtractAmount("",0.0)
            Mockito.verify(accountLedgerRepository, Mockito.times(2)).save(any())

            assertEquals("some-src-account-number", response.srcAccount?.accountNumber)
            assertEquals(200.0, response.srcAccount?.balance)
            assertEquals("some-dest-account-number", response.destAccount.accountNumber)
            assertEquals(200.0, response.destAccount.balance)
        }

        @Test
        fun `should decrease amount in both accounts when src is liability dest is asset`(){
            srcAccount = getSourceAccount(AccountType.LIABILITIES)
            destAccount = getDestAccount(AccountType.ASSETS)

            updatedSrcAccount = getSourceAccount(AccountType.LIABILITIES, 0.0)
            updatedDestAccount = getDestAccount(AccountType.ASSETS, 0.0)

            whenever(accountRepository.findByAccountNumber("some-src-account-number")).thenReturn(srcAccount)
            whenever(accountRepository.findByAccountNumber("some-dest-account-number")).thenReturn(destAccount)
            whenever(accountRepository.subtractAmount("some-src-account-id", 100.0)).thenReturn(updatedSrcAccount)
            whenever(accountRepository.subtractAmount("some-dest-account-id", 100.0)).thenReturn(updatedDestAccount)

            val response = service.fundTransfer(requestDto)

            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-src-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-dest-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).subtractAmount("some-src-account-id", 100.0)
            Mockito.verify(accountRepository, Mockito.times(1)).subtractAmount("some-dest-account-id", 100.0)
            Mockito.verify(accountRepository, Mockito.times(0)).addAmount("",0.0)
            Mockito.verify(accountLedgerRepository, Mockito.times(2)).save(any())

            assertEquals("some-src-account-number", response.srcAccount?.accountNumber)
            assertEquals(0.0, response.srcAccount?.balance)
            assertEquals("some-dest-account-number", response.destAccount.accountNumber)
            assertEquals(0.0, response.destAccount.balance)
        }

        @Test
        fun `decrease amount liability as src acc and increase amount in revenue as dest acc`(){
            srcAccount = getSourceAccount(AccountType.LIABILITIES)
            destAccount = getDestAccount(AccountType.REVENUE)
            updatedSrcAccount = getSourceAccount(AccountType.LIABILITIES, 0.0)
            updatedDestAccount = getDestAccount(AccountType.REVENUE, 200.0)

            whenever(accountRepository.findByAccountNumber("some-src-account-number")).thenReturn(srcAccount)
            whenever(accountRepository.findByAccountNumber("some-dest-account-number")).thenReturn(destAccount)
            whenever(accountRepository.subtractAmount("some-src-account-id", 100.0)).thenReturn(updatedSrcAccount)
            whenever(accountRepository.addAmount("some-dest-account-id", 100.0)).thenReturn(updatedDestAccount)

            val response = service.fundTransfer(requestDto)

            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-src-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-dest-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).subtractAmount("some-src-account-id", 100.0)
            Mockito.verify(accountRepository, Mockito.times(1)).addAmount("some-dest-account-id", 100.0)
            Mockito.verify(accountLedgerRepository, Mockito.times(2)).save(any())

            assertEquals("some-src-account-number", response.srcAccount?.accountNumber)
            assertEquals(0.0, response.srcAccount?.balance)
            assertEquals("some-dest-account-number", response.destAccount.accountNumber)
            assertEquals(200.0, response.destAccount.balance)
        }

        @Test
        fun `increase amount of asset as src acc and decrease amount of expense as dest acc`(){
            srcAccount = getSourceAccount(AccountType.ASSETS)
            destAccount = getDestAccount(AccountType.EXPENSES)
            updatedSrcAccount = getSourceAccount(AccountType.ASSETS, 200.0)
            updatedDestAccount = getDestAccount(AccountType.EXPENSES, 0.0)

            whenever(accountRepository.findByAccountNumber("some-src-account-number")).thenReturn(srcAccount)
            whenever(accountRepository.findByAccountNumber("some-dest-account-number")).thenReturn(destAccount)
            whenever(accountRepository.addAmount("some-src-account-id", 100.0)).thenReturn(updatedSrcAccount)
            whenever(accountRepository.subtractAmount("some-dest-account-id", 100.0)).thenReturn(updatedDestAccount)

            val response = service.fundTransfer(requestDto)

            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-src-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).findByAccountNumber("some-dest-account-number")
            Mockito.verify(accountRepository, Mockito.times(1)).addAmount("some-src-account-id", 100.0)
            Mockito.verify(accountRepository, Mockito.times(1)).subtractAmount("some-dest-account-id", 100.0)
            Mockito.verify(accountLedgerRepository, Mockito.times(2)).save(any())

            assertEquals("some-src-account-number", response.srcAccount?.accountNumber)
            assertEquals(200.0, response.srcAccount?.balance)
            assertEquals("some-dest-account-number", response.destAccount.accountNumber)
            assertEquals(0.0, response.destAccount.balance)
        }
    }

    companion object{
        fun getSourceAccount(accountType: AccountType, amount: Double = 100.0): Account{
            return Account(id = "some-src-account-id", accountNumber = "some-src-account-number", accountTitle = "some-account-title", accountType = accountType, status = AccountStatus.ACTIVE, balance = amount)
        }

        fun getDestAccount(accountType: AccountType, amount: Double = 100.0): Account{
            return Account(id = "some-dest-account-id", accountNumber = "some-dest-account-number", accountTitle = "some-account-title", accountType = accountType, status = AccountStatus.ACTIVE, balance = amount)
        }

        fun getDebitLedger(amount: Double, closingBalance: Double): AccountLedger{
            return AccountLedger(accountId = "some-src-account-id", amount = amount, closingBalance = closingBalance)
        }

        fun getCreditLedger(amount: Double, closingBalance: Double): AccountLedger{
            return AccountLedger(accountId = "some-dest-account-id", amount = amount, closingBalance = closingBalance)
        }
    }
}