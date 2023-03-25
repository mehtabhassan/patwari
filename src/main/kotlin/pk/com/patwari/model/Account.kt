package pk.com.patwari.model

import com.devskiller.friendly_id.FriendlyId
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import pk.com.patwari.constant.AccountStatus
import pk.com.patwari.constant.AccountType
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Account(
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: String = "",
    val accountTitle: String,
    val accountNumber: String,
    val accountType: AccountType,
    val status: AccountStatus,
    @CreatedBy
    val createdBy: String = "",
    @LastModifiedBy
    val updatedBy: String = "",
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now()
){
    init {
        this.id = id ?: FriendlyId.createFriendlyId()
    }
}