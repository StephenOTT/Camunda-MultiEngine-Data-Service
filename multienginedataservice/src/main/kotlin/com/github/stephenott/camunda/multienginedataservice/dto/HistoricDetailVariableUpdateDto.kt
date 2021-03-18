package com.github.stephenott.camunda.multienginedataservice.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.camunda.bpm.engine.impl.history.event.HistoricVariableUpdateEventEntity
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity
import java.util.*


//@JsonTypeName("variableUpdate")
data class HistoricDetailVariableUpdateDto(
    override val _id: String? = null,
    override val id: String? = null,
    override val processDefinitionKey: String? = null,
    override val processDefinitionId: String? = null,
    override val processInstanceId: String? = null,
    override val activityInstanceId: String? = null,
    override val executionId: String? = null,
    override val caseDefinitionKey: String? = null,
    override val caseDefinitionId: String? = null,
    override val caseInstanceId: String? = null,
    override val caseExecutionId: String? = null,
    override val taskId: String? = null,
    override val tenantId: String? = null,
    override val userOperationId: String? = null,
    override val time: Date? = null,
    override val removalTime: Date? = null,
    override val rootProcessInstanceId: String? = null,

    val variableName: String? = null,
    val variableInstanceId: String? = null,

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl = Any::class)
    val value: Any? = null,

    val initial: Boolean? = null,

    val revision: Int = 0,
//    val errorMessage: String? = null,

    override val sequenceCounter: Long? = null,

    val eventType: String? = null,

    override val metadata: EngineMetadata? = null

) : HistoricDetail, SequenceCounted {

    override val EVENT_TYPE: String = "historicDetailVariableUpdate"

    constructor(instance: HistoricVariableUpdateEventEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = instance.id,
        processDefinitionKey = instance.processDefinitionKey,
        processDefinitionId = instance.processDefinitionId,
        processInstanceId = instance.processInstanceId,
        activityInstanceId = instance.activityInstanceId,
        executionId = instance.executionId,
        caseDefinitionKey = instance.caseDefinitionKey,
        caseDefinitionId = instance.caseDefinitionId,
        caseInstanceId = instance.caseInstanceId,
        caseExecutionId = instance.caseExecutionId,
        taskId = instance.taskId,
        userOperationId = instance.userOperationId,
        time = instance.timestamp,
        removalTime = instance.removalTime,
        rootProcessInstanceId = instance.rootProcessInstanceId,

        variableName = instance.variableName,
        variableInstanceId = instance.variableInstanceId,
        value = HistoricVariableInstanceEntity(instance).typedValue.value,
        initial = instance.isInitial, //@TODO review how this is used and if required? Is this used in the CAM db? (currently a create event does not get initial... so is it actually needed?)
        revision = instance.revision, // @TODO should revision start at 0 or 1?  Review what camunda does.
        sequenceCounter = instance.sequenceCounter,
        eventType = instance.eventType,
        metadata = metadata,
        _id = dbId
    )

}