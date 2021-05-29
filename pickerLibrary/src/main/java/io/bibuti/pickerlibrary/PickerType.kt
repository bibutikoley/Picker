package io.bibuti.pickerlibrary

import java.util.*

enum class MimeType(val key: String) {
    IMAGE("image/*"),
    VIDEO("video/*"),
    DOCUMENTS("application/*"),
    OTHERS("*/*");

    companion object {
        fun fromKey(key: String) = values().firstOrNull { it.key == key.lowercase(Locale.ROOT) } ?: OTHERS
        fun getLabel(pickerType: MimeType) =  displayMap[pickerType] ?: "Others"
    }

}

private val displayMap = mapOf(
    MimeType.IMAGE.key to "Image",
    MimeType.VIDEO to "Video",
    MimeType.DOCUMENTS to "Documents",
    MimeType.OTHERS to "Others",
)