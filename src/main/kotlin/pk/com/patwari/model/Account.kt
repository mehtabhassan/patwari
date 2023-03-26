package pk.com.patwari.model

import com.devskiller.friendly_id.FriendlyId
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import pk.com.patwari.constant.AccountStatus
import pk.com.patwari.constant.AccountType
import pk.com.patwari.dto.request.AccountCreationRequest
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Account(
    @Id
    var id: String = FriendlyId.createFriendlyId(),
    val accountTitle: String = "",
    val accountNumber: String = "",
    @Enumerated(EnumType.STRING)
    val accountType: AccountType = AccountType.ASSETS,
    @Enumerated(EnumType.STRING)
    val status: AccountStatus = AccountStatus.ACTIVE,
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now()
){
    companion object{
        fun AccountCreationRequest.toAccountEntity(): Account {
            return Account(
                accountTitle = this.accountTitle,
                accountNumber = this.accountNumber,
                accountType = this.accountType
            )
        }
    }
}