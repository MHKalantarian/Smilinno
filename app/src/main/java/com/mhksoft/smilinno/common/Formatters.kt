package com.mhksoft.smilinno.common

import java.text.SimpleDateFormat

class Formatters {
    companion object {
        fun String.formatDate(): String {
            return try {
                SimpleDateFormat("yyyy/MM/dd HH:mm").format(
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(
                        this
                    )
                )
            } catch (e: Exception) {
                "Unknown Date"
            }
        }
    }
}