package patwari

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pk.com.patwari.PatwariApplication

@ActiveProfiles("test")
@SpringBootTest(classes = [PatwariApplication::class])
class PatwariApplicationTest {

    @Test
    fun contextLoads() {
        PatwariApplication.main(arrayOf())
    }

}