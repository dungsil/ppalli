// (c) 2022 companyname All rights reserved.
package yourpackage.servlet.utils

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import yourpackage.api.global.error.GlobalErrorController
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class GetAccessTokenTest {
  private val mock: MockMvc = MockMvcBuilders
    .standaloneSetup(GlobalErrorController::class)
    .build()

  @Test
  @DisplayName("헤더가 빈 값일 때")
  fun testNullHeader() {
    mock.perform(get("/"))
      .andDo { assertNull(it.request.getAccessToken()) }
  }

  @Test
  @DisplayName("Bearer token : HS256")
  fun testBearerHs256() {
    val req = mock.perform(
      get("/")
        .header(
          "Authorization",
          "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        )
    )

    req.andDo {
      assertEquals(
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
        it.request.getAccessToken()
      )
    }
  }
}
