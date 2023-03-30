package pk.com.patwari.service

import org.springframework.stereotype.Service
import pk.com.patwari.constant.AccountType
import pk.com.patwari.dto.request.AccountCreationRequest
import pk.com.patwari.dto.request.FundsTransferRequest
import pk.com.patwari.dto.response.AccountDetailsResponse
import pk.com.patwari.model.Account
import pk.com.patwari.model.Account.Companion.toAccountEntity
import pk.com.patwari.repository.AccountRepository
import javax.transaction.Transactional

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun getAllAccounts(): List<AccountDetailsResponse>{
        return accountRepository.findAll().map { AccountDetailsResponse(
            it.id, it.accountTitle, it.accountNumber, it.balance, it.accountType, it.status
        )}
    }

    fun createAccount(requestDto: AccountCreationRequest): AccountDetailsResponse {
        val account = accountRepository.save(requestDto.toAccountEntity())
        return AccountDetailsResponse(account.id, account.accountTitle, account.accountNumber, account.balance, account.accountType, account.status)
    }

    @Transactional
    fun fundTransfer(requestDto: FundsTransferRequest) {

        if(!requestDto.srcAccount.isNullOrEmpty()) {
            val srcAcc = accountRepository.findByAccountNumber(requestDto.srcAccount!!)
            debitAccount(srcAcc, requestDto.amount)
        }

        val destAcc = accountRepository.findByAccountNumber(requestDto.destAccount)
        creditAccount(destAcc, requestDto.amount)
    }

    fun debitAccount(account: Account, amount: Double){
        when(account.accountType){
            AccountType.ASSETS, AccountType.EXPENSES -> {
                accountRepository.addAmount(account.id, amount)
            }
            AccountType.EQUITY, AccountType.LIABILITIES, AccountType.REVENUE -> {
                accountRepository.subtractAmount(account.id, amount)
            }
        }
    }

    fun creditAccount(account: Account, amount: Double){
        var updatedAccount: Account
        when(account.accountType){
            AccountType.ASSETS, AccountType.EXPENSES -> {
                updatedAccount = accountRepository.subtractAmount(account.id, amount)
            }
            AccountType.EQUITY, AccountType.LIABILITIES, AccountType.REVENUE -> {
                updatedAccount = accountRepository.addAmount(account.id, amount)
            }
        }
    }
}