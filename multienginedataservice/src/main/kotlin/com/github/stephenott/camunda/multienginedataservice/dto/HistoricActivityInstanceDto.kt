package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity
import java.util.*

data class HistoricActivityInstanceDto(
    override val _id: String? = null,
    override val id: String? = null,
    val parentActivityInstanceId: String? = null,
    val activityId: String? = null,
    val activityName: String? = null,
    val activityType: String? = null,
    val processDefinitionKey: String? = null,
    val processDefinitionId: String? = null,
    val processInstanceId: String? = null,
    val executionId: String? = null,
    val taskId: String? = null,
    val calledProcessInstanceId: String? = null,
    val calledCaseInstanceId: String? = null,
    val assignee: String? = null,
    override val startTime: Date? = null,
    override val endTime: Date? = null,
    override val durationInMillis: Long? = null,
    val canceled: Boolean? = null,
    val completeScope: Boolean? = null,
    override val tenantId: String? = null,
    val removalTime: Date? = null,
    val rootProcessInstanceId: String? = null,
    override val sequenceCounter: Long? = null,

    override val metadata: EngineMetadata? = null
) : HistoricData, DurationTracked, SequenceCounted {

    override val EVENT_TYPE: String = "historicActivityInstance"

    constructor(instance: HistoricActivityInstanceEventEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = instance.id,
        parentActivityInstanceId = instance.parentActivityInstanceId,
        activityId = instance.activityId,
        activityName = instance.activityName,
        activityType = instance.activityType,
        processDefinitionKey = instance.processDefinitionKey,
        processDefinitionId = instance.processDefinitionId,
        processInstanceId = instance.processInstanceId,
        executionId = instance.executionId,
        taskId = instance.taskId,
        calledProcessInstanceId = instance.calledProcessInstanceId,
        calledCaseInstanceId = instance.calledCaseInstanceId,
        assignee = instance.assignee,
        startTime = instance.startTime,
        endTime = instance.endTime,
        durationInMillis = instance.durationInMillis,
        canceled = instance.isCanceled,
        completeScope = instance.isCompleteScope,
        tenantId = instance.tenantId,
        removalTime = instance.removalTime,
        rootProcessInstanceId = instance.rootProcessInstanceId,
        sequenceCounter = instance.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )
}

