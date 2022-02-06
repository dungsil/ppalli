// (c) 2022 companyname All rights reserved.
package yourpackage.api

import org.junit.jupiter.api.DisplayName
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import kotlin.test.*

@ActiveProfiles("test")
@SpringBootTest(classes = [ProjectnameApi::class], webEnvironment = RANDOM_PORT)
class ProjectnameApiTest {

  @Test
  @DisplayName("정상적으로 어플리케이션이 실행되는지 확인")
  fun contextLoads() {
  }
}
