package pk.com.patwari.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Account(
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    val id: String = "",

    val title: String = ""
)