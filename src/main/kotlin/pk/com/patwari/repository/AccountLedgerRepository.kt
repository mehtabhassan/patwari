package pk.com.patwari.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pk.com.patwari.model.AccountLedger

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
interface AccountLedgerRepository : CrudRepository<AccountLedger, String>