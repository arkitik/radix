package io.arkitik.radix.tool.json.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */

/**
 * Creates a [JsonArray], calls the provided function on it, and returns it
 */
fun radixJsonObject(
    init: RadixJsonObjectBuilder.() -> Unit,
): JsonObject =
    RadixJsonObjectBuilder()
        .apply(init)
        .build()

/**
 * Returns a [JsonArray] with the elements provided.
 *
 * Items provided must either be [Boolean], [Char], [String], [Number], or [JsonElement]
 */
fun radixJsonArray(vararg item: Any?): JsonArray {
    return item.toList().toJsonArray()
}

/**
 * Converts a [List] to a [JsonArray]
 */
fun List<*>.toJsonArray(): JsonArray {
    val jsonArray = JsonArray()
    this.forEach {
        when (it) {
            is Boolean -> jsonArray.add(it)
            is Char -> jsonArray.add(it)
            is String -> jsonArray.add(it)
            is Number -> jsonArray.add(it)
            is JsonElement -> jsonArray.add(it)
            null -> jsonArray.add(JsonNull.INSTANCE)
            else -> jsonArray.add(it.toString())
        }
    }
    return jsonArray
}

/**
 * Alternative syntax for [List].[toJsonArray]. Converts the [List] into a [JsonArray].
 */
fun <T> radixJsonArray(list: List<T>) = list.toJsonArray()

/**
 * Converts a [HashMap] to a [JsonObject]
 */
fun <K, V> HashMap<K, V>.toJsonObject(): JsonObject {
    return radixJsonObject {
        forEach { (k, v) -> k.toString() to v }
    }
}
