package com.fityatra.models

import java.io.Serializable

data class Exercise(
    val name: String,
    val image: String,
    val description:String,
    val video:String
):Serializable

