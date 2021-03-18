package com.github.stephenott.camunda.multienginedataservice.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS
import org.camunda.bpm.engine.impl.history.event.HistoricVariableUpdateEventEntity
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity
import java.util.*

data class HistoricVariableInstanceDto(
    override val _id: String? = null,
    override val id: String? = null,
    val name: String? = null,
    val processDefinitionKey: String? = null,
    val processDefinitionId: String? = null,
    val processInstanceId: String? = null,
    val executionId: String? = null,
    val activityInstanceId: String? = null,
    val caseDefinitionKey: String? = null,
    val caseDefinitionId: String? = null,
    val caseInstanceId: String? = null,
    val caseExecutionId: String? = null,
    val taskId: String? = null,
    override val tenantId: String? = null,
    val state: String? = null,
    val createTime: Date? = null,
    val removalTime: Date? = null,
    val rootProcessInstanceId: String? = null,

    @JsonTypeInfo(
        use = CLASS, include = EXTERNAL_PROPERTY, property = "type", defaultImpl = Any::class
    )
    val value: Any? = null,

    override val relations: Relations? = null,

    override val metadata: EngineMetadata? = null

) : HistoricData, Relatable {

    override val EVENT_TYPE: String = "historicVariableInstance"

    constructor(historicVariableUpdateEventEntity: HistoricVariableUpdateEventEntity) : this(
        HistoricVariableInstanceEntity(historicVariableUpdateEventEntity)
    )

    constructor(historicVariableInstance: HistoricVariableInstanceEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = historicVariableInstance.id,
        name = historicVariableInstance.name,
        processDefinitionKey = historicVariableInstance.processDefinitionKey,
        processDefinitionId = historicVariableInstance.processDefinitionId,
        processInstanceId = historicVariableInstance.processInstanceId,
        executionId = historicVariableInstance.executionId,
        activityInstanceId = historicVariableInstance.activityInstanceId,
        caseDefinitionKey = historicVariableInstance.caseDefinitionKey,
        caseDefinitionId = historicVariableInstance.caseDefinitionId,
        caseInstanceId = historicVariableInstance.caseInstanceId,
        caseExecutionId = historicVariableInstance.caseExecutionId,
        taskId = historicVariableInstance.taskId,
        tenantId = historicVariableInstance.tenantId,
        state = historicVariableInstance.state,
        createTime = historicVariableInstance.createTime,
        removalTime = historicVariableInstance.removalTime,
        rootProcessInstanceId = historicVariableInstance.rootProcessInstanceId,
        value = historicVariableInstance.typedValue.value,
        metadata = metadata,
        _id = dbId
    )

    val isLocalVariable: Boolean? =
        if (processInstanceId != null && activityInstanceId != null) {
            processInstanceId != activityInstanceId
        } else null
}

