package com.nsr.flickerimages

import com.nsr.flickerimages.utils.formatDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DateFormatUtilsTest {

    @Test
    fun `formatDate should return formatted date`() {
        // Arrange
        val inputDate = "2024-02-14T23:36:51Z"
        val expectedFormattedDate = "02-14-2024"

        // Act
        val result = formatDate(inputDate)

        // Assert
        assertEquals(expectedFormattedDate, result)
    }
}
