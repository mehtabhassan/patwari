package pk.com.patwari.controller

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/account")
class AccountController {

    private val logger: Logger = LogManager.getLogger(PingController::class.java)
    @GetMapping
    fun createAccount() = logger.info("Will return all account")

}