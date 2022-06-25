package io.arkitik.radix.tool.json.dsl

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
class RadixJsonObjectBuilder {

    private val jsonObject = JsonObject()

    /**
     * Add a property with the given key
     *
     * Syntax: "key" to value
     */
    infix fun <T> String.to(value: T?) =
        when (value) {
            is Boolean -> jsonObject.addProperty(this, value)
            is Char -> jsonObject.addProperty(this, value)
            is String -> jsonObject.addProperty(this, value)
            is Number -> jsonObject.addProperty(this, value)
            is JsonElement -> jsonObject.add(this, value)
            null -> jsonObject.add(this, JsonNull.INSTANCE)
            else -> jsonObject.addProperty(this, value.toString())
        }

    /**
     * Return the completed [JsonObject]
     */
    fun build() = jsonObject
}
