package pk.com.patwari.service

import org.springframework.stereotype.Service
import pk.com.patwari.constant.AccountType
import pk.com.patwari.constant.TransactionType
import pk.com.patwari.dto.request.AccountCreationRequest
import pk.com.patwari.dto.request.FundsTransferRequest
import pk.com.patwari.dto.response.AccountDetails
import pk.com.patwari.dto.response.AccountDetailsResponse
import pk.com.patwari.dto.response.FundTransferResponse
import pk.com.patwari.model.Account
import pk.com.patwari.model.Account.Companion.addAmount
import pk.com.patwari.model.Account.Companion.subtractAmount
import pk.com.patwari.model.Account.Companion.toAccountEntity
import pk.com.patwari.model.AccountLedger.Companion.mapLedgerEntry
import pk.com.patwari.repository.AccountLedgerRepository
import pk.com.patwari.repository.AccountRepository
import javax.transaction.Transactional

@Service
class AccountService(private val accountRepository: AccountRepository,
                     private val accountLedgerRepository: AccountLedgerRepository) {

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
    fun fundTransfer(requestDto: FundsTransferRequest): FundTransferResponse {

        var srcAccDetails: AccountDetails? = null; var destAccDetails: AccountDetails

        if(!requestDto.srcAccount.isNullOrEmpty()) {
            val srcAcc = accountRepository.findByAccountNumber(requestDto.srcAccount)
            val updatedSrcAcc = debitAccount(srcAcc, requestDto.amount)
            val debitLedger = requestDto.mapLedgerEntry(TransactionType.DEBIT, updatedSrcAcc.balance)
            accountRepository.save(updatedSrcAcc)
            accountLedgerRepository.save(debitLedger)

            srcAccDetails = AccountDetails(requestDto.srcAccount, debitLedger.closingBalance)
        }

        val destAcc = accountRepository.findByAccountNumber(requestDto.destAccount)
        val updatedDestAcc = creditAccount(destAcc, requestDto.amount)
        val creditLedger = requestDto.mapLedgerEntry(TransactionType.CREDIT, updatedDestAcc.balance)
        accountRepository.save(updatedDestAcc)
        accountLedgerRepository.save(creditLedger)

        destAccDetails = AccountDetails(requestDto.destAccount, creditLedger.closingBalance)

        return FundTransferResponse(srcAccDetails, destAccDetails)
    }

    fun debitAccount(account: Account, amount: Double): Account{
        return when(account.accountType){
            AccountType.ASSETS, AccountType.EXPENSES -> {
                account.addAmount(amount)
            }
            AccountType.EQUITY, AccountType.LIABILITIES, AccountType.REVENUE -> {
                account.subtractAmount(amount)
            }
        }
    }

    fun creditAccount(account: Account, amount: Double): Account{
        return when(account.accountType){
            AccountType.ASSETS, AccountType.EXPENSES -> {
                account.subtractAmount(amount)
            }

            AccountType.EQUITY, AccountType.LIABILITIES, AccountType.REVENUE -> {
                account.addAmount(amount)
            }
        }
    }
}