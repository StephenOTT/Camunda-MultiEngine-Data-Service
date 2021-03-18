package com.github.stephenott.camunda.multienginedataservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.camunda.bpm.engine.history.HistoricDecisionInstance
import java.util.*


data class HistoricDecisionInstanceDto(
    override val _id: String? = null,
    override val id: String? = null,
    val decisionDefinitionId: String? = null,
    val decisionDefinitionKey: String? = null,
    val decisionDefinitionName: String? = null,
    val evaluationTime: Date? = null,
    val removalTime: Date? = null,
    val processDefinitionId: String? = null,
    val processDefinitionKey: String? = null,
    val processInstanceId: String? = null,
    val rootProcessInstanceId: String? = null,
    val caseDefinitionId: String? = null,
    val caseDefinitionKey: String? = null,
    val caseInstanceId: String? = null,
    val activityId: String? = null,
    val activityInstanceId: String? = null,
    val userId: String? = null,

    @JsonInclude(JsonInclude.Include.ALWAYS)
    val inputs: List<HistoricDecisionInputInstanceDto>? = null,

    @JsonInclude(JsonInclude.Include.ALWAYS)
    val outputs: List<HistoricDecisionOutputInstanceDto>? = null,

    val collectResultValue: Double? = null,
    val rootDecisionInstanceId: String? = null,
    val decisionRequirementsDefinitionId: String? = null,
    val decisionRequirementsDefinitionKey: String? = null,
    override val tenantId: String? = null,

    override val metadata: EngineMetadata? = null
): HistoricData {

    override val EVENT_TYPE: String = "historicDecisionInstance"

    constructor(historicDecisionInstance: HistoricDecisionInstance, metadata: EngineMetadata? = null, dbId: String? = null): this(
        id = historicDecisionInstance.id,
                decisionDefinitionId = historicDecisionInstance.decisionDefinitionId,
                decisionDefinitionKey = historicDecisionInstance.decisionDefinitionKey,
                decisionDefinitionName = historicDecisionInstance.decisionDefinitionName,
                evaluationTime = historicDecisionInstance.evaluationTime,
                removalTime = historicDecisionInstance.removalTime,
                processDefinitionId = historicDecisionInstance.processDefinitionId,
                processDefinitionKey = historicDecisionInstance.processDefinitionKey,
                processInstanceId = historicDecisionInstance.processInstanceId,
                caseDefinitionId = historicDecisionInstance.caseDefinitionId,
                caseDefinitionKey = historicDecisionInstance.caseDefinitionKey,
                caseInstanceId = historicDecisionInstance.caseInstanceId,
                activityId = historicDecisionInstance.activityId,
                activityInstanceId = historicDecisionInstance.activityInstanceId,
                userId = historicDecisionInstance.userId,
                collectResultValue = historicDecisionInstance.collectResultValue,
                rootDecisionInstanceId = historicDecisionInstance.rootDecisionInstanceId,
                rootProcessInstanceId = historicDecisionInstance.rootProcessInstanceId,
                decisionRequirementsDefinitionId = historicDecisionInstance.decisionRequirementsDefinitionId,
                decisionRequirementsDefinitionKey = historicDecisionInstance.decisionRequirementsDefinitionKey,
                tenantId = historicDecisionInstance.tenantId,
                inputs = historicDecisionInstance.inputs.map { HistoricDecisionInputInstanceDto(it) },
                outputs = historicDecisionInstance.outputs.map { HistoricDecisionOutputInstanceDto(it) },
                metadata = metadata,
                _id = dbId
    )

}
