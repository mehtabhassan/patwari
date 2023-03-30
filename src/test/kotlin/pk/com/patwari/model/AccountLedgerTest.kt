package pk.com.patwari.model

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import pk.com.patwari.constant.AccountStatus
import pk.com.patwari.constant.AccountType
import pk.com.patwari.constant.TransactionType

class AccountLedgerTest {

    @Nested
    inner class AccountLedgerModelTest{
        @Test
        fun modelTest(){
            val model = AccountLedger(accountId = "some-account-id", amount = 0.0, transactionType = TransactionType.DEBIT, closingBalance = 0.0, description = "some-description")
            assertEquals("some-account-id", model.accountId)
            assertEquals(0.0, model.amount)
            assertEquals(TransactionType.DEBIT, model.transactionType)
            assertEquals(0.0, model.closingBalance)
            assertEquals("some-description", model.description)
        }
    }
}