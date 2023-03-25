package pk.com.patwari.service

import org.springframework.stereotype.Service
import pk.com.patwari.dto.response.AccountDetailsResponse
import pk.com.patwari.repository.AccountRepository

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun getAllAccounts(): List<AccountDetailsResponse>{
        return accountRepository.findAll().map { AccountDetailsResponse(it.id) }
    }
}