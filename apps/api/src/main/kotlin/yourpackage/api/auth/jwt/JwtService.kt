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
package yourpackage.api.auth.jwt

import yourpackage.api.account.Account

/**
 * Jwt 서비스
 */
interface JwtService {

  /**
   * 토큰 발급
   *
   * @param account 토큰을 발급할 사용자 계정
   * @return JWT 토큰
   */
  fun issueToken(account: Account): JwtToken

  fun validateToken(token: String?): Boolean

  /**
   * 토큰을 파싱해서 계정 정보를 가져온다.
   *
   * @param accessToken 파싱할 토큰
   * @return 토큰 사용자의 기본키
   */
  fun getAccountIdByToken(accessToken: String): Long

  /**
   * 사용자 토큰 제거
   *
   * @param accessToken 엑세스 토큰
   */
  fun revokeToken(accessToken: String)
}
