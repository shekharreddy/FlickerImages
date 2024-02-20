package com.nsr.flickerimages.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun annotatedString(title: String, subTitle: String): AnnotatedString = buildAnnotatedString {
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