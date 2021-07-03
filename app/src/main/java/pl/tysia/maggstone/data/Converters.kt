package pl.tysia.maggstone.data

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType: Type =
            object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toJSON(value: String?): JSONObject {
        return JSONObject(value)
    }

    @TypeConverter
    fun fromJSON(json: JSONObject?): String {
        return json.toString()
    }
}