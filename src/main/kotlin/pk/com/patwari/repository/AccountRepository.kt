package pk.com.patwari.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pk.com.patwari.model.Account

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
interface AccountRepository : CrudRepository<Account, String>{

    fun findByAccountNumber(accountNumber: String): Account

    @Modifying
    @Query("UPDATE Account a " +
        "SET a.balance = a.balance + :amount, " +
        "a.updatedAt = CURRENT_TIMESTAMP() " +
        "WHERE a.id = :accountId")
    fun addAmount(@Param("accountId") accountId: String, @Param("amount") amount: Double): Account

    @Modifying
    @Query("UPDATE Account a " +
        "SET a.balance = a.balance - :amount, " +
        "a.updatedAt = CURRENT_TIMESTAMP() " +
        "WHERE a.id = :accountId")
    fun subtractAmount(@Param("accountId") accountId: String, @Param("amount") amount: Double): Account
}