package pk.com.patwari.model

import com.devskiller.friendly_id.FriendlyId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import pk.com.patwari.constant.TransactionType
import pk.com.patwari.dto.request.FundsTransferRequest
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
class AccountLedger(
    @Id
    var id: String = FriendlyId.createFriendlyId(),
    val accountId: String = "",
    val amount: Double = 0.0,
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType = TransactionType.DEBIT,
    val closingBalance: Double = 0.0,
    val description: String = "",
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now()
){
    companion object{
        fun FundsTransferRequest.mapLedgerEntry(transactionType: TransactionType, closingBalance: Double): AccountLedger {
            return AccountLedger(
                accountId = this.srcAccount.orEmpty(),
                amount = this.amount,
                transactionType = TransactionType.CREDIT,
                closingBalance = closingBalance,
                description = this.description
            )
        }
    }
}