package com.github.stephenott.camunda.multienginedataservice.dto

import java.util.*

interface DurationTracked {
    val durationInMillis: Long?
    val startTime: Date?
    val endTime: Date?
}