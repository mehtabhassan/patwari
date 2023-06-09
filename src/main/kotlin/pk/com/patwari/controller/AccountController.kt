package pk.com.patwari.controller

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pk.com.patwari.dto.request.AccountCreationRequest
import pk.com.patwari.dto.request.FundsTransferRequest
import pk.com.patwari.service.AccountService

@RestController
@RequestMapping("/v1/account")
class AccountController(val accountService: AccountService){

    private val logger: Logger = LogManager.getLogger(PingController::class.java)

    @GetMapping
    fun getAllAccounts() = accountService.getAllAccounts()

    @PostMapping
    fun createAccount(@RequestBody requestBody: AccountCreationRequest) = accountService.createAccount(requestBody)

    @PostMapping("/fund/transfer")
    fun fundTransfer(@RequestBody requestBody: FundsTransferRequest) = accountService.fundTransfer(requestBody)

}