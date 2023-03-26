package pk.com.patwari.service

import org.springframework.stereotype.Service
import pk.com.patwari.dto.request.AccountCreationRequest
import pk.com.patwari.dto.response.AccountDetailsResponse
import pk.com.patwari.model.Account.Companion.toAccountEntity
import pk.com.patwari.repository.AccountRepository

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun getAllAccounts(): List<AccountDetailsResponse>{
        return accountRepository.findAll().map { AccountDetailsResponse(
            it.id, it.accountTitle, it.accountNumber, it.accountType, it.status
        )}
    }

    fun createAccount(requestDto: AccountCreationRequest): AccountDetailsResponse {
        val account = accountRepository.save(requestDto.toAccountEntity())
        return AccountDetailsResponse(account.id, account.accountTitle, account.accountNumber, account.accountType, account.status)
    }
}