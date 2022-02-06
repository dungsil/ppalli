// (c) 2022 companyname All rights reserved.
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
class WebMvcConfig(val mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter) :
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
