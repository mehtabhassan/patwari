package pk.com.patwari.service

import org.springframework.stereotype.Service
import pk.com.patwari.constant.AccountType
import pk.com.patwari.constant.TransactionType
import pk.com.patwari.dto.request.AccountCreationRequest
import pk.com.patwari.dto.request.FundsTransferRequest
import pk.com.patwari.dto.response.AccountDetailsResponse
import pk.com.patwari.model.Account
import pk.com.patwari.model.Account.Companion.toAccountEntity
import pk.com.patwari.model.AccountLedger.Companion.mapLedgerEntry
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
            val updatedSrcAcc = debitAccount(srcAcc, requestDto.amount)
            requestDto.mapLedgerEntry(TransactionType.DEBIT, updatedSrcAcc.balance)
        }

        val destAcc = accountRepository.findByAccountNumber(requestDto.destAccount)
        val updatedDestAcc = creditAccount(destAcc, requestDto.amount)
        requestDto.mapLedgerEntry(TransactionType.CREDIT, updatedDestAcc.balance)
    }

    fun debitAccount(account: Account, amount: Double): Account{
        return when(account.accountType){
            AccountType.ASSETS, AccountType.EXPENSES -> {
                accountRepository.addAmount(account.id, amount)
            }
            AccountType.EQUITY, AccountType.LIABILITIES, AccountType.REVENUE -> {
                accountRepository.subtractAmount(account.id, amount)
            }
        }
    }

    fun creditAccount(account: Account, amount: Double): Account{
        return when(account.accountType){
            AccountType.ASSETS, AccountType.EXPENSES -> {
                accountRepository.subtractAmount(account.id, amount)
            }

            AccountType.EQUITY, AccountType.LIABILITIES, AccountType.REVENUE -> {
                accountRepository.addAmount(account.id, amount)
            }
        }
    }
}