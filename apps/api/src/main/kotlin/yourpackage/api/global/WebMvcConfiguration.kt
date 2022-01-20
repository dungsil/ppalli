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
package yourpackage.api.global

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


/**
 * Spring web mvc 설정
 */
@Configuration
@EnableWebMvc
class WebMvcConfiguration(val mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter) :
  WebMvcConfigurer {
  override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
    // `@EnableWebMvc` 어노테이션이 Jackson 설정을 덮어 버려서 다시 설정해줘야 한다.
    // https://stackoverflow.com/a/61925956
    for (i in converters.indices) {
      if (converters[i] is MappingJackson2HttpMessageConverter) {
        converters[i] = mappingJackson2HttpMessageConverter
      }
    }
  }
}
