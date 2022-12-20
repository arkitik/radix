package io.arkitik.radix.port.logger.function

import org.zalando.logbook.LogbookCreator

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 1:11 AM, 25 , **Sat, June 2022**
 * Project *radix* [arkitik.io](https://arkitik.io)
 */
fun interface RadixLoggerCustomizer {
    fun customize(logbook: LogbookCreator.Builder)
}
