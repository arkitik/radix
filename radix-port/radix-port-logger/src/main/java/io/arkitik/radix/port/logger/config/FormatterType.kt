package io.arkitik.radix.port.logger.config

import org.zalando.logbook.CurlHttpLogFormatter
import org.zalando.logbook.DefaultHttpLogFormatter
import org.zalando.logbook.HttpLogFormatter
import org.zalando.logbook.SplunkHttpLogFormatter
import org.zalando.logbook.json.JsonHttpLogFormatter

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
enum class FormatterType(val formatter: HttpLogFormatter) {
    DEFAULT(DefaultHttpLogFormatter()),
    JSON(JsonHttpLogFormatter()),
    CURL(CurlHttpLogFormatter()),
    SPLUNK(SplunkHttpLogFormatter())
}