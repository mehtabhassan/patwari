package pk.com.patwari.dto.response

data class FundTransferResponse (
    var srcAccount: AccountDetails?,
    var destAccount: AccountDetails
)

data class AccountDetails(
    var accountNumber: String,
    var balance: Double
)
