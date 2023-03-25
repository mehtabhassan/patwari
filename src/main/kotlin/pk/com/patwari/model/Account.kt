package pk.com.patwari.model

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Account(
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    val id: String = "",

    val title: String = ""
)