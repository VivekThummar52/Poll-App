package com.example.vivekpracticalnov1.roomdbclasses

import androidx.room.TypeConverter
import com.example.vivekpracticalnov1.data.Option
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OptionListConverter {
    @TypeConverter
    fun fromOptionList(optionList: List<Option>?): String? {
        return Gson().toJson(optionList)
    }

    @TypeConverter
    fun toOptionList(optionListString: String?): List<Option>? {
        val listType = object : TypeToken<List<Option>>() {}.type
        return Gson().fromJson(optionListString, listType)
    }
}
