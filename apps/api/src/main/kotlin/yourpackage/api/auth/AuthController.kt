/*
 * MIT License
 *
 * Copyright ⓒ 2022 Kim Younggeon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * SPDX-License-Identifier: MIT
 */
package yourpackage.api.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yourpackage.api.account.AccountService
import yourpackage.api.auth.jwt.JwtService
import yourpackage.api.auth.jwt.JwtToken
import yourpackage.api.global.utils.getAccessToken
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * 계정 인증 컨트롤러
 */
@RestController
@RequestMapping("/auth")
class AuthController(
  private val accounts: AccountService,
  private val jwts: JwtService,
) {

  /**
   * 인증 요청 (로그인)
   */
  @PostMapping
  fun authorize(@Valid @RequestBody auth: AuthorizationRequest): JwtToken {
    val account = accounts.authorize(auth.username!!, auth.rawPassword!!)
    return jwts.issueToken(account)
  }

  @DeleteMapping
  fun revoke(req: HttpServletRequest): ResponseEntity<Unit> {
    val accssToken = req.getAccessToken()
      ?: return ResponseEntity.badRequest().build() // 토큰이 없으면 무시

    jwts.revokeToken(accssToken)

    return ResponseEntity
      .noContent()
      .build()
  }
}
