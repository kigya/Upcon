package com.kigya.upcon.db

import androidx.room.TypeConverter
import com.kigya.upcon.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name ?: "null"
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}
