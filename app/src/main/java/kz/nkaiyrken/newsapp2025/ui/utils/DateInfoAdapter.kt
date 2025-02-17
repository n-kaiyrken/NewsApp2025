package kz.nkaiyrken.newsapp2025.ui.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kz.nkaiyrken.newsapp2025.domain.model.DateInfo
import java.lang.reflect.Type

class DateInfoAdapter : JsonDeserializer<DateInfo>, JsonSerializer<DateInfo> {
    override fun serialize(src: DateInfo?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return when (src) {
            is DateInfo.Today -> JsonObject().apply {
                addProperty("type", "Today")
                addProperty("time", src.time)
            }
            is DateInfo.Yesterday -> JsonObject().apply {
                addProperty("type", "Yesterday")
                addProperty("time", src.time)
            }
            is DateInfo.FullDate -> JsonObject().apply {
                addProperty("type", "FullDate")
                addProperty("formattedDate", src.formattedDate)
            }
            else -> JsonNull.INSTANCE
        }
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DateInfo {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid DateInfo format")
        return when (val type = jsonObject.get("type").asString) {
            "Today" -> DateInfo.Today(jsonObject.get("time").asString)
            "Yesterday" -> DateInfo.Yesterday(jsonObject.get("time").asString)
            "FullDate" -> DateInfo.FullDate(jsonObject.get("formattedDate").asString)
            else -> throw JsonParseException("Unknown DateInfo type: $type")
        }
    }
}
