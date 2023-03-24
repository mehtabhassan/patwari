package pk.com.patwari.service

import org.springframework.stereotype.Service
import pk.com.patwari.dto.response.AccountDetailsResponse

@Service
class AccountService {
    fun getAllAccounts(): List<AccountDetailsResponse>{
        return listOf(AccountDetailsResponse("some-id"))
    }
}