package com.github.stephenott.camunda.multienginedataservice.deployer

import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.github.stephenott.camunda.multienginedataservice.dto.EngineMetadata
import java.util.*

data class HistoricDeploymentDto(
    override val _id: String? = null,
    override val id: String? = null,
    val name: String? = null,
    val resources: Set<String>? = null,
    val deploymentTime: Date? = null,
    val source: String? = null,
    override val tenantId: String? = null,
    override val metadata: EngineMetadata? = null
): HistoricData {

    override val EVENT_TYPE: String = "historicDeployment"

}
