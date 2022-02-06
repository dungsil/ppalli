// (c) 2022 companyname All rights reserved.
package yourpackage.api.infrastructure.error

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import yourpackage.api.account.AccountService
import yourpackage.api.auth.jwt.JwtService
import yourpackage.api.global.error.GlobalErrorController

@WebMvcTest(GlobalErrorController::class)
class GlobalErrorControllerTest {

  @Autowired
  lateinit var mock: MockMvc

  @MockBean
  lateinit var jwtService: JwtService

  @MockBean
  lateinit var accountService: AccountService

  @Test
  @DisplayName("정상적으로 오류코드를 리턴하는지 확인")
  fun handleError() {
    mock.perform(get("/error"))
      .andExpect { handler().handlerType(GlobalErrorController::class.java) } // 제대로 등록되었는지 확인
      .andExpect { status().isNotFound } // 상태코드가 정상적으로 표시되는지 확인
  }
}
