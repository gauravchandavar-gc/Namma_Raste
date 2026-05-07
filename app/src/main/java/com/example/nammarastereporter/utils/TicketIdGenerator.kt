package com.example.nammarastereporter.utils

import java.util.UUID

object TicketIdGenerator {

    /**
     * Generates a unique ticket ID in the format NR-XXXXXX
     * where XXXXXX is the first 6 characters of a UUID in uppercase.
     */
    fun generate(): String {
        val uuid = UUID.randomUUID().toString().replace("-", "")
        val shortId = uuid.substring(0, 6).uppercase()
        return "NR-$shortId"
    }
}
