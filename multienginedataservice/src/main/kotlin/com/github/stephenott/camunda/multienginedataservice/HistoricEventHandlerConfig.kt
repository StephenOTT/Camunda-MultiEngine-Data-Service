package com.github.stephenott.camunda.multienginedataservice

data class HistoricEventHandlerConfig(
    val generateIdentityLinkInstances: Boolean,
    val clusterName: String?
)