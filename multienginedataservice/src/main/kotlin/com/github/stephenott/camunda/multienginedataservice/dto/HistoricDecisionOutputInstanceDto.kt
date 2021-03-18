package com.github.stephenott.camunda.multienginedataservice.dto


import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.camunda.bpm.engine.history.HistoricDecisionOutputInstance
import org.camunda.bpm.engine.impl.persistence.entity.util.TypedValueField
import org.camunda.bpm.engine.impl.variable.serializer.ValueFields
import java.util.*


data class HistoricDecisionOutputInstanceDto(
    val id: String? = null, //@TODO review if this is needed / can it be removed?
    val decisionInstanceId: String? = null,
    val clauseId: String? = null,
    val clauseName: String? = null,
    val ruleId: String? = null,
    val ruleOrder: Int? = null,
    val variableName: String? = null,
//    val errorMessage: String? = null,
    val createTime: Date? = null,
    val removalTime: Date? = null, //@TODO Is this required?  Could possibly be removed because it is now nested as part of the DecisionInstance DTO
    val rootProcessInstanceId: String? = null,

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl = Any::class)
    val value: Any? = null
) {
    constructor(historicDecisionOutputInstanceEntity: HistoricDecisionOutputInstance): this(
        id = historicDecisionOutputInstanceEntity.id,
        decisionInstanceId = historicDecisionOutputInstanceEntity.decisionInstanceId,
        clauseId = historicDecisionOutputInstanceEntity.clauseId,
        clauseName = historicDecisionOutputInstanceEntity.clauseName,
        ruleId = historicDecisionOutputInstanceEntity.ruleId,
        ruleOrder = historicDecisionOutputInstanceEntity.ruleOrder,
        variableName = historicDecisionOutputInstanceEntity.variableName,
        createTime = historicDecisionOutputInstanceEntity.createTime,
        removalTime = historicDecisionOutputInstanceEntity.removalTime,
        rootProcessInstanceId = historicDecisionOutputInstanceEntity.rootProcessInstanceId,
        value = historicDecisionOutputInstanceEntity.value
    )
}

