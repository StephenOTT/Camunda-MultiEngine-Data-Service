package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.impl.history.event.HistoricTaskInstanceEventEntity
import java.util.*

data class HistoricTaskInstanceDto(
    override val _id: String? = null,
    override val id: String? = null,
    val processDefinitionKey: String? = null,
    val processDefinitionId: String? = null,
    val processInstanceId: String? = null,
    val executionId: String? = null,
    val caseDefinitionKey: String? = null,
    val caseDefinitionId: String? = null,
    val caseInstanceId: String? = null,
    val caseExecutionId: String? = null,
    val activityInstanceId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val deleteReason: String? = null,
    val owner: String? = null,
    val assignee: String? = null,
    override val startTime: Date? = null,
    override val endTime: Date? = null,
    override val durationInMillis: Long? = null,
    val taskDefinitionKey: String? = null,
    val priority: Int = 0,
    val due: Date? = null,
    val parentTaskId: String? = null,
    val followUp: Date? = null,
    override val tenantId: String? = null,
    val removalTime: Date? = null,
    val rootProcessInstanceId: String? = null,

    override val sequenceCounter: Long? = null,

    override val relations: Relations? = null,

    override val metadata: EngineMetadata? = null,

    ): HistoricData, DurationTracked, SequenceCounted, Relatable {

    override val EVENT_TYPE: String = "historicTaskInstance"

    constructor(historicTaskInstanceEventEntity: HistoricTaskInstanceEventEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = historicTaskInstanceEventEntity.id,
        processDefinitionKey = historicTaskInstanceEventEntity.processDefinitionKey,
        processDefinitionId = historicTaskInstanceEventEntity.processDefinitionId,
        processInstanceId = historicTaskInstanceEventEntity.processInstanceId,
        executionId = historicTaskInstanceEventEntity.executionId,
        caseDefinitionKey = historicTaskInstanceEventEntity.caseDefinitionKey,
        caseDefinitionId = historicTaskInstanceEventEntity.caseDefinitionId,
        caseInstanceId = historicTaskInstanceEventEntity.caseInstanceId,
        caseExecutionId = historicTaskInstanceEventEntity.caseExecutionId,
        activityInstanceId = historicTaskInstanceEventEntity.activityInstanceId,
        name = historicTaskInstanceEventEntity.name,
        description = historicTaskInstanceEventEntity.description,
        deleteReason = historicTaskInstanceEventEntity.deleteReason,
        owner = historicTaskInstanceEventEntity.owner,
        assignee = historicTaskInstanceEventEntity.assignee,
        startTime = historicTaskInstanceEventEntity.startTime,
        endTime = historicTaskInstanceEventEntity.endTime,
        durationInMillis = historicTaskInstanceEventEntity.durationInMillis,
        taskDefinitionKey = historicTaskInstanceEventEntity.taskDefinitionKey,
        priority = historicTaskInstanceEventEntity.priority,
        due = historicTaskInstanceEventEntity.dueDate,
        parentTaskId = historicTaskInstanceEventEntity.parentTaskId,
        followUp = historicTaskInstanceEventEntity.followUpDate,
        tenantId = historicTaskInstanceEventEntity.tenantId,
        removalTime = historicTaskInstanceEventEntity.removalTime,
        rootProcessInstanceId = historicTaskInstanceEventEntity.rootProcessInstanceId,
        sequenceCounter = historicTaskInstanceEventEntity.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )
}

