package pk.com.patwari.dto.request

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class FundsTransferRequest (
    @JsonProperty("srcAccount") val srcAccount: String? = "",
    @JsonProperty("destAccount") val destAccount: String,
    @JsonProperty("amount") val amount: Double,
    @JsonProperty("description") val description: String,
)
