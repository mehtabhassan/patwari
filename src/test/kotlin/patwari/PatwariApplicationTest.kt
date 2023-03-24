package patwari

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.SocketUtils
import pk.com.patwari.PatwariApplication

@ActiveProfiles("test")
@SpringBootTest(classes = [PatwariApplication::class])
class PatwariApplicationTest {

    companion object {
        init {
            System.setProperty("spring.profiles.active", "test")
            System.setProperty("server.port", SocketUtils.findAvailableTcpPort().toString())
        }
    }
    @Test
    fun contextLoads() {
        PatwariApplication.main(arrayOf())
    }

}