package com.nsr.flickerimages.model

data class FlickerSearchResponse(
    val description: String,
    val generator: String,
    val items: List<Item>,
    val link: String,
    val modified: String,
    val title: String
)