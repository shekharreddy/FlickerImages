package com.nsr.flickerimages.utils


// Extract width and height from description
fun extractWidthAndHeight(inputText: String): Pair<Int, Int> {
    // Define the regex pattern to match width and height attributes
    val pattern = Regex("width=\"(\\d+)\".*height=\"(\\d+)\"")

    // Find the match in the input text
    val matchResult = pattern.find(inputText)

    // Extract the width and height values from the match
    return matchResult?.let {
        val width = it.groupValues[1].toIntOrNull()
        val height = it.groupValues[2].toIntOrNull()

        if (width != null && height != null) {
            Pair(width, height)
        } else {
            Pair(160, 120)// Default value
        }
    }?: Pair(160, 120) // default value
}