package io.arkitik.radix.port.logger.function

import org.slf4j.Logger
import org.slf4j.event.Level
import org.zalando.logbook.Correlation
import org.zalando.logbook.HttpLogWriter
import org.zalando.logbook.Precorrelation

class RadixHttpLogWriter(
    private val logger: Logger,
    private val level: Level,
) : HttpLogWriter {
    private val activator: Logger.() -> Boolean = {
        when (level) {
            Level.ERROR -> isErrorEnabled
            Level.WARN -> isWarnEnabled
            Level.INFO -> isInfoEnabled
            Level.DEBUG -> isDebugEnabled
            Level.TRACE -> isTraceEnabled
        }
    }
    private val consumer: Logger.(String) -> Unit = { message: String ->
        when (level) {
            Level.ERROR -> error(message)
            Level.WARN -> warn(message)
            Level.INFO -> info(message)
            Level.DEBUG -> debug(message)
            Level.TRACE -> trace(message)
        }
    }

    override fun isActive(): Boolean {
        return activator(logger)
    }

    override fun write(precorrelation: Precorrelation, request: String) {
        logger.consumer(request)
    }

    override fun write(correlation: Correlation, response: String) {
        logger.consumer(response)
    }
}