package io.arkitik.radix.starter.logger

import io.arkitik.radix.port.logger.RadixLoggerPortContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
@Configuration
@Import(value = [RadixLoggerPortContext::class])
class SpringRadixLoggerAutoConfiguration