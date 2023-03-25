package pk.com.patwari.dto.response

import pk.com.patwari.constant.AccountStatus
import pk.com.patwari.constant.AccountType

data class AccountDetailsResponse(
    val id: String,
    val accountTitle: String,
    val accountNumber: String,
    val accountType: AccountType,
    val status: AccountStatus,
)
