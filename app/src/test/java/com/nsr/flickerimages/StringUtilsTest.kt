package com.nsr.flickerimages

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.nsr.flickerimages.utils.annotatedString

import org.junit.Assert.assertEquals
import org.junit.Test

class StringUtilsTest {

    @Test
    fun `annotatedString should return formatted string`() {
        val title = "Title"
        val subTitle = "North American Porcupine"
        val expectedAnnotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
            )
            ) {
                append(title)
                append("\n")
            }
            append(subTitle)
        }

        val result = annotatedString(title, subTitle)

        // Assert
        assertEquals(expectedAnnotatedString.toString(), result.toString())
    }

}
