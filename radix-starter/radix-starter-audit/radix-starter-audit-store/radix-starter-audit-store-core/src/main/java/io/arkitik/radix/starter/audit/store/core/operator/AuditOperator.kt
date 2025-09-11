package io.arkitik.radix.starter.audit.store.core.operator

import io.arkitik.radix.starter.audit.store.core.log.AuditLogData

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface AuditOperator<I> {
    fun <T> addHistoryRecord(
        keyName: String,
        oldValue: T?,
        newValue: T?,
        mapper: (T) -> String?,
    ) = addHistoryRecord(
        AuditLogData(
            keyName = keyName,
            oldValue = oldValue,
            newValue = newValue,
        ),
        mapper
    )

    fun <T> addHistoryRecord(
        log: AuditLogData<T>,
        mapper: (T) -> String? = { it?.toString() },
    ): AuditOperator<I>
}
