package pk.com.patwari.dto.request

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import pk.com.patwari.constant.AccountType

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountCreationRequest (
    @JsonProperty("accountTitle") val accountTitle: String,
    @JsonProperty("accountNumber") val accountNumber: String,
    @JsonProperty("accountType") val accountType: AccountType
)
