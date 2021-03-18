package com.github.stephenott.camunda.multienginedataservice.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.camunda.bpm.engine.impl.history.event.HistoricFormPropertyEventEntity
import java.util.*

//@JsonTypeName("formField")
data class HistoricDetailFormFieldDto(
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

    val fieldId: String? = null,

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl = Any::class)
    val fieldValue: Any? = null,

    override val sequenceCounter: Long? = null,

    override val metadata: EngineMetadata? = null

) : HistoricDetail, SequenceCounted {

    override val EVENT_TYPE: String = "historicDetailFormField"

    constructor(instance: HistoricFormPropertyEventEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
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
        time = instance.time,
        removalTime = instance.removalTime,
        rootProcessInstanceId = instance.rootProcessInstanceId,

        fieldId = instance.propertyId,
        fieldValue = instance.propertyValue,

        sequenceCounter = instance.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )

}