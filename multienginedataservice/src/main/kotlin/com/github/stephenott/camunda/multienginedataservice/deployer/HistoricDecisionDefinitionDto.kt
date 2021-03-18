package com.github.stephenott.camunda.multienginedataservice.deployer

import com.github.stephenott.camunda.multienginedataservice.dto.HistoricData
import com.github.stephenott.camunda.multienginedataservice.dto.EngineMetadata

data class HistoricDecisionDefinitionDto(
    override val _id: String? = null,
    override val id: String? = null,
    val key: String? = null,
    val category: String? = null, //@TODO what is this?
    val name: String? = null,
    val version: Int = 0,
    val resource: String? = null, //@TODO what is this?
    val deploymentId: String? = null,
    override val tenantId: String? = null,
    val versionTag: String? = null,
    val historyTimeToLive: Int? = null, //@TODO can this be changed post deployment?

    val previousDecisionDefinitionId: String? = null,

    var decisionRequirementsDefinitionId: String? = null,
    var decisionRequirementsDefinitionKey: String? = null,

    val moddled: Map<String, Any?>? = null,

    override val metadata: EngineMetadata? = null

) : HistoricData {

    override val EVENT_TYPE: String = "historicDecisionDefinition"
}





