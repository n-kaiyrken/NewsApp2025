package kz.nkaiyrken.newsapp2025.domain.model

sealed class DateInfo {
    data class Today(val time: String) : DateInfo()
    data class Yesterday(val time: String) : DateInfo()
    data class FullDate(val formattedDate: String) : DateInfo()
}

