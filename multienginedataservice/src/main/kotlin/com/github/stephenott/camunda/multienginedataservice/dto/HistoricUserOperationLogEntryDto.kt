package com.github.stephenott.camunda.multienginedataservice.dto

import org.camunda.bpm.engine.impl.history.event.UserOperationLogEntryEventEntity
import java.util.*


data class HistoricUserOperationLogEntryDto(
    override val _id: String? = null,
    override val id: String? = null,
    val deploymentId: String? = null,
    val processDefinitionId: String? = null,
    val processDefinitionKey: String? = null,
    val processInstanceId: String? = null,
    val executionId: String? = null,
    val caseDefinitionId: String? = null,
    val caseInstanceId: String? = null,
    val caseExecutionId: String? = null,
    val taskId: String? = null,
    val jobId: String? = null,
    val jobDefinitionId: String? = null,
    val batchId: String? = null,
    val userId: String? = null,
    val timestamp: Date? = null,
    val operationId: String? = null,
    val externalTaskId: String? = null,
    val operationType: String? = null,
    val entityType: String? = null,
    val property: String? = null,
    val orgValue: String? = null,
    val newValue: String? = null,
    val removalTime: Date? = null,
    val rootProcessInstanceId: String? = null,
    val category: String? = null,
    val annotation: String? = null,
    override val tenantId: String? = null,

    override val sequenceCounter: Long? = null,

    override val metadata: EngineMetadata? = null

) : HistoricData, SequenceCounted {

    override val EVENT_TYPE: String = "historicUserOperationLogEntry"

    constructor(userOperationLogEntryEventEntity: UserOperationLogEntryEventEntity, metadata: EngineMetadata? = null, dbId: String? = null) : this(
        id = userOperationLogEntryEventEntity.id,
        deploymentId = userOperationLogEntryEventEntity.deploymentId,
        processDefinitionId = userOperationLogEntryEventEntity.processDefinitionId,
        processDefinitionKey = userOperationLogEntryEventEntity.processDefinitionKey,
        processInstanceId = userOperationLogEntryEventEntity.processInstanceId,
        executionId = userOperationLogEntryEventEntity.executionId,
        caseDefinitionId = userOperationLogEntryEventEntity.caseDefinitionId,
        caseInstanceId = userOperationLogEntryEventEntity.caseInstanceId,
        caseExecutionId = userOperationLogEntryEventEntity.caseExecutionId,
        taskId = userOperationLogEntryEventEntity.taskId,
        jobId = userOperationLogEntryEventEntity.jobId,
        jobDefinitionId = userOperationLogEntryEventEntity.jobDefinitionId,
        batchId = userOperationLogEntryEventEntity.batchId,
        userId = userOperationLogEntryEventEntity.userId,
        timestamp = userOperationLogEntryEventEntity.timestamp,
        operationId = userOperationLogEntryEventEntity.operationId,
        externalTaskId = userOperationLogEntryEventEntity.externalTaskId,
        operationType = userOperationLogEntryEventEntity.operationType,
        entityType = userOperationLogEntryEventEntity.entityType,
        property = userOperationLogEntryEventEntity.property,
        orgValue = userOperationLogEntryEventEntity.orgValue,
        newValue = userOperationLogEntryEventEntity.newValue,
        removalTime = userOperationLogEntryEventEntity.removalTime,
        rootProcessInstanceId = userOperationLogEntryEventEntity.rootProcessInstanceId,
        category = userOperationLogEntryEventEntity.category,
        annotation = userOperationLogEntryEventEntity.annotation,
        tenantId = userOperationLogEntryEventEntity.tenantId,
        sequenceCounter = userOperationLogEntryEventEntity.sequenceCounter,
        metadata = metadata,
        _id = dbId
    )
}


