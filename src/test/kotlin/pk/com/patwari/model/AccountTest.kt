package pk.com.patwari.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AccountTest{
    @Nested
    inner class AccountModelTest{
        @Test
        fun modelTest(){
            val model = Account(id = "some-account-id", title = "some-account-title")
            assertEquals("some-account-id", model.id)
            assertEquals("some-account-title", model.title)
        }
    }
}