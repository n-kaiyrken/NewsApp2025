package kz.nkaiyrken.newsapp2025.ui.utils

import android.content.Context
import kz.nkaiyrken.newsapp2025.R
import kz.nkaiyrken.newsapp2025.domain.model.DateInfo

fun formatDateString(context: Context, dateInfo: DateInfo): String {
    return when (dateInfo) {
        is DateInfo.Today -> "${context.getString(R.string.today)}, ${dateInfo.time}"
        is DateInfo.Yesterday -> "${context.getString(R.string.yesterday)}, ${dateInfo.time}"
        is DateInfo.FullDate -> dateInfo.formattedDate
    }
}