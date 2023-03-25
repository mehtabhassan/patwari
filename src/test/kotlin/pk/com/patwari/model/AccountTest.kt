package pk.com.patwari.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pk.com.patwari.constant.AccountStatus
import pk.com.patwari.constant.AccountType

class AccountTest{
    @Nested
    inner class AccountModelTest{
        @Test
        fun modelTest(){
            val model = Account(accountNumber = "some-account-number", accountTitle = "some-account-title", accountType = AccountType.ASSETS, status = AccountStatus.ACTIVE)
            assertEquals("some-account-number", model.accountNumber)
            assertEquals("some-account-title", model.accountTitle)
            assertEquals(AccountType.ASSETS, model.accountType)
            assertEquals(AccountStatus.ACTIVE, model.status)
        }
    }
}