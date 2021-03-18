package com.github.stephenott.camunda.multienginedataservice.dto


import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.camunda.bpm.engine.history.HistoricDecisionInputInstance
import org.camunda.bpm.engine.impl.persistence.entity.util.TypedValueField
import org.camunda.bpm.engine.impl.variable.serializer.ValueFields
import java.util.*


data class HistoricDecisionInputInstanceDto(
    val id: String? = null, //@TODO review if the ID is needed or could be removed?
    val decisionInstanceId: String? = null,
    val clauseId: String? = null,
    val clauseName: String? = null,
//    val errorMessage: String? = null,
    val createTime: Date? = null,
    val removalTime: Date? = null, //@TODO Is this required?  Could possibly be removed because it is now nested as part of the DecisionInstance DTO
    val rootProcessInstanceId: String? = null,

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    val value: Any? = null
) {
    constructor(historicDecisionInputInstanceEntity: HistoricDecisionInputInstance): this(
        id = historicDecisionInputInstanceEntity.id,
        decisionInstanceId = historicDecisionInputInstanceEntity.decisionInstanceId,
        clauseId = historicDecisionInputInstanceEntity.clauseId,
        clauseName = historicDecisionInputInstanceEntity.clauseName,
        createTime = historicDecisionInputInstanceEntity.createTime,
        removalTime = historicDecisionInputInstanceEntity.removalTime,
        rootProcessInstanceId = historicDecisionInputInstanceEntity.rootProcessInstanceId,
        value = historicDecisionInputInstanceEntity.value
    )
}

