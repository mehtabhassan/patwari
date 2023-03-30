package pk.com.patwari.dto.request

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pk.com.patwari.constant.AccountType

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountCreationRequest (
    val accountTitle: String = "",
    val accountNumber: String = "",
    val accountType: AccountType = AccountType.ASSETS
)
