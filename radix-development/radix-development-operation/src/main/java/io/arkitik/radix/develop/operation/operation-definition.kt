package io.arkitik.radix.develop.operation

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 21, **Sun February, 2021**
 * Project *radix* [https://arkitik.io]
 */
fun interface Operation<RQ, RS> {
    fun RQ.operate(): RS
}

fun interface OperationRole<RQ, RS> {
    fun RQ.operateRole(): RS
}

fun interface Operator<RQ, RS> {
    fun RQ.operate(response: RS)
}
