package com.nsr.flickerimages.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

fun formatDate(inputDate: String): String {
    // Parse the input date string to Instant
    val instant = Instant.parse(inputDate)

    // Convert Instant to LocalDateTime
    val localDateTime = LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId())

    // Format the LocalDateTime to a readable string
    val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH)
    return localDateTime.format(formatter)
}