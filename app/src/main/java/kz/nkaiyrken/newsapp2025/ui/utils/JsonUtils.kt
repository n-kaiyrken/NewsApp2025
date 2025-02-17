package kz.nkaiyrken.newsapp2025.ui.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kz.nkaiyrken.newsapp2025.domain.model.DateInfo

object JsonUtils {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(DateInfo::class.java, DateInfoAdapter())
        .create()
}