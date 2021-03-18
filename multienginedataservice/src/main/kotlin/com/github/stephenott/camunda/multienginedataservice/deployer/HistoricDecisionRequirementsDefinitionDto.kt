package com.github.stephenott.camunda.multienginedataservice.deployer

import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.github.stephenott.camunda.multienginedataservice.dto.EngineMetadata

data class HistoricDecisionRequirementsDefinitionDto(
    override val _id: String? = null,
    override val id: String? = null,
    val key: String? = null,
    val category: String? = null, //@TODO what is this?
    val name: String? = null,
    val version: Int = 0,
    val resource: String? = null, //@TODO what is this?
    val deploymentId: String? = null,
    val diagram: String? = null, //@TODO what is this?
    override val tenantId: String? = null,

    val previousDecisionRequirementsDefinitionId: String? = null,

    val decisions: List<String>? = null,

    val moddled: Map<String, Any?>? = null,

    override val metadata: EngineMetadata? = null

) : HistoricData {

    override val EVENT_TYPE: String = "historicDecisionRequirementsDefinition"
}


