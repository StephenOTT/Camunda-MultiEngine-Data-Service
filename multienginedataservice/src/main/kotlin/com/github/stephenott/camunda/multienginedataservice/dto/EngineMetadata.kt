package com.github.stephenott.camunda.multienginedataservice.dto

import java.time.Instant

data class EngineMetadata(
    val clusterName: String? = null,
    val engineName: String? = null,
    val hostname: String? = null,
    val createdAt: Instant? = null,
    val transactionId: String? = null,
)
