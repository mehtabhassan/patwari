package pk.com.patwari.dto.request

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class FundsTransferRequest (
    val srcAccount: String? = "",
    val destAccount: String = "",
    val amount: Double = 0.0,
    val description: String = "",
)
